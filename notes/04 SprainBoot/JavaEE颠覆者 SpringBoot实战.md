# JavaEE颠覆者 SpringBoot实

### 二 Spring常用配置

####2.1 Spring EL和资源调用 

spring主要在注解**@value**的参数中使用spel

@value注解的作用是:

* 注入
* 获取
* 运算

```java
@Configuration
@ComponentScan
@PropertySource("classpath:com/xmcc/conf/test.properties")
public class ElConfig {
  	//注入
    @Value("i love u")
    private String normal;
	//获取
    @Value("#{systemProperties['os.name']}")
    private String osName;
	
  //运算后注入
    @Value("#{ T(java.lang.Math).random()*100.0}")
    private double randomNumber;

    @Value("#{demoService.another}")
    private String formAnoher;

    @Value("classpath:com/xmcc/conf/test.txt")
    private Resource testFile;

    @Value("http://www.baidu.com")
    private Resource testUrl;

    @Value("${book.name}")
    private String bookName;

    @Autowired
    private Environment environment;

    @Bean
    public static PropertySourcesPlaceholderConfigurer property(){
        return new PropertySourcesPlaceholderConfigurer();
    }

    public void outputResource(){
        try {
            System.out.println(normal);
            System.out.println(osName);
            System.out.println(randomNumber);
            System.out.println(formAnoher);
            System.out.println(IOUtils.toString(testFile.getInputStream()));
            System.out.println(IOUtils.toString(testUrl.getInputStream()));
            System.out.println(bookName);
            System.out.println(environment.getProperty("book.author"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
```

