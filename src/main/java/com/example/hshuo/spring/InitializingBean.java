package com.example.hshuo.spring;

/**
 * @author SHshuo
 * @data 2022/7/7--8:35
 *
 * 初始化bean两种方式：
 * 1、实现InitializingBean接口里面的afterPropertiesSet方法
 * 2、执行init-method方法
 */
public interface InitializingBean {

    void afterPropertiesSet();
}
