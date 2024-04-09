package com.common.entity.common;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("小程序相关配置")
@Data
public class AppConfig {

    private String shopName = "简单奶茶";

    @ApiModelProperty("包装费(单位: 分)")
    private int packingPrice = 0;

    @ApiModelProperty("外卖配送费(单位: 分)")
    private int sendingPrice = 0;

    @ApiModelProperty("起送需要的最低价格")
    private int sendingNeedLeastPrice = 800;

    @ApiModelProperty("营业开始时间, 整点(不要大于结束时间)")
    private int businessStartTime = 0;

    @ApiModelProperty("营业结束时间, 整点(不要小于开始时间)")
    private int businessEndTime = 24;

    @ApiModelProperty("联系电话1")
    private String lianxidianhua1 = "17058179046";

    @ApiModelProperty("联系电话2")
    private String lianxidianhua2 = "17058179046";

    @ApiModelProperty("联系QQ")
    private String lianxiQQ = "2414402332";

    @ApiModelProperty("店铺开放状态, 商家休息|营业中")
    private Boolean shopStatus = true;

    @ApiModelProperty("菜单上面的公告通知")
    private String shopNotice = "12点到22点可以送餐~";

    @ApiModelProperty("允许测试号登录")
    private Boolean testUserLoginEnabled = true;
}
