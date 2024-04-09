package com.admin.common.util.session;

import cn.hutool.json.JSONUtil;
import com.admin.modules.system.entity.dto.SysUserDTO;
import com.alibaba.fastjson.JSON;
import com.common.common.constant.Const;
import com.common.common.exception.ServiceException;
import com.common.common.util.GeneratorUtil;
import com.common.common.util.jwt.JWTUtil;
import com.common.common.util.spring.SpringContextUtil;
import com.common.service.LocalCacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

// 后台session会话
@Slf4j
public class SessionUtil {
    private static final LocalCacheService LOCAL_CACHE_SERVICE = SpringContextUtil.getBeanByClass(LocalCacheService.class);

    public static boolean logout(HttpServletRequest request) {
        LOCAL_CACHE_SERVICE.del(Const.CONST_sys_user_session_map+request.getAttribute(Const.CONST_sys_user_id));
        return true;
    }

    // 检查管理员是否登录
    public static boolean checkSysUserLogin(Integer sysUserId) {
        return LOCAL_CACHE_SERVICE.get(Const.CONST_sys_user_session_map+sysUserId.toString()) != null;
    }

    /**
     * 添加会话缓存  缓存到redis里，session Map<sysUserId, SysUserDTO>
     */
    public static void setSysUserSession(SysUserDTO sysUserDTO) {
        sysUserDTO.setToken(JWTUtil.generateTokenWithUserId(sysUserDTO.getId(), GeneratorUtil.generateExpireTime(Const.CONST_half_month)));
        LOCAL_CACHE_SERVICE.set(Const.CONST_sys_user_session_map+sysUserDTO.getId().toString(), JSONUtil.toJsonStr(sysUserDTO));
    }

    // 更新会话缓存
    public static void updateSysUserSession(SysUserDTO sysUserDTO) {
        LOCAL_CACHE_SERVICE.set(Const.CONST_sys_user_session_map+sysUserDTO.getId().toString(), JSONUtil.toJsonStr(sysUserDTO));
    }

    // 当前用户的id
    public static Integer getCurrentSysUserId(HttpServletRequest request) {
        return Integer.valueOf(request.getAttribute(Const.CONST_sys_user_id).toString());
    }

    // 当前登录的管理员信息
    public static SysUserDTO getCurrentSysUserDTO(Integer sysUserId) {
        Object o = LOCAL_CACHE_SERVICE.get(Const.CONST_sys_user_session_map+sysUserId.toString());
        if (o != null)
            return (SysUserDTO) o;
        return null;
    }

    // 当前登录的管理员信息
    public static SysUserDTO getCurrentSysUserDTO(HttpServletRequest request) {
        String o = LOCAL_CACHE_SERVICE.get(Const.CONST_sys_user_session_map+getCurrentSysUserId(request).toString());
        if (o != null)
            return JSON.parseObject(o,SysUserDTO.class);
        return null;
    }

    // 找请求里携带的token
    public static String getToken(HttpServletRequest request) throws ServiceException {
        String token = request.getParameter(Const.CONST_token); // 从url后面的参数里找
        if (StringUtils.isEmpty(token)) // 从header里面找
            token = request.getHeader(Const.CONST_token);
        if (token == null)
            throw ServiceException.CONST_user_not_login;
        return token;
    }


}
