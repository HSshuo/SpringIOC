package com.example.hshuo.spring.Entry;

/**
 * @author SHshuo
 * @data 2022/7/7--8:31
 *
 * bean的实体类
 */
public class BeanDefinition {
    private String scope;
    private Class beanClass;

    public String getScope() {return scope;}
    public void setScope(String scope) {this.scope = scope;}

    public Class getBeanClass() {return beanClass;}
    public void setBeanClass(Class beanClass) {this.beanClass = beanClass;}
}
