package com.base.web;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * WebComponent相应接口注释
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface WebHandleAnnotation
{
    public String cmdName();

    public String description();

    public boolean state() default false;
}
