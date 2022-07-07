package com.example.hshuo.spring;

/**
 * @author SHshuo
 * @data 2022/7/7--8:44
 *
 * 对bean的再加工，可以修改bean的属性、生成动态代理实例等等
 * 拓展：SpringAop的底层处理也是通过实现BeanPostProcessor来执行代理包装的逻辑
 */
public interface BeanPostProcessor {

    Object postProcessorBeforeInitialization(Object bean, String beanName);

    Object postProcessorAfterInitialization(Object bean, String beanName);

}
