package com.common.entity.app.vo;

import com.common.entity.app.OrderInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class OrderInfoVO extends OrderInfo {

    private List<ProductVO> productVOList;

    @Data
    @AllArgsConstructor
    public static class ProductVO{
        private String goodsDetail;
        private Integer goodsTotalNum;
        private Integer totalPrice;
        private String imgUrl;
    }
}
