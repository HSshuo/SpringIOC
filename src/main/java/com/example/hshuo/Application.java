package com.example.hshuo;

import com.example.hshuo.Service.UserService;
import com.example.hshuo.config.MyConfig;
import com.example.hshuo.spring.AnnotationConfigApplicationContext;

/**
 * @author SHshuo
 * @data 2022/7/4--19:40
 */
public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        UserService userService = (UserService) context.getBean("userService");
        userService.hello();
    }
}
