package com.app.moudles.service;

import com.alibaba.fastjson.JSON;
import com.app.moudles.mapper.OrderMapper;
import com.app.moudles.mapper.OrderProductMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.common.constant.Const;
import com.common.common.enums.OrderStatus;
import com.common.common.enums.OrderTakeType;
import com.common.common.exception.ServiceException;
import com.common.common.util.GeneratorUtil;
import com.common.common.util.PayjsUtil;
import com.common.common.util.http.HttpUtil;
import com.common.common.util.result.Result;
import com.common.entity.app.OrderInfo;
import com.common.entity.app.OrderProducts;
import com.common.entity.app.form.CreateOrderForm;
import com.common.entity.app.vo.HistoryOrderVO;
import com.common.entity.app.vo.OrderInfoVO;
import com.common.entity.common.AppConfig;
import com.common.service.AppConfigService;
import com.common.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private AppConfigService appConfigService;
    @Resource
    private LockService lockService;
    @Resource
    private OrderProductMapper orderProductMapper;

    @Transactional(rollbackFor = Exception.class)
    public Result<String> createOrder(CreateOrderForm orderParams, String wxOpenid) {
        if (orderParams.getTotalPrice() < 0) {
            return Result.fail("订单的价格异常");
        }
        if (CollectionUtils.isEmpty(orderParams.getProductVOList())) {
            return Result.fail("不能生成空订单，请选择商品");
        }
        // 外卖配送 检验参数
        if (OrderTakeType.ENUM_take_out.value.equals(orderParams.getTakeType()))
            if (StringUtils.isEmpty(orderParams.getAddressDetail())
                    || orderParams.getAddressDetail().trim().length() < 3 // 尽量确保收货地址等正确性
                    || StringUtils.isEmpty(orderParams.getUserPhone())
                    || StringUtils.isEmpty(orderParams.getReceiver()))
                return Result.fail("收货信息错误, 请检查收获地址、手机和姓名");

        AppConfig appConfig = appConfigService.getAppConfig();
        if (!appConfig.getShopStatus()) {
            return Result.fail("商家休息中，下单失败");
        }
        Date currentTime = new Date();
        int nowHour = LocalTime.now(ZoneId.of("Asia/Shanghai")).getHour();
        if (!(nowHour >= appConfig.getBusinessStartTime() && nowHour < appConfig.getBusinessEndTime()))
            return Result.fail("未到营业时间，下单失败");

        if (!lockService.tryLock(Const.CONST_lock_redis_prefix + wxOpenid, ""))
            return Result.fail("正在下单中，请勿重复下单");

        try {
            // 订单数据保存
            OrderInfo orderInfo = new OrderInfo();
            BeanUtils.copyProperties(orderParams, orderInfo);
            orderInfo.setWxOpenid(wxOpenid);
            orderInfo.setOrderNo(GeneratorUtil.generateOrderNo());
            //默认下单成功就是直接制作中
            orderInfo.setOrderStatus(OrderStatus.ENUM_on_making.value);
            orderInfo.setCreateTime(currentTime);
            orderInfo.setGoodsPreview(orderParams.getGoodsPreview());
            orderInfo.setGoodsTotalNum(orderParams.getGoodsTotalNum());
            orderInfo.setTotalPrice(orderParams.getTotalPrice());
            orderInfo.setPayPrice(orderParams.getTotalPrice());
            orderMapper.insert(orderInfo);
            for(CreateOrderForm.ProductVO productVO : orderParams.getProductVOList()) {
                OrderProducts orderProducts = new OrderProducts();
                orderProducts.setOrderNo(orderInfo.getOrderNo());
                orderProducts.setGoodsDetail(productVO.getGoodsDetail());
                orderProducts.setGoodsTotalNum(productVO.getGoodsTotalNum());
                orderProducts.setTotalPrice(productVO.getTotalPrice());
                orderProducts.setImgUrl(productVO.getImgUrl());
                orderProductMapper.insert(orderProducts);
            }
            return Result.ok(orderInfo.getOrderNo());
        } finally {
            lockService.releaseLock(Const.CONST_lock_redis_prefix + wxOpenid);
        }
    }

    public Page<HistoryOrderVO> getHistoryOrderByPage(Integer pageNo, Integer pageSize, String wxOpenid) {
        Page<HistoryOrderVO> page = new Page<>(pageNo, pageSize);
        List<HistoryOrderVO> orderVOList = orderMapper.getHistoryOrderByPage(wxOpenid, (pageNo - 1) * pageSize, pageSize);
        if(!CollectionUtils.isEmpty(orderVOList)){
           List<String> orderNoList = orderVOList.stream().map(HistoryOrderVO::getOrderNo).collect(Collectors.toList());
           List<OrderProducts> orderProducts = orderProductMapper.selectList(new QueryWrapper<OrderProducts>().in("order_no", orderNoList));
           Map<String, List<OrderProducts>> map = CollectionUtils.isEmpty(orderProducts) ? null : orderProducts.stream().collect(Collectors.groupingBy(OrderProducts::getOrderNo));
           if(!CollectionUtils.isEmpty(map)){
               orderVOList.forEach(vo ->{
                   if(map.get(vo.getOrderNo()) != null){
                       vo.setProductVOList(map.get(vo.getOrderNo()).stream()
                               .map(pro -> new HistoryOrderVO.ProductVO(pro.getGoodsDetail(), pro.getGoodsTotalNum(), pro.getImgUrl()))
                               .collect(Collectors.toList())
                       );
                   }
               });
           }
        }
        page.setRecords(orderVOList);
        page.setTotal(orderMapper.getHistoryOrderTotalCount(wxOpenid));
        return page;
    }

    public OrderInfoVO getOrderDetail(String orderNo) throws ServiceException {
        OrderInfoVO orderInfoVO = new OrderInfoVO();
        OrderInfo orderInfo = orderMapper.selectById(orderNo);
        List<OrderProducts> orderProducts = orderProductMapper.selectList(new QueryWrapper<OrderProducts>().in("order_no", orderNo));
        if(orderInfo != null) {
            BeanUtils.copyProperties(orderInfo, orderInfoVO);
        }
        if(!CollectionUtils.isEmpty(orderProducts)){
            orderInfoVO.setProductVOList(orderProducts.stream()
                    .map(vo -> new OrderInfoVO.ProductVO(vo.getGoodsDetail(), vo.getGoodsTotalNum(), vo.getTotalPrice(), vo.getImgUrl()))
                    .collect(Collectors.toList())
            );
        }
        return orderInfoVO;
    }

    // 取消订单，用户在未支付的情况下才能取消订单
    @Transactional(rollbackFor = Exception.class)
    public Result<String> cancelOrder(String orderNo) {
        OrderInfo orderInfo = orderMapper.selectById(orderNo);
        if (OrderStatus.ENUM_has_not_pay_money.value.equalsIgnoreCase(orderInfo.getOrderStatus())) {
            if (!StringUtils.isEmpty(orderInfo.getPayjsOrderId())) {
                Map<String, String> payData = new HashMap<>();
                payData.put("mchid", Const.PayJS.mchid);
                payData.put("payjs_order_id", orderInfo.getPayjsOrderId()); // payjs订单号
                payData.put("sign", PayjsUtil.sign(payData));
                HttpUtil.post(Const.PayJS.closeUrl, JSON.toJSONString(payData)); // 不用完全保证微信那边的订单也取消了，2小时自动取消
            }
            orderInfo.setOrderStatus(OrderStatus.ENUM_has_canceled.value);
            orderMapper.updateById(orderInfo);
            return Result.ok("订单已取消");
        }
        return Result.fail("未付款的订单才能取消");
    }

    // 用户确认收货
    @Transactional(rollbackFor = Exception.class)
    public Integer finishedOrder(String orderNo, String wxOpenid) throws ServiceException {
        OrderInfo orderInfo = orderMapper.selectById(orderNo);
        // '请取餐' 或 '已送达' 时用户才能让去确认收货
        if (OrderStatus.ENUM_please_take_meal.value.equalsIgnoreCase(orderInfo.getOrderStatus())
                || OrderStatus.ENUM_has_received.value.equalsIgnoreCase(orderInfo.getOrderStatus()))
            return orderMapper.finishOrder(orderNo, OrderStatus.ENUM_has_completed.value, new Date());

        throw ServiceException.CONST_confirm_receive_failed;
    }

    // 获取正在处理的订单
    public List<OrderInfo> getHandlingOrders(String wxOpenid) throws ServiceException {
        List<String> params = new ArrayList<>();
        params.add(OrderStatus.ENUM_has_completed.value);
        params.add(OrderStatus.ENUM_has_canceled.value);
        params.add(OrderStatus.ENUM_has_refunded.value);
        params.add(OrderStatus.ENUM_on_refunding.value);

        return orderMapper.selectList(new QueryWrapper<OrderInfo>()
                .eq("wx_openid", wxOpenid)
                .notIn("order_status", params)
                .orderByDesc("create_time"));
    }
}
