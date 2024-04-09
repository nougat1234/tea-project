package com.common.service;

import cn.hutool.json.JSONUtil;
import com.common.entity.common.AppConfig;
import com.common.common.constant.Const;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class AppConfigService {

    @Resource
    private LocalCacheService localCacheService;

    // 获取小程序配置
    public AppConfig getAppConfig()  {
        String str = localCacheService.get(Const.CONST_app_config);
        if (!StringUtils.isEmpty(str))
            return  JSONUtil.toBean(str, AppConfig.class);
        AppConfig appConfig = new AppConfig();
        localCacheService.set(Const.CONST_app_config, JSONUtil.toJsonStr(appConfig));
        return appConfig;
    }
}
