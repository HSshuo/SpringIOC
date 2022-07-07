package com.example.hshuo.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author SHshuo
 * @data 2022/7/4--19:44
 *
 * 包扫描注解
 *
 * @Target：注解标记另外的注解用于限制此注解可以应用哪种Java元素类型。type对应的是类、接口；
 * @Retention：元注解，注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在；
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ComponentScan {

    String value();
}
