# Spring





### 一  AOP

- 基于schema-base实现的aop，无论是什么类型的通知，都必须实现接口     

- 基于aspectj的方式，对通知类的要求没有那么严格，也就是说，在一个切面类中可以写任何的方法，而且不必继承任何接口<br>但是aspect对配置文件的要求非常高，必须指定通知在哪个切面类中，还必须指定是类中的哪个方法

- 出了异常就不会执行后置通知。

- aop一般应用于service层中，service中的方法出现异常不应该处理，必须抛出，否则spring aop就无调用异常通知，因为异常已经被service中的方法try catch过了   

- aspectj中的after与after-returning区别： 
   
   ```java
   //无论是否发生异常 这个方法都会被执行
   	public void  after(){
   	    System.out.println("执行后置通知");
   	}
   ```
   
   ```
   //发生异常时，该方法不会执行
public void afterReturning(){``
       System.out.println("执行afterRunning");
   }
   ```
   
- JDK动态代理也是基于接口的，即只能对实际对象所实现的功能接口中的方法进行代理。

**注意：使用注解来配置切面只适合个别方法，当大面积的方法都需要被代理时，切面类中的方法上的注解就无法复用了**


> 自动注入（自动装配）和依赖注入      

- 自动装配是反射机制实现的。
- 以来注入式通过setter或构造器注入的

### 二  关于依赖注入(装配与注入)

#### 2.1 装配方式

* 自动化装配
* JavaConfig
* XML装配

#### 2.2注意事项

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

### 三  高级装配

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


#### 3.2条件化的bean

* **@Conditional**注解可以用在**@bean**注解上，就表示满足条件才创建bean，否则忽略这个bean。

* **@Primary**可以与**@Conditiona**注解一起使用或者与**@Bean**一起使用，当自动装配存在歧义性的时候可以解决此冲突，@Primary表示将bean声明为首选的（首选bean）

  ```java
  @Component
  @Primary		//有多个bean同时满足了fruit
  public class Origin extends Fruit {
  ....
  }
  ```

若使用xml配置的话，则直接在bean后面加上属性primary=true即可。如果一个类型的bean（fruit）有多个@primary注解同时存在，那也会有歧义问题，这也就没有任何意义了。

* 使用面向特性的限定符注解**@Qualifier**指定装配具体的bean

  ```java
  @AutoWired
  @Qualifier("origin")
  public void setFruit(){}
  ```

  但是，当多个bean具有相同的特性，此时在按照特性注入也会产生歧义，但是同一个bean可以同时添加多个Qualifier注解，来解决此问题。

  在使用的时候：

  ```java
  @AutoWired	//这个bean表示是甜的origin
  @Qualifier("origin")		//多个相同注解在一个类或方法需要java8以上的版本才能支持，但恰码头是此注解上加了@repeatable 所以次数也是不可取的
  @Qualifier("dessert")
  public void setFruit(){}
  ```

  也可以使用**自定义注解**来替代**@Qualifier注解** ,如下自定义了一个@Dessert注解来表示bean的特性

  ```java
  @Qualifier("dessert")	//这里实际上还是使用了@Qualifier注解
  public @interface Dessert {}
  ```

#### 3.3 bean的作用域

##### 3.3.1bean的作用域：

* single
* prototype
* session
* request

后面两个似乎用处比较小，但是在有的情况下不可替代，如购物车作用域可以是session。如果是single那么所有用户的购物都放在一个购物车里，这不合理。如果是prototype，当一个用户在此处生成一个购物车，在点击别的页面买的商品又生成一个购物车，这似乎也不合理。

**作用域冲突**

```java
@Service
public class StoreService{
    @Autowired
    private ShoppingCar shoppingCar;
}
```

在一个web应用中storeservice是单例的，而购物车shoppingcar是session级别的。此时需要解决作用域冲突的问题。shoppingCar一开是空的直到某个用户进入系统后才生成一个ShoppingCar的实例（session）。另外系统中有许多个shoppingcar的实例，我们并不能让某个特定的shoppingcar注入到storeservice中。

**spring是如何解决的：**

spring并不会将实际的shoppingcar注入到service实例中，spring会注入一个到shoppingcar bean的代理。

#### 3.4 Environment类

Environment提供了与属性和环境有关的内容，以下是重要的方法。

*  T env.getProperty(string key)
* String[] getActiveProfile()
* boolean acceptsProfile(String... profiles)  如果env支持指定的profile的话，返回true。

#### 3.5 运行时注入 SPEL

SPEL与属性占位符的区别

SPEL: 形如#{ ...}

属性占位符：形如 ${ ...}

##### 3.5.1 **使用SPEL可以做什么：**

* 调用方法和访问对象的属性
* 通过bean的id来引用bean
* 对值进行算术运算
* 正则表达式匹配
* 集合操作



##### 3.5.2 常用实例

```java
#{fruit.color} //获取水果id为fruit的颜色属性
#{fruit.getPrice()} //调用对象的方法
#{fruit.color.toUpperCase（）}  //对获取的值进行方法调用的追加操作
#{fruit.color?.toUpperCase（）}  // ?.能在访问右边的内容之前对该操作符左边的内容判空，如果为空则右边就不会被访问到，此时表达式的值就是null
#{T(java.lang.Math).PI}  //T()的运算结果是一个Class对象，T()的真正威力在于可以调用类的静态方法和静态属性，括号中是全限定类名。
#{2 * T(java.lang.Math).PI *circle.radius} //使用SPEL进行表达式运算
#{'math' == 'since'} //使用SPEL做逻辑运算，此处可替换为 #{'math'  eq 'since' },返回值是boolan
#{100>90?true:false} //使用SPEL做三元运算
#{fruit ?: 'orange'}  // ?:判断是否为null值，此处若fruit为null，则返回'orange'
#{people.emaul  mathches '[a-zA-Z0-9]'}  //SPEL表达式正则匹配


/*--------以下是与集合操作有关的------------*/
#{store.fruits[4].price} //商店属性的fruits集合中第五个水果的价格
#{store.fruits[T(java.lang.Math).random()*store.fruits.size].price}  //同上，从fruits集合中随机取出一个水果，查看价格
#{'this is a test'[3]}  //获取字符串的第四个字符
#{store.fruits.?[price>10]} //查询fruits集合中价格大于10的水果   .?提供了过滤查询集合的效果
#{store.fruits.![title]}  //映射生成一个新的集合，这个集合是每个fruit的价格  .!映射结果集

```

