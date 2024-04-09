package com.common.entity.app.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

// 下单参数对象
@Data
public class CreateOrderForm {
    private String addressDetail;
    private Integer totalPrice;
    private Integer verifyNum; // 取单号，一般取手机尾号
    private String takeType;
    private String extraInfo;
    private String userPhone;
    @ApiModelProperty("取餐人")
    private String receiver;
    private String goodsPreview;
    private Integer goodsTotalNum;
    private List<ProductVO> productVOList;

    @Data
    public static class ProductVO{
        private String goodsDetail;
        private Integer goodsTotalNum;
        private Integer totalPrice;
        private String imgUrl;
    }
}
