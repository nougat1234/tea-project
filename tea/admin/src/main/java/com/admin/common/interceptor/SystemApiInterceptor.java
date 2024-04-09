package com.admin.common.interceptor;

import com.common.common.annotation.NeedPermission;
import com.common.common.constant.Const;
import com.common.common.exception.ServiceException;
import com.common.common.util.jwt.JWTUtil;
import com.admin.common.util.session.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 系统后台api拦截器
 */
@Slf4j
@Component
public class SystemApiInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        NeedPermission needPermission = handler instanceof HandlerMethod ? ((HandlerMethod) handler).getMethod().getAnnotation(NeedPermission.class) : null;
        if ( needPermission != null) {
            String token = SessionUtil.getToken(request);
            if (StringUtils.isEmpty(token)) {
                log.error("未携带token，拦截，请求路径[{}][{}]", request.getMethod(), request.getServletPath());
                return false;
            }

            // 检测token的有效性
            if (!JWTUtil.verify(token)){
                log.error("token无效或已经过期[{}]",token);
                throw ServiceException.CONST_token_is_not_validate;
            }
            Integer sysUserId = JWTUtil.getSysUserId(token);
            if (StringUtils.isEmpty(token) || !SessionUtil.checkSysUserLogin(sysUserId)) {
                log.error("[后台] 拦截未登录 请求路径[{}]", request.getServletPath());
                throw ServiceException.CONST_user_not_login;
            }
            // 将认证后的sysUserId放到request的属性里
            request.setAttribute(Const.CONST_sys_user_id, sysUserId);
        }
        return super.preHandle(request, response, handler);
    }

}
