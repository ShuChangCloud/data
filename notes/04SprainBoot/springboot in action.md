# Spring Boot  In  Action 



>  Spring Boot  的核心是起步依赖(starter)和自动配置(autoconfig)



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

​	什么情况下会存在一个 JdbcOperations Bean呢？Spring Boot的设计是加载应用级配置，随后再考虑自动配置类。因此，如果你已经配置了一个 JdbcTemplate Bean，那么在执行自动配置时就已经存在一个JdbcOperations 类型的Bean了，于是忽略自动配置的 JdbcTemplate Bean。



​	关于Spring Security，自动配置会考虑几个配置类。在这里讨论每个配置类的细节是不切实际的，但覆盖Spring Boot自动配置的安全配置时，最重要的一个类是 SpringBootWebSecurityConfiguration 。以下是其中的一个代码片段：

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

​	如你所见， SpringBootWebSecurityConfiguration 上加了好几个注解。看到 @ConditionalOnClass 注解后，你就应该知道Classpath里必须要有 @EnableWebSecurity 注解。

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

​	我们可以从上面的例子上看到,当我们使用@EnableWebSecurity编写自己的安全配置类时,就会**完全的覆盖掉自动配置**,这实在是一件让人感到无语的事情. 因为我想的是尽可能的微调.

​	所以在变得非常专业之前,尽量不要全面接springboot的自动配置,应该使用微调的方式,这正是下个小章节要介绍的.



#### 通过属性文件外置配置

​	在处理应用安全时，你当然会希望完全掌控所有配置。不过，为了微调一些细节，比如改改端口号和日志级别，便放弃自动配置，这是一件让人感到恶心的事。

为了设置数据库URL，是配置一个属性简单，还是完整地声明一个数据源的Bean简单？答案不言自明，不是吗？

​	事实上，Spring Boot自动配置的Bean提供了300多个用于微调的属性。当你调整设置时，只要在环境变量、Java系统属性、JNDI（Java Naming and Directory Interface）、命令行参数或者属性文件里进行指定就好了。

​	要了解这些属性，让我们来看个非常简单的例子(以下的配置已经在现在的springboot版本中废弃了,这里只是用一个例子来简单讲解)。

​	你也许已经注意到了，在命令行里运行阅读列表应用程序时，Spring Boot有一个ascii-art Banner。如果你想禁用这个Banner，可以将spring.main.show-banner 属性设置为 false 。有几种实现方式，其中之一就是在运行应用程序的命令行参数里指定：

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

* 命令行参数

*  java:comp/env 里的JNDI属性

* JVM系统属性

* 操作系统环境变量

* 随机生成的带 random.* 前缀的属性（在设置其他属性时，可以引用它们，比如 ${random.long} ）

*  应用程序以外的application.properties或者appliaction.yml文件

*  打包在应用程序内的application.properties或者appliaction.yml文件

*  通过 @PropertySource 标注的属性源

*  默认属性

  

  这个列表按照优先级排序，也就是说，任何在高优先级属性源里设置的属性都会覆盖低优先级的相同属性。例如，命令行参数会覆盖其他属性源里的属性。



application.properties和application.yml文件能放在以下四个位置。

* 外置，在相对于应用程序运行目录的/config子目录里。
*  外置，在应用程序运行的目录里。
* 内置，在config包内。
* 内置，在Classpath根目录。



同样，这个列表按照优先级排序。也就是说，/config子目录里的application.properties会覆盖应用程序Classpath里的application.properties中的相同属性。

​	此外，如果你在同一优先级位置同时有application.properties和application.yml，那么application.yml里的属性会覆盖application.properties里的属性。

**小结:** 本节只是简单的介绍了自动配置能来自哪些属性源,还有自动配置的优先级.



##### 自动配置微调

禁用Banner只是使用属性的一个小例子。让我们再看几个例子，看看如何通过常用途径微调自动配置的Bean。

1. **禁用模板缓存**

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



2. **配置嵌入式服务器**

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



3. **配置日志**

默认情况下，Spring Boot会用Logback来记录日志，并用 **INFO** 级别输出到控制台。

用其他日志实现替换Logback:

以Maven为例，应排除掉根起步依赖传递引入的默认日志起步依赖，这样就能排除
Logback了：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

排除默认日志的起步依赖后，就可以引入你想用的日志实现的起步依赖了。在Maven里可
以这样添加Log4j2：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j</artifactId>
</dependency>
```

​	完全掌握日志配置，可以在Classpath的根目录（src/main/resources）里创建logback.xml文件。但这并没有什么必要,只要不是非常细粒度的控制日志,那么进行属性微调足以满足大部分业务的需求了.

下面是logback的属性微调:

要设置日志级别，你可以创建以 logging.level 开头的属性，后面是要日志名称。如果根日志级别要设置为 WARN ，但Spring Security的日志要用 DEBUG 级别，可以在application.yml里加入以下内容：

```yaml
logging:
	level:
	root: WARN
    org:
    	springframework:
    		security: DEBUG
```

另外，你也可以把Spring Security的包名写成一行：

```yaml
logging:
    level:
		root: WARN
		org.springframework.security: DEBUG
```



现在，假设你想把日志写到位于/var/logs/目录里的BookWorm.log文件里。使用 logging.path 和 loggin.file 属性就行了：

```yaml
logging:
    path: /var/logs/
    file: BookWorm.log
    level:
        root: WARN
        org.springframework.security: DEBUG
```

假设应用程序有/var/logs/的写权限，日志就能被写入/var/logs/BookWorm.log。默认情况下，日志文件的大小达到10MB时会切分一次。

与之类似，这些属性也能在application.properties里设置：

```properties
logging.path=/var/logs/
logging.file=BookWorm.log
logging.level.root=WARN
logging.level.root.org.springframework.security=DEBUG
```

如果你还是想要完全掌控日志配置，但是又不想用logback.xml作为Logback配置的名字，可以通过 logging.config 属性指定自定义的名字：

```yaml
logging:
	config:
		classpath:logging-config.xml	#自己定义日式配置文件名
```

虽然一般并不需要改变配置文件的名字，但是如果你想针对不同运行时Profile使用不同的日志配置节，这个功能会很有用。

通常你都无需指定JDBC驱动，Spring Boot会根据数据库URL识别出需要的驱动，但如果识别出问题了，你还可以设置 spring.datasource.driver-class-name 属性：

```	yaml
spring:
    datasource:
    	url: jdbc:mysql://localhost/readinglist
    	username: dbuser
        password: dbpass
        driver-class-name: com.mysql.jdbc.Driver
```

在自动配置 DataSource Bean的时候，Spring Boot会使用这里的连接数据。 DataSource Bean是一个数据源，如果Classpath里有Tomcat的连接池 DataSource ，那么就会使用这个连接池；否则，Spring Boot会在Classpath里查找以下连接池：

*  HikariCP
*  Commons DBCP
*  Commons DBCP2

这里列出的只是自动配置支持的连接池，你还可以自己配置 DataSource Bean，使用你喜欢的各种连接池。

你也可以设置 spring.datasource.jndi-name 属性，从JNDI里查找 DataSource ：

```yaml
spring:
	datasource:
		jndi-name: java:/comp/env/jdbc/readingListDS
```

一旦设置了 spring.datasource.jndi-name 属性，其他数据源连接属性都会被忽略，除非没有设置别的数据源连接属性。



##### 应用程序 Bean 的配置外置

假设我们在某人的阅读列表里不止想要展示图书标题，还要提供该书的Amazon链接。我们不仅想提供该书的链接，还要标记该书，以便利用Amazon的Associate Program，这样如果有人用我们应用程序里的链接买了书，我们还能收到一笔推荐费。
这很简单，只需修改Thymeleaf模板，以链接的形式来呈现每本书的标题就可以了：

```html
<a th:href="'http://www.amazon.com/gp/product/'
    + ${book.isbn}
    + '/tag=habuma-20'"
th:text="${book.title}">Title</a>
```

虽然在模板里修改这个值很简单，但这毕竟也是硬编码。现在只在一个模板里链接到Amazon，但后续可能会有更多页面链接到Amazon，于是需要为应用程序添加功能。那样的话，修改Amazon Associate ID就要改动好几个地方。因此，这种细节最好不要放在代码里，要把它们集中在一个地方维护。

我们可以不在模板里硬编码Amazon Associate ID，而是把它变成模型中的一个值：

```html
<a th:href="'http://www.amazon.com/gp/product/'
    + ${book.isbn}
    + '/tag=' + ${amazonID}"	//
th:text="${book.title}">Title</a>
```

此外， ReadingListController 需要在模型里包含 amazonID 这个键，其中的内容是Amazon Associate ID。同样的道理，我们不应该硬编码这个值，而是应该引用一个实例变量。这个变量的值应该来自属性配置。下面是新的 ReadingListController ，它会返回注入的Amazon Associate ID。

```java
@Controller
@RequestMapping("/")
@ConfigurationProperties(prefix="amazon")
public class ReadingListController {
    private String associateId;
    private ReadingListRepository readingListRepository;
    @Autowired
	public ReadingListController(
    ReadingListRepository readingListRepository) {
    	this.readingListRepository = readingListRepository;
    }
    public void setAssociateId(String associateId) {
    	this.associateId = associateId;
    }
    @RequestMapping(method=RequestMethod.GET)
    public String readersBooks(Reader reader, Model model) {
        List<Book> readingList =
        readingListRepository.findByReader(reader);
        if (readingList != null) {
            model.addAttribute("books", readingList);
            model.addAttribute("reader", reader);
            model.addAttribute("amazonID", associateId);	//属性注入
    	}	
        return "readingList";
    }
    @RequestMapping(method=RequestMethod.POST)
    public String addToReadingList(Reader reader, Book book) {
        book.setReader(reader);
        readingListRepository.save(book);
        return "redirect:/";
    }
}
```

 	ReadingListController 现在有了一个 associateId 属性，还有对应的 set 方法，用它可以设置该属性。readersBooks() 现在能通过 amazonID 这个键把associateId 的值放入模型。

​	棒极了！现在就剩一个问题了——从哪里能取到 associateId 的值。

​	 ReadingListController 上加了 @ConfigurationProperties 注解，这说明该Bean的属性应该是（通过setter方法）从配置属性值注入的。说得更具体一点， prefix 属性说明ReadingListController 应该注入带 amazon 前缀的属性。

例如，我们可以在application.properties里设置该属性：

```properties
amazon.associateId=habuma-20
```

或者在application.yml里设置：

```yaml
amazon:
	associateId: habuma-20
```



**在一个类里收集属性**

​	虽然在 ReadingListController 上加上 @ConfigurationProperties 注解跑起来没问题，但这并不是一个理想的方案。 ReadingListController 和Amazon没什么关系，但属性的前缀却是 amazon ，这看起来难道不奇怪吗？再说，后续的各种功能可能需要在 ReadingListController 里新增配置其他属性，而它们和Amazon无关。

​	与其在 ReadingListController 里加载配置属性，还不如创建一个单独的Bean，为它加上@ConfigurationProperties 注解，让这个Bean收集所有配置属性。

代码 AmazonProperties 就是一个例子，它用于加载Amazon相关的配置属性。

```java
@Component
@ConfigurationProperties("amazon")
public class AmazonProperties {
    private String associateId;
    public void setAssociateId(String associateId) {
    	this.associateId = associateId;
    }
    public String getAssociateId() {
   		 return associateId;
    }
}
```

​	有了加载 amazon.associateId 配置属性的 AmazonProperties 后，我们可以调整ReadingListController 如下所示，让它从注入的 AmazonProperties 中获取Amazon Associate ID。

```java
@Controller
@RequestMapping("/")
public class ReadingListController {
    private ReadingListRepository readingListRepository;
    private AmazonProperties amazonProperties;
    @Autowired		//注入amazonProperties
    public ReadingListController(
        ReadingListRepository readingListRepository,
        AmazonProperties amazonProperties) {
        this.readingListRepository = readingListRepository;
        this.amazonProperties = amazonProperties;
    }
    @RequestMapping(method=RequestMethod.GET)
    public String readersBooks(Reader reader, Model model) {
        List<Book> readingList =
        readingListRepository.findByReader(reader);
        if (readingList != null) {
            model.addAttribute("books", readingList);
            model.addAttribute("reader", reader);
            //向model中添加amazonID,通过amazonProperties
            model.addAttribute("amazonID", amazonProperties.getAssociateId());
    	}
   		 return "readingList";
    }
    @RequestMapping(method=RequestMethod.POST)
        public String addToReadingList(Reader reader, Book book) {
        book.setReader(reader);
        readingListRepository.save(book);
        return "redirect:/";
    }
}
```

ReadingListController 不再直接加载配置属性，转而通过注入其中的 AmazonProperties Bean来获取所需的信息。

​	在AmazonProperties类中你应该注意到了它使用了两个注解:@ConfigurationProperties注解和

@Component注解.是的要使用@ConfigurationProperties将模型中的值注入到类中,该类就必须要由spring管理,

因此必须使用@Component注解. 这里有一个可以替代的方案就是在AmazonProperties类上只加上@EnableConfigurationProperties注解.该注解表示把类交给容器管理同时启用@ConfigurationProperties.

但注意@EnableConfigurationProperties的属性是class[ ]. 下面是一个例子:

```java
@ConfigurationProperties(prefix = "author")	//把容器中的配置属性绑定到该类上
public class AuthorProperties {
    private String name;
    private String age;
    //省略getter setter
}
```

下面是引用该类的测试类

```java
@EnableConfigurationProperties(AuthorProperties.class)	//激活属性类
public class SpringBoot02ConfigApplicationTests {
    @Autowired
    private AuthorProperties authorProperties;
    @Test
    public void test001() {
        String age = authorProperties.getAge();	//从属性类得到值
        System.out.println(age);
    }
}

```

​	可以看到AuthorProperties类上没有@Component，在我们需要的时候可以把@EnableConfigurationProperties和@Conditional注解连用，当满足某个条件的时候才将AuthorProperties类导入到容器，并激活。这样可以防止由于包扫描一开始就把各种配置都导入容器，使容器的内容变得臃肿。

​	在IDEA里使用了@EnableConfigurationProperties注解，但 AuthorProperties类上没有加@Component可能会提示编译错误，但经测试后，实际上是可以运行的。

##### 使用 Profile 进行配置

​	当应用程序需要部署到不同的运行环境时，一些配置细节通常会有所不同。比如，数据库连接的细节在开发环境下和测试环境下就会不一样，在生产环境下又不一样。Spring Framework从Spring 3.1开始支持基于Profile的置。Profile是一种条件化配置，基于运行时激活的Profile，会使用或者忽略不同的Bean或配置类。

​	举例来说，假设我们在下面的代码里创建的安全配置是针对生产环境的，而自动配置的安全配置用在开发环境刚刚好。在这个例子中，我们就能为 SecurityConfig 加上 @Profile 注解：

```java
@Profile("production")		//生产环境
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	...
}
```

​	这里用的 @Profile 注解要求运行时激活 production Profile，这样才能应用该配置。如果production Profile没有激活，就会忽略该配置. 没有被激活的话,此时缺少其他用于覆盖的安全配置，于是就只会用自动配置(autoconfig)的安全配置。

激活方式 :设置 spring.profiles.active 属性为production就能激活该profile .

命令行方式:

```cmd
$ java -jar readinglist-0.0.1-SNAPSHOT.jar --spring.profiles.active=production
```

application.yaml:

```yaml
spring:
	profiles:
		active: production
```

application.properties:

```properties
spring.profiles. active=production
```

或者使用之前开头提到的各种方法,总之方式真是多到眼花缭乱了!



​	但由于Spring Boot的自动配置替你做了太多的事情，要找到一个能放置 @Profile 的地方还真不怎么方便。

比如我们看到前面的SecurityConfig类上居然都使用了@EnableWebSecurity,这个注解用了就会覆盖掉自动配置,得不偿失啊. 

幸运的是，Spring Boot支持为application.properties和application.yml里的属性配置Profile。

1. **使用特定于Profile的属性文件**

可以创建额外的属性文件，遵循application-{profile}.properties这种命名格式，这样就能提供特定于Profile的属性了。

在日志这个例子里，开发环境的配置可以放在名为application-development.properties的文件里，配置包含日志级别和输出到控制台：

```properties
logging.level.root=DEBUG
```

对于生产环境，application-production.properties会将日志级别设置为 WARN 或更高级别，并将日志写入日志文件：

```properties
logging.path=/var/logs/
logging.file=BookWorm.log
logging.level.root=WARN
```

与此同时，那些并不特定于哪个Profile或者保持默认值（以防万一有哪个特定于Profile的配置不指定这个值）的属性，可以继续放在application.properties里：

```properties
amazon.associateId=habuma-20
logging.level.root=INFO
```



2. **使用多Profile YAML文件进行配置**

​	如果使用YAML来配置属性，则可以遵循与配置文件相同的命名规范，即创建application-{profile}.yml这样的YAML文件，并将与Profile无关的属性继续放在application.yml里。

​	但既然用了YAML，你就可以把所有Profile的配置属性都放在一个application.yml文件里。举例来说，我们可以像下面这样声明日志配置：

```yaml
logging:
	level:
		root: INFO
---
spring:
	profiles: development
logging:
    level:
    	root: DEBUG
---
spring:
	profiles: production
logging:
    path: /tmp/
    file: BookWorm.log
    level:
    	root: WARN
```

​	如你所见，这个application.yml文件分为三个部分，使用一组三个连字符（ --- ）作为分隔符。第二段和第三段分别为 spring.profiles 指定了一个值，这个值表示该部分配置应该应用在哪个 Profile 里 。

 	第 二 段 中 定 义 的 属 性 应 用 于 开 发 环 境 ， 因 为 spring.profiles 设 置 为development 。

​	与之类似，最后一段的 spring.profile 设置为 production ，在 productionProfile被激活时生效。

这里没有显示的去激活profile ,需要的话可以在第一段上加上.

```yaml
spring:
  profiles:
    active: development  #注意dev前面有空格
```

acitve: development表示激活开发环境.

另一方面，第一段并未指定 spring.profiles ，因此这里的属性对全部Profile都生效，或者对那些未设置该属性的激活Profile生效。如果同时有和其他profile不一样的配置,又指定了profile,那么avtive属性将会覆盖已有的配置.



##### 定制应用程序错误页面

springboot自动为我们配置了一个错误页面.它长成下面这个样子:

![](assets/errorpage.png)



​	错误总是会发生的，那些在生产环境里最健壮的应用程序偶尔也会遇到麻烦。虽然减小用户遇到错误的概率很重要，但让应用程序展现一个好的错误页面也同样重要。

了让你的应用程序故障页变成大师级作品，你可以自己定制一个错误页。

​	Spring Boot自动配置的默认错误处理器会查找名为error的视图，如果找不到就用默认的白标错误视图，因此，最简单的方法就是创建一个自定义视图，让解析出的视图名为error。

​	这一点归根到底取决于错误视图解析时的视图解析器。

*  实现了Spring的 View 接口的Bean，其 ID为 error （由Spring的 BeanNameViewResolver所解析）。
*  如果配置了Thymeleaf，则有名为error.html的Thymeleaf模板。
* 如果是用JSP视图，则有名为error.jsp的JSP模板



​	因为我们的阅读列表应用程序使用了Thymeleaf，所以我们要做的就是创建一个名为error.html的文件，把它和其他的应用程序模板一起放在模板文件夹(templates)里。

下面是一个简单的错误页面 error.html

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Oops!</title>
</head>
<div class="errorPage">
    <span class="oops">Oops!</span><br/>
    <p>There seems to be a problem with the page you requested
    <p th:text="${'Details: ' + #messages}"></p>
</div>
</html>
```

它看起来像是这样的

![](assets/errres.png)



#### 小结

​	Spring Boot消除了Spring应用程序中经常要用到的很多样板式配置。让Spring Boot处理全部配置，你可以仰仗它来配置那些适合你的应用程序的组件。当自动配置无法满足需求时，SpringBoot允许你覆盖并微调它提供的配置。

​	覆盖自动配置其实很简单，就是显式地编写那些没有Spring Boot时你要做的Spring配置。Spring Boot的自动配置被设计为优先使用用户的显示应用程配置，然后才轮得到自己的自动配置。

​	即使自动配置合适，你仍然需要调整一些细节。Spring Boot会开启多个属性解析器，让你通过环境变量、属性文件、YAML文件等多种方式来设置属性，以此微调配置。这套基于属性的配置模型也能用于应用程序自己定义的组件，可以从外部配置源加载属性并注入到Bean里。

​	Spring Boot还自动配置了一个简单的白标错误页，虽然它比异常跟踪信息友好一点，但在艺术性方面还有很大的提升空间。幸运的是，Spring Boot提供了好几种选项来自定义或完全替换这个白标错误页，以满足应用程序的特定风格。

