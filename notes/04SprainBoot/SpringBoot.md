# SpringBoot Notes

### 常见注解

**@ConfigurationProperties** 告诉springboot将配置文件里面值取出来关联到改类的属性上,前提是该类必须交给spring容器管理,所以同事需要**@Component** 注解配合使用