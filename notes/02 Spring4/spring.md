# Spring





### 一  AOP

- 基于schema-base实现的aop，无论是什么类型的通知，都必须实现接口     
- 基于aspectj的方式，对通知类的要求没有那么严格，也就是说，在一个切面类中可以写任何的方法，而且不必继承任何接口<br>但是aspect对配置文件的要求非常高，必须指定通知在哪个切面类中，还必须指定是类中的哪个方法
- 出了异常就不会执行后置通知。
- aop一般应用于service层中，service中的方法出现异常不应该处理，必须抛出，否则spring aop就无调用异常通知，因为异常已经被service中的方法try catch过了   
- aspectj中的after与after-returning区别： 
   ​     
   //无论是否发生异常 这个方法都会被执行
   	public void  after(){
   	    System.out.println("执行后置通知");
   	}
   	
   	//发生异常时，该方法不会执行
   	public void afterReturning(){``
   	    System.out.println("执行afterRunning");
   	}

- JDK动态代理也是基于接口的，即只能对实际对象所实现的功能接口中的方法进行代理。

**注意：使用注解来配置切面只适合个别方法，当大面积的方法都需要被代理时，切面类中的方法上的注解就无法复用了**


> 自动注入（自动装配）和依赖注入      

- 自动装配是反射机制实现的。
- 以来注入式通过setter或构造器注入的



###二  关于依赖注入(装配与注入)

#### 2.1 装配方式

* 自动化装配
* JavaConfig
* XML装配

####2.2注意事项

* P域和C域无法装配集和 但是导入util命名空间.就可以使用util-list功能装配集合了.

用xml装配集合

```xml
<bean class="com.xmcc.pojo.Apple">
<construct-arg>
  <list>
  	<value>zhangsan</value>
    <value>lsi</value>
    <value>wangwu</value>
  </list>
</construct-arg>
</bean>
```

同理集合中也可以是bean,但是此时把**\<value>**要换成**\<ref bean="apple">**



* 构造器注入实现强制依赖,setter注入实现可选依赖.

### 三  高级装备

#### 3.1 环境与profile

可以根据不同的环境创建不同的profile

spring3.1以后,@Profile注解可以使用在方法上了,同时和@Bean一起在配置类中使用

```java
@Configuration
public class DataSourceConfig{
  
  @Bean
  @Profile("dev")
  public DataSource jndiDatasource(){
    ....
      return  datasource;
  }
}
```

#### 

* 在bean上使用了**@Profile**注解后,对应的profile被激活时,bean才被创建

* 没有使用**@Profile**的bean无论何时都会被创建.

* 同样可以在XML中使用profile环境

* 在XML中配置多个profile时,beans标签中可以嵌套beans标签

  ```xml
  <beans>
  	<beans profile="dev">
        ...
      </beans>
   	<beans profile="qa">
      ....  
      </beans>
  </beans>
  ```

  ​

####3.2条件化的bean