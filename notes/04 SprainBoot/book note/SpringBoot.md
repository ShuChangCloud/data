# SpringBoot Notes

### 一 常见注解

**@ConfigurationProperties** 告诉springboot将配置文件里面值取出来关联到该类的属性上,前提是该类必须交给spring容器管理,所以同时需要**@Component** 注解配合使用

**prefix的值加上pojo类中属性名组成全限定属性名到@PropertySource指定的文件中查找**

* ```java
  //导入容器或配置文件中的属性到当前类中,常常和@PropertySource一起使用
  @ConfigurationProperties(prefix = "author")  
  @PropertySource("classpath:author.properties") 
  public class Author {
      private String name;
      private String sex;
      private  int age;
  }
  ```

* ```java
  @PropertySource("classpath:author.properties") //加载指定配置文件到容器
  ```

* ```java
  @ImportResource(locations = "classpath:beans.xml")		//导入指定配置文件或类到当前类或配置文件中
  ```

> **注意:**  把@PropertySource和@ImportResource写反会抛出XML解析异常的错误        



@ImportResource 导入xml装配的bean到spring容器

@PropertySource 导入外部配置

### 二 自动配置 AutoConfiguration

```java
@Configuration
@EnableConfigurationProperties(HttpProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(CharacterEncodingFilter.class)
@ConditionalOnProperty(prefix = "spring.http.encoding", value = "enabled",
		matchIfMissing = true)
public class HttpEncodingAutoConfiguration {}
```



#### 1. 为什么有了自动配置类还需要配置文件类(HttpEncodingAutoConfiguration.class和HttpProperties.class)

容器导入的是**自动配置类**,而不是**配置文件类**,自动配置类依赖配置文件类,而配置文件类依赖配置文件中的值注入.

### 三 日志框架

在代码中，并不会出现具体日志框架的api。程序根据classpath中的桥接器类型，和日志框架类型，判断出**logger.info()**应该以什么框架输出！注意了，如果classpath中不小心引了两个桥接器，那会直接报错的！

```java
@see https://zhuanlan.zhihu.com/p/52272417
import org.slf4j.Logger;		//导入的包中不是具体的日志框架实现,而是抽象日志门面slf4j
import org.slf4j.LoggerFactory;
//省略
Logger logger = LoggerFactory.getLogger(Test.class);
// 省略
logger.info("info");
```

因此，在阿里的开发手册上才有这么一条

强制：应用中不可直接使用日志系统（log4j、logback）中的 API ，而应依赖使用日志框架 SLF4J 中的 API 。使用门面模式的日志框架，有利于维护和各个类的日志处理方式的统一。



### 四 静态资源的访问

1. springboot项目是依赖于maven的.

2. 用spring向导创建springboot项目时,src\main\目录下有java和resources这两个目录,这两个目录都是类路径下的目录,再编译完后会合并到classes目录下.![root](G:\data\notes\04SprainBoot\root.png)

3. 当**外部映射路径(浏览器请求资源的地址)** 没有控制器处理时,springboot会按照静态资源匹配规则去寻找对应的资源.

   匹配规则如下

   ```java
   "classpath:/META‐INF/resources/",
   "classpath:/resources/",
   "classpath:/static/",
   "classpath:/public/"
   "/"：当前项目的根路径   //springboot 2.x以上已经不行了
   ```

   ```java
   private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
   	"classpath:/META-INF/resources/", 
       "classpath:/resources/",
   	"classpath:/static/", 
       "classpath:/public/" };  //此源码来自springboot2.x
   ```

   

   4.thymeleaf表达式中的[ ]是 属性bean/Map/List/数组的性质


