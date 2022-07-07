## SpringIOC

1. bean的扫描，通过@ComponentScan的属性，获取此路径下的所有类，存储到list中；
2. BeanDefinition的注册，通过遍历list，将声明@Component的类，存储到map中；
3. bean的实例化： 通过遍历map，将bean依次执行

- 属性填充
- 设置BeanName
- 调用BeanPostProcessor的postProcessorBeforeInitialization方法
- 执行InitializingBean的afterPropertiesSet方法 或者 init-method
- 调用BeanPostProcessor的PostProcessorAfterInitialization方法
- 执行destory-method销毁