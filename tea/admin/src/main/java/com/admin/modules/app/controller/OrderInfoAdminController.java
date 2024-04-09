package com.admin.modules.app.controller;

import cn.hutool.core.date.DateUtil;
import com.admin.modules.app.service.OrderInfoAdminService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.common.constant.Const;
import com.common.common.enums.OrderStatus;
import com.common.common.util.PayjsUtil;
import com.common.common.util.http.HttpUtil;
import com.common.entity.app.OrderInfo;
import com.admin.modules.app.mapper.OrderInfoAdminMapper;
import com.common.common.annotation.NeedPermission;
import com.common.common.exception.ServiceException;
import com.common.common.util.result.Result;
import com.admin.modules.system.entity.dto.StatWeekDataDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "系统：订单管理")
@RestController
@RequestMapping("/orderInfoAdmin")
public class OrderInfoAdminController {
    @Resource
    private OrderInfoAdminMapper orderInfoAdminMapper;

    @Resource
    private OrderInfoAdminService orderInfoAdminService;


    @ApiOperation("分页查询")
    @NeedPermission
    @GetMapping("/page")
    public Result<Page<OrderInfo>> getOrderInfoAdminByPage(@RequestParam(defaultValue = "1") int pageNo,
                                                           @RequestParam(defaultValue = "10") int pageSize,
                                                           @RequestParam(required = false) String orderStatus) {
        return Result.ok(orderInfoAdminService.getOrderInfoAdminByPage(pageNo, pageSize, orderStatus));
    }


    @ApiOperation("进入订单的下一个步")
    @NeedPermission
    @PostMapping("/nextStatus/{orderNo}/{currentStatus}")
    public Result<String> toNextOrderStatus(@PathVariable String orderNo, @PathVariable String currentStatus) throws ServiceException {
        return Result.ok(orderInfoAdminService.toNextOrderStatus(orderNo, currentStatus));
    }

    @ApiOperation("商家取消没有付款的订单")
    @NeedPermission
    @PutMapping("/cancelOrderNotPay/{orderNo}")
    public Result cancelOrderNotPay(@PathVariable String orderNo) throws ServiceException {
        return Result.ok(orderInfoAdminService.cancelOrderNotPay(orderNo));
    }

    @ApiOperation("商家取消订单并执行退款")
    @NeedPermission
    @PutMapping("/cancelAndRefund/{orderNo}")
    public Result cancelAndRefund(@PathVariable String orderNo, String reason) throws ServiceException {
        return Result.ok(orderInfoAdminService.cancelAndRefund(orderNo, reason));
    }


    @ApiOperation("报表数据获取")
    @GetMapping("/statData")
    public Result<List<StatWeekDataDTO>> statData(){
        return Result.ok(orderInfoAdminMapper.statWeekData());
    }
}
