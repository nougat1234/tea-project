package com.common.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("订单商品")
@Data
public class OrderProducts {
    @TableId(type = IdType.INPUT)
    private Integer Id;
    @TableField("order_no")
    private String orderNo;
    @TableField("goods_detail")
    private String goodsDetail;
    @TableField("goods_total_num")
    private Integer goodsTotalNum;
    @TableField("total_price")
    private Integer totalPrice;
    @TableField("img_url")
    private String imgUrl;
}