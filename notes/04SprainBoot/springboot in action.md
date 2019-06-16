# Spring Boot  In  Action 



> ## Spring Boot  的核心是起步依赖和自动配置



### 开发运行第一个应用程序



#### 测试spring boot应用程序

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class MallTinyApplicationTests {
    @Test
    public void contextLoads() {
    }
}
```

这里还有一个简单的测试方法，即 contextLoads() 。实际上它就是个空方法。先测试运行这个空方法,如果测试通过,说明前期的配置和部署都是没有问题的.



**配置应用程序属性**

> Initializr为你生成的application.properties文件是一个空文件。实际上，这个文件完全是可选的，你大可以删掉它，这不会对应用程序有任何影响，但留着也没什么问题。
>
> application.properties文件可以很方便地帮你细粒度地调整Spring Boot的自动配置。
>
> 注意的是，你完全不用告诉Spring Boot为你加载 application.properties ，只要它存
> 在就会被加载，Spring和应用程序代码都能获取其中的属性。



#### 指定基于功能的依赖

假设你想开发一个spring的web应用程序,那么首先:

你会向项目里添加哪些依赖呢？要用Spring MVC的话，你需要哪个Spring依赖？你还记得
Thymeleaf的Group和Artifact ID吗？你应该用哪个版本的Spring Data JPA呢？它们放在一起兼容
吗？

但你是怎么知道的？你怎么保证你选的这些版本能相
互兼容？也许可以，但构建并运行应用程序之前你是不知道的。再说了，你怎么知道这个列表是
完整的？在一行代码都没写的情况下，你离开始构建还有很长的路要走。

**如果我们只在构建文件里指定功能，让构建过程自己搞明白我们要什么东西，岂不是更简单？这正是Spring Boot起步依赖的功能。**



#### 覆盖起步依赖引入的传递依赖

但是，即使经过了Spring Boot团队的测试，起步依赖里所选的库仍有问题该怎么办？如何覆盖起步依赖呢？

说到底，起步依赖和你项目里的其他依赖没什么区别。也就是说，你可以通过构建工具中的功能，选择性地覆盖它们引入的传递依赖的版本号，排除传递依赖，当然还可以为那些Spring Boot起步依赖没有涵盖的库指定依赖。

比方说你的起步依赖里面有jsonAPI Jackson的库,但是你不想使用它,可以用<exclusions>标签排除.

```xml
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-web</artifactId>
<exclusions>
        <exclusion>
        <groupId>com.fasterxml.jackson.core</groupId>
        </exclusion>
</exclusions>
</dependency>
```

另一方面，也许项目需要Jackson，但你需要用另一个版本的Jackson来进行构建，而不是Web起步依赖里的那个。假设Web起步依赖引用了Jackson 2.3.4，但你需要使用2.4.3。在Maven里，你可以直接在pom.xml中表达诉求，就像这样：

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.4.3</version>
</dependency>
```

Maven总是会用最近的依赖，也就是说，你在项目的构建说明文件里增加的这个依赖，会**覆盖**传递依赖引入的另一个依赖。



不管什么情况，在覆盖Spring Boot起步依赖引入的传递依赖时都要多加小心。虽然不同的版本放在一起也许没什么问题，但你要知道，起步依赖中各个依赖版本之间的兼容性都经过了精心的测试。应该只在特殊的情况下覆盖这些传递依赖（比如新版本修复了一个bug）。

**查看maven依赖树结构**

```cmd
mvn dependency:tree
```

#### 使用自动配置

简而言之，Spring Boot的自动配置是一个运行时（更准确地说，是应用程序启动时）的过程，考虑了众多因素，才决定Spring配置应该用哪个，不该用哪个。举几个例子，下面这些情况都是Spring Boot的自动配置要考虑的:

* Spring的 JdbcTemplate 是不是在Classpath里？如果是，并且有 DataSource 的Bean，则自动配置一个 JdbcTemplate 的Bean。
* Thymeleaf是不是在Classpath里？如果是，则配置Thymeleaf的模板解析器、视图解析器以及模板引擎。
*  Spring Security是不是在Classpath里？如果是，则进行一个非常基本的Web安全设置。每当应用程序启动的时候，Spring Boot的自动配置都要做将近200个这样的决定，涵盖安全、集成、持久化、Web开发等诸多方面。所有这些自动配置就是为了尽量不让你自己写配置。



**条件化的配置**

在Spring里可以很方便地编写你自己的条件，你所要做的就是实现 Condition 接口，覆盖它的 matches() 方法。

Spring 4.0引入的新特性。条件化配置允许配置存在于应用程序中，但在满足某些特定条件之前都忽略这个配置。

举例来说，下面这个简单的条件类只有在Classpath里存在 JdbcTemplate 时才会生效：

```java
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
public class JdbcTemplateCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata){
        try {
        context.getClassLoader().loadClass(
        "org.springframework.jdbc.core.JdbcTemplate");
       		 return true;
        } catch (Exception e) {
      	 	 return false;
        }
    }
}
```

当你用Java来声明Bean的时候，可以使用这个自定义条件类：

```java
@Conditional(JdbcTemplateCondition.class)
public MyService myService() {
	...
}
```

在这个例子里，只有当 JdbcTemplateCondition 类的条件成立时才会创建 MyService 这个Bean。也就是说MyService Bean创建的条件是Classpath里有 JdbcTemplate 。否则，这个Bean的声明就会被忽略掉。



表2-1列出了Spring Boot提供的条件化注解。

![](assets/condionbena-1560606398267.png)

随便选中一个注解,查看源码.比如@ConditionalOnBean

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional({OnBeanCondition.class})	//这里才是Conditional的决策
public @interface ConditionalOnBean {
    Class<?>[] value() default {};

    String[] type() default {};

    Class<? extends Annotation>[] annotation() default {};

    String[] name() default {};

    SearchStrategy search() default SearchStrategy.ALL;

    Class<?>[] parameterizedContainer() default {};
}
```

可以看到这个注解并没有什么特殊之处,只是在已有的@Conditional注解上 已经实现了一个条件加上去而已



#### 小结

通过Spring Boot的起步依赖和自动配置，你可以更加快速、便捷地开发Spring应用程序。起步依赖帮助你专注于应用程序需要的功能类型，而非提供该功能的具体库和版本。与此同时，自动配置把你从样板式的配置中解放了出来。这些配置在没有Spring Boot的Spring应用程序里非常常见。



### 自定义配置



#### 覆盖 Spring Boot 自动配置



##### 掀开自动配置的神秘面纱

部分情况下， @ConditionalOnMissingBean 注解是覆盖自动配置的关键。

Spring Boot的 DataSourceAutoConfiguration 中定义的 JdbcTemplate Bean就是一个非常简单的例子，演示了 @ConditionalOnMissingBean 如何工作：

```java
@Bean
@ConditionalOnMissingBean(JdbcOperations.class)
public JdbcTemplate jdbcTemplate() {
	return new JdbcTemplate(this.dataSource);
}
```

​	jdbcTemplate() 方法上添加了 @Bean 注解，在需要时可以配置出一个 JdbcTemplateBean。但它上面还加了 @ConditionalOnMissingBean 注解，要求当前不存在 JdbcOperations类型（ JdbcTemplate 实现了该接口）的Bean时才生效。如果当前已经有一个 JdbcOperationsBean了，条件即不满足，不会执行 jdbcTemplate() 方法。

什么情况下会存在一个 JdbcOperations Bean呢？Spring Boot的设计是加载应用级配置，随后再考虑自动配置类。因此，如果你已经配置了一个 JdbcTemplate Bean，那么在执行自动配置时就已经存在一个JdbcOperations 类型的Bean了，于是忽略自动配置的 JdbcTemplate Bean。



关于Spring Security，自动配置会考虑几个配置类。在这里讨论每个配置类的细节是不切实际的，但覆盖Spring Boot自动配置的安全配置时，最重要的一个类是 SpringBootWebSecurityConfiguration 。以下是其中的一个代码片段：

```java
@Configuration
@EnableConfigurationProperties
@ConditionalOnClass({ EnableWebSecurity.class })
@ConditionalOnMissingBean(WebSecurityConfiguration.class)
@ConditionalOnWebApplication
public class SpringBootWebSecurityConfiguration {
	...
}
```

如你所见， SpringBootWebSecurityConfiguration 上加了好几个注解。看到 @ConditionalOnClass 注解后，你就应该知道Classpath里必须要有 @EnableWebSecurity 注解。

@ConditionalOnWebApplication 说 明 这 必 须 是 个 Web 应 用 程 序 。

@ConditionalOnMissingBean 注解才是我们的安全配置类代替 SpringBootWebSecurityConfiguration 的关
键所在。

@ConditionalOnMissingBean 注解要求容器中没有 WebSecurityConfiguration 类型的Bean。虽然表面上我们并没有这么一个Bean，但如果你自己定义了一个配置类,并且在上面加上了 @EnableWebSecurity 注解时,比如它可能像下面这样:

```java
@Configuration
@EnableWebSecurity	//注意这里的注解
public class SecurityConfig extends WebSecurityConfigurerAdapter {
@Autowired
private ReaderRepository readerRepository;
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        .antMatchers("/").access("hasRole('READER')")
        .antMatchers("/**").permitAll()
        .and()
        .formLogin()
        .loginPage("/login")
        .failureUrl("/login?error=true");
    }
    @Override
    protected void configure(
    AuthenticationManagerBuilder auth) throws Exception {
    auth
    .userDetailsService(new UserDetailsService() {
    @Override
    public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    return readerRepository.findOne(username);
    }
    });
    }
}
```

这样,我们实际上间接创建了一个 WebSecurityConfiguration Bean。所以在自动配置时，这个Bean就已经存在了， @ConditionalOnMissingBean 条件不成立， SpringBootWebSecurityConfiguration 提供的配置就被跳过了。

为什么呢?我们可以看到@EnableWebSecurity的注解声明

```java
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@Import({ WebSecurityConfiguration.class,	//看这里导入的配置类
		SpringWebMvcImportSelector.class,
		OAuth2ImportSelector.class })
@EnableGlobalAuthentication
@Configuration
public @interface EnableWebSecurity {
	boolean debug() default false;
}
```



**小结:**

我们可以从上面的例子上看到,当我们使用@EnableWebSecurity编写自己的安全配置类时,就会**完全的覆盖掉自动配置**,这实在是一件让人感到无语的事情. 因为我想的是尽可能的微调.

所以在变得非常专业之前,尽量不要全面接springboot的自动配置,应该使用微调的方式,这正是下个小章节要介绍的.



#### 通过属性文件外置配置

在处理应用安全时，你当然会希望完全掌控所有配置。不过，为了微调一些细节，比如改改端口号和日志级别，便放弃自动配置，这是一件让人感到恶心的事。

为了设置数据库URL，是配置一个属性简单，还是完整地声明一个数据源的Bean简单？答案不言自明，不是吗？

事实上，Spring Boot自动配置的Bean提供了300多个用于微调的属性。当你调整设置时，只要在环境变量、Java系统属性、JNDI（Java Naming and Directory Interface）、命令行参数或者属性文件里进行指定就好了。



要了解这些属性，让我们来看个非常简单的例子(以下的配置已经在现在的springboot版本中废弃了,这里只是用一个例子来简单讲解)。

你也许已经注意到了，在命令行里运行阅读列表应用程序时，Spring Boot有一个ascii-art Banner。如果你想禁用这个Banner，可以将spring.main.show-banner 属性设置为 false 。有几种实现方式，其中之一就是在运行应用程序的命令行参数里指定：

```cmd
$ java -jar readinglist-0.0.1-SNAPSHOT.jar --spring.main.show-banner=false
```

另一种方式是创建一个名为application.properties的文件，包含如下内容：

```properties
spring.main.show-banner=false
```

或者，如果你喜欢的话，也可以创建名为application.yml的YAML文件，内容如下：

```yml
spring:
	main:
		show-banner: false
```

还可以将属性设置为环境变量。举例来说，如果你用的是bash或者zsh，可以用 export 命令：

```bash
$ export spring_main_show_banner=false
```



实际上，Spring Boot应用程序有多种设置途径。Spring Boot能从多种属性源获得属性，包括
如下几处。
(1) 命令行参数
(2)  java:comp/env 里的JNDI属性
(3) JVM系统属性
(4) 操作系统环境变量

(5) 随机生成的带 random.* 前缀的属性（在设置其他属性时，可以引用它们，比如 ${random.long} ）
(6) 应用程序以外的application.properties或者appliaction.yml文件
(7) 打包在应用程序内的application.properties或者appliaction.yml文件
(8) 通过 @PropertySource 标注的属性源
(9) 默认属性
这个列表按照优先级排序，也就是说，任何在高优先级属性源里设置的属性都会覆盖低优先级的相同属性。例如，命令行参数会覆盖其他属性源里的属性。



application.properties和application.yml文件能放在以下四个位置。

* 外置，在相对于应用程序运行目录的/config子目录里。
*  外置，在应用程序运行的目录里。
* 内置，在config包内。
* 内置，在Classpath根目录。



同样，这个列表按照优先级排序。也就是说，/config子目录里的application.properties会覆盖应用程序Classpath里的application.properties中的相同属性。

此外，如果你在同一优先级位置同时有application.properties和application.yml，那么application.yml里的属性会覆盖application.properties里的属性。
禁用Banner只是使用属性的一个小例子。让我们再看几个例子，看看如何通过常用途径微调自动配置的Bean。



**自动配置微调**

1. 禁用模板缓存

将 spring.thymeleaf.cache 设置为 false 就能禁用Thymeleaf模板缓存。在命令行里运行应用程序时，将其设置为命令行参数即可：

```cmd
$ java -jar readinglist-0.0.1-SNAPSHOT.jar --spring.thymeleaf.cache=false
```

yml:

````yaml
spring:
	thymeleaf:
		cache: false
````

你一定要确保这个文件不会发布到生产环境，否则生产环境里的应用程序就无法享受模板缓存带来的性能提升了。
作为开发者，在修改模板时始终关闭缓存实在太方便了。为此，可以通过环境变量来禁用Thymeleaf缓存：

```bash
$ export spring_thymeleaf_cache=false
```



2.配置嵌入式服务器

无论出于什么原因，让服务器监听不同的端口，你所要做的就是设置 server.port 属性。要是只改一次，可以用命令行参数：

```cmd
$ java -jar readinglist-0.0.1-SNAPSHOT.jar --server.port=8000
```

但如果希望端口变更时间更长一点，可以在其他支持的配置位置上设置 server.port 。

yml:

```yaml
server:
	port: 8000
```

peroperties:

```properties
server.port=8080
```



3. 配置日志