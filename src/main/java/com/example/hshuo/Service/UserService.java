package com.example.hshuo.Service;

import com.example.hshuo.spring.BeanNameAware;
import com.example.hshuo.spring.InitializingBean;
import com.example.hshuo.spring.annotation.Autowired;
import com.example.hshuo.spring.annotation.Component;

/**
 * @author SHshuo
 * @data 2022/7/5--9:08
 */

@Component("userService")
public class UserService implements BeanNameAware, InitializingBean {

    @Autowired
    private OrderService orderService;

    public void hello(){
        System.out.println("打印：Hello Word、" + orderService);
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("set bean name : " + name);
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("init");
    }
}
