package com.common.common.util.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.common.common.util.http.HttpUtil;
import com.common.common.constant.Const;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WeixinUtil {
    // 根据code获取微信openid
    public static String getWeiXinOpenid(String code) {
        String urlStr = "https://api.weixin.qq.com/sns/jscode2session?appid=" + "wx1e78bd40d9572316"
                + "&secret=" + "4035cffba13591415e351437909581ed"
                + "&grant_type=authorization_code&js_code=" + code;
        System.out.println(urlStr);

        String body = HttpUtil.get(urlStr);
        JSONObject result = JSON.parseObject(body);
        Integer errcode = result.getInteger("errcode");
        if (errcode == null || errcode.equals(0))
            return result.getString("openid");
        else {
            log.error("[获取openid失败] 回复报文:{}", body);
            return null;
        }
    }
}
