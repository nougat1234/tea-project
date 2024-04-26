package com.app.moudles.controller;

import com.app.common.util.session.SessionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.app.moudles.mapper.OrderMapper;
import com.app.moudles.service.OrderServiceImpl;
import com.common.common.annotation.NeedLogin;
import com.common.common.exception.ServiceException;
import com.common.common.util.result.Result;
import com.common.entity.app.OrderComment;
import com.common.entity.app.OrderInfo;
import com.common.entity.app.form.CreateOrderForm;
import com.common.entity.app.vo.HistoryOrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "订单服务")
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderServiceImpl orderService;

    @Resource
    private OrderMapper orderMapper;

    @ApiModelProperty("下订单")
    @NeedLogin
    @PostMapping
    public Result<String> createOrder(@RequestBody CreateOrderForm orderParams, HttpServletRequest request) throws ServiceException {
        return orderService.createOrder(orderParams, SessionUtil.getCurrentWxOpenidFromRequest(request));
    }

    @ApiOperation("取消订单")
    @NeedLogin
    @DeleteMapping("/cancel/{orderNo}")
    public Result<String> cancelOrder(@ApiParam("订单号") @PathVariable String orderNo) {
        return orderService.cancelOrder(orderNo);
    }

    @ApiOperation("确认收货")
    @NeedLogin
    @PutMapping("/confirmReceive/{orderNo}")
    public Result confirm(@ApiParam("订单号") @PathVariable String orderNo, HttpServletRequest request) throws ServiceException {
        return Result.ok(orderService.finishedOrder(orderNo, SessionUtil.getCurrentWxOpenidFromRequest(request)));
    }

    @ApiOperation("分页获取历史订单")
    @NeedLogin
    @GetMapping("/history/page/{pageNo}/{pageSize}")
    public Result<Page<HistoryOrderVO>> getHistoryOrderByPage(@PathVariable Integer pageNo,
                                                              @PathVariable Integer pageSize,
                                                              HttpServletRequest request) throws ServiceException {
        return Result.ok(orderService.getHistoryOrderByPage(pageNo, pageSize, SessionUtil.getCurrentWxOpenidFromRequest(request)));
    }

    @ApiOperation("订单详情信息")
    @GetMapping("/detail/{orderNo}")
    public Result<OrderInfo> getOrderDetail(@PathVariable String orderNo) throws ServiceException {
        return Result.ok(orderService.getOrderDetail(orderNo));
    }

    @ApiOperation("获取正在处理的订单列表")
    @NeedLogin
    @GetMapping("/notCompleted")
    public Result<List<OrderInfo>> getHandlingOrders(HttpServletRequest request) throws ServiceException {
        return Result.ok(orderService.getHandlingOrders(SessionUtil.getCurrentWxOpenidFromRequest(request)));
    }

    @ApiModelProperty("订单建议添加")
    @NeedLogin
    @PostMapping("/createOrderComment")
    public Result createOrderComment(@RequestBody OrderComment orderParams, HttpServletRequest request) throws ServiceException {
        orderService.createOrderComment(orderParams);
        return  Result.ok();
    }
}
