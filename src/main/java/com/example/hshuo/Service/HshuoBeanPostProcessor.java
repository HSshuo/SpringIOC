package com.example.hshuo.Service;

import com.example.hshuo.spring.BeanPostProcessor;
import com.example.hshuo.spring.annotation.Component;

/**
 * @author SHshuo
 * @data 2022/7/7--10:20
 */
@Component("hshuoBeanPostProcessor")
public class HshuoBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessorBeforeInitialization(Object bean, String beanName) {
        System.out.println("before init");
        return bean;
    }

    @Override
    public Object postProcessorAfterInitialization(Object bean, String beanName) {
        System.out.println("after init");
        return bean;
    }
}
