package com.admin.common.mvc;

import com.admin.common.interceptor.SystemApiInterceptor;
import com.common.common.config.property.IOProperty;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer, ErrorPageRegistrar {
    @Resource
    private SystemApiInterceptor systemApiInterceptor; // 管理后台拦截器
    @Resource
    private IOProperty ioProperty;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(this.systemApiInterceptor).addPathPatterns(("/**"));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态图片的位置 // eg: windows: file:D/Desktop/image/  Linux或Mac系统: file:/image/
        registry.addResourceHandler("/static/image/**")
                .addResourceLocations("file:" + ioProperty.getImageFileRootPath());
    }

    // 处理404页面，或者将Vue页面打包放到静态资源下找不到页面的问题
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404"));
    }
}
