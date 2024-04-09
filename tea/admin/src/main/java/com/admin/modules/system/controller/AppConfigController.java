package com.admin.modules.system.controller;

import com.common.common.annotation.NeedPermission;
import com.alibaba.fastjson.JSON;
import com.common.common.constant.Const;
import com.common.common.exception.ServiceException;
import com.common.common.util.result.Result;
import com.common.entity.common.AppConfig;
import com.common.service.AppConfigService;
import com.common.service.LocalCacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "系统：小程序配置")
@RestController
@RequestMapping("/config")
public class AppConfigController {

    @Resource
    private AppConfigService appConfigService;
    @Resource
    private LocalCacheService localCacheService;

    @ApiOperation("获取小程序的所有配置")
    @GetMapping
    public Result<AppConfig> getAppConfig() throws ServiceException {
        return Result.ok(appConfigService.getAppConfig());
    }

    @ApiOperation("修改配置信息")
    @NeedPermission
    @PutMapping
    public Result<AppConfig> updateAppConfig(@RequestBody AppConfig updateConfig) throws ServiceException {
        if (updateConfig.getBusinessStartTime() < 0 || updateConfig.getBusinessStartTime() > 24
                || updateConfig.getBusinessEndTime() < 0 || updateConfig.getBusinessEndTime() > 24)
            throw new ServiceException("营业开始时间或结束时间不合法, 只能为0到24的整数");
        if (updateConfig.getBusinessStartTime() >= updateConfig.getBusinessEndTime())
            throw new ServiceException("请将营业开始时间设置在营业结束时间之前, [开始时间, 结束时间)");

        localCacheService.set(Const.CONST_app_config, JSON.toJSONString(updateConfig)); // 刷新到缓存里
        return getAppConfig();
    }

}
