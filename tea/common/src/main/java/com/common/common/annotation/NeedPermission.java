package com.common.common.annotation;

import java.lang.annotation.*;

// 标记接口方法, 需要登录
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NeedPermission {
}
