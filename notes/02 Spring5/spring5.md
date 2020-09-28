# 开发web应用程序

## 展示信息

### 设计视图

像 Thymeleaf 这样的视图库被设计成与任何特定的 web 框架解耦。因此，他们不知道 Spring 的模型抽象，并且无法处理控制器放置在模型中的数据。但是它们可以处理 servlet 请求属性。因此，在 Spring 将请求提交给视图之前，它将**模型数据复制到请求属性中**，而 Thymeleaf 和其他视图模板选项可以随时访问这些属性。



## 使用视图控制器

如果一个控制器足够简单，不填充模型或流程输入（就像 HomeController 一样），那么还有另一种定义控制器的方法。请查看下一个程序清单，了解如何声明视图控制器 —— 一个只将请求转发给视图的控制器。

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
    }
}
```



## 选择视图模板库

但是如果选择使用 JSP，就会遇到一个问题。事实证明，Java servlet 容器 —— 包括嵌入式 Tomcat 和 Jetty 容器 —— 通常在 /WEB-INF 下寻找 jsp。但是如果将应用程序构建为一个可执行的 JAR 文件，就没有办法满足这个需求。因此，如果将应用程序构建为 WAR 文件并将其部署在传统的 servlet 容器中，那么 JSP 只是一个选项。如果正在构建一个可执行的 JAR 文件，必须选择 Thymeleaf、FreeMarker 或表 2.2 中的其他选项之一。