package com.common.entity.app.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("历史订单列表信息")
public class HistoryOrderVO {
    private String orderNo;
    private String orderStatus;
    @ApiModelProperty("商品大致信息，eg: 奶茶*2")
    private String goodsPreview;
    private Integer goodsTotalNum;
    private String createTime;
    private Integer payPrice;
    private List<ProductVO> productVOList;

    /**
     * 商品数据
     */
    @Data
    @AllArgsConstructor
    public static class ProductVO{
        private String goodsDetail;
        private Integer goodsTotalNum;
        private String imgUrl;
    }
}
