package com.admin.modules.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.common.entity.app.OrderInfo;
import com.admin.modules.system.entity.dto.StatWeekDataDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 订单
 */
public interface OrderInfoAdminMapper extends BaseMapper<OrderInfo> {

    // 更新订单状态和微信交易号
    @Update("UPDATE order_info SET order_status = #{param3}, " +
            " wx_pay_transaction_id = #{param2}, pay_price=#{param4}, pay_time=#{param5} \n" +
            " WHERE order_no = #{param1}; ")
    void updateOrderStatus(String orderNo, String wxPayTransactionId, String orderStatus, Integer payPrice, String payTime);

    // 通过微信交易号查找微信订单号
    @Select("SELECT order_no FROM order_info where wx_pay_transaction_id is null order by create_time limit #{param1}; ")
    List<String> selectOrderNoByWxPayTransactionId(int limit);


    // 删除没有支付的订单
    @Delete("delete from order_info where order_status = #{param1} or wx_pay_transaction_id is null; ")
    void deleteOrderNotPay(String orderStatus);


    /**
     * 报表数据汇总
     */
    @Select("SELECT\n" +
            "\tDATE_FORMAT( create_time, '%Y%-%m-%d' ) as days,\n" +
            "\tcount(*) as total,\n" +
            "\tsum(case when order_status='制作中' then 1 else 0 end) as doCount ,\n" +
            "\tsum(case when order_status='已送达' then 1 else 0 end) as sendCount,\n" +
            "\tsum(case when order_status='已退款' then 1 else 0 end) as refundCount,\n" +
            "\tsum(case when order_status='已完成' then 1 else 0 end) as finishCount\n" +
            "FROM\n" +
            "\torder_info \n" +
            "WHERE\n" +
            "\tcreate_time > NOW() - INTERVAL 7 DAY \n" +
            "GROUP BY\n" +
            "\tdays;")
    public List<StatWeekDataDTO> statWeekData();

}
