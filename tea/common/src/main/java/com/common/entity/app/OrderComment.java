package com.common.entity.app;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class OrderComment {

    @TableId
    private Integer id;
    @TableField("order_no")
    private Integer orderNo;
    @TableField("service_num")
    private Integer serviceNum;
    @TableField("order_num")
    private Integer orderNum;
    @TableField("speed_num")
    private Integer speedNum;
    @TableField("product_num")
    private Integer productNum;
    @TableField("enviroment_num")
    private Integer enviromentNum;
    @TableField("remark")
    private String remark;
}
