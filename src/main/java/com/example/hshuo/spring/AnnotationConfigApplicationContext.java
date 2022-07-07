package com.example.hshuo.spring;

import com.example.hshuo.spring.Entry.BeanDefinition;
import com.example.hshuo.spring.annotation.Autowired;
import com.example.hshuo.spring.annotation.Component;
import com.example.hshuo.spring.annotation.ComponentScan;
import com.example.hshuo.spring.annotation.Scope;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SHshuo
 * @data 2022/7/7--8:21
 *
 * 获取bean的实例
 */
public class AnnotationConfigApplicationContext {

    /**
     * 存储bean
     */
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    /**
     * bean的单例池
     */
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    /**
     * 存储BeanPostProcessor
     */
    List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();



    public AnnotationConfigApplicationContext(Class configClass) {
//        1、扫描包下的所有class
        List<Class> list = doScan(configClass);
//        2、注册 BeanDefinition
        registryBeanDefinition(list);
//        3、实例化
        createBean();
    }


    /**
     * 1、通过ComponentScan获取路径下的所有class
     * @param configClass
     * @return
     */
    private List<Class> doScan(Class configClass) {
        List<Class> list = new ArrayList<>();
        String scanPath = "";

//        拿到扫描路径
        if(configClass.isAnnotationPresent(ComponentScan.class)){
            ComponentScan componentScan = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            scanPath = componentScan.value();
        }

//        路径替换
        scanPath = scanPath.replace(".", "/");

//        找到路径下的class文件
        ClassLoader classLoader = AnnotationConfigApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(scanPath);
        File file = new File(resource.getFile());
//        找到目录下的文件
        File[] files = file.listFiles();
        for(File f : files){
//            加载类
            String path = f.getAbsolutePath();
            path = path.substring(path.indexOf("com"), path.indexOf(".class"));
            path = path.replace("\\", ".");
            try {
                Class<?> loadClass = classLoader.loadClass(path);
                list.add(loadClass);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return list;
    }


    /**
     * 2、获取所有实现Component的类，注册到BeanDefinition中
     * @param list
     */
    private void registryBeanDefinition(List<Class> list) {
        for(Class aClass : list){
            BeanDefinition beanDefinition = new BeanDefinition();
            if(aClass.isAnnotationPresent(Component.class)){
                Component component = (Component) aClass.getAnnotation(Component.class);
                String beanName = component.value();

//                存放在BeanDefinition
                beanDefinition.setBeanClass(aClass);
//                判断是否单例
                if(aClass.isAnnotationPresent(Scope.class)){
                    Scope scope = (Scope) aClass.getAnnotation(Scope.class);
                    beanDefinition.setScope(scope.value());
                }else{
                    beanDefinition.setScope("singleton");
                }

//                实现beanPOSTProcessor的类优先于普通的 bean 初始化并存放到一个list中，使用时直接获取执行方法
                if(BeanPostProcessor.class.isAssignableFrom(aClass)) {
                    try {
                        BeanPostProcessor postProcessor = (BeanPostProcessor) aClass.getDeclaredConstructor().newInstance();
                        beanPostProcessors.add(postProcessor);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                beanDefinitionMap.put(beanName, beanDefinition);
            }
        }
    }


    /**
     * 3、实例化bean
     */
    private void createBean() {
        for(Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()){
            docreateBean(entry.getKey(), entry.getValue());
        }
    }

    private Object docreateBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = null;
        try {
            Class beanClass = beanDefinition.getBeanClass();

//            通过反射实例化bean
            bean = beanClass.getDeclaredConstructor().newInstance();
//            4、属性填充
            Field[] fields = beanClass.getDeclaredFields();
            for(Field field : fields){
                if(field.isAnnotationPresent(Autowired.class)){
//                    属性名称
                    String fieldName = field.getName();
                    field.setAccessible(true);
                    field.set(bean, getBean(fieldName));
                }
            }

//            5、*aware接口回调处理
            if(BeanNameAware.class.isAssignableFrom(beanClass)){
                ((BeanNameAware) bean).setBeanName(beanName);
            }

//            6、beanPostProcessors -before方法
            for(BeanPostProcessor beanPostProcessor : beanPostProcessors){
                beanPostProcessor.postProcessorBeforeInitialization(bean, beanName);
            }

//            7、初始化方法 -init-method; InitializingBean
            if(InitializingBean.class.isAssignableFrom(beanClass)){
                ((InitializingBean) bean).afterPropertiesSet();
            }

//            8、beanPostProcessors -after方法
            for(BeanPostProcessor beanPostProcessor : beanPostProcessors){
                beanPostProcessor.postProcessorAfterInitialization(bean, beanName);
            }

//            如果是单例、存入单例池中
            if(beanDefinition.getScope().equals("singleton")){
                singletonObjects.put(beanName, bean);
            }

//            9、注册 Disposable bean - destroy 方法

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return bean;
    }


    /**
     * 判断获取的是原型bean，还是单例bean
     * @param beanName
     * @return
     */
    public Object getBean(String beanName){
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if(beanDefinition.getScope().equals("prototype")){
            return docreateBean(beanName, beanDefinition);
        }else{
            Object bean = singletonObjects.get(beanName);
            if(bean == null){
                bean = docreateBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
            return bean;
        }
    }

}
