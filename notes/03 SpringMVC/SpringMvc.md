# springmvc笔记

> 404问题

    <bean id="handlerMapping" class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
    <property name="urlMap">
    	<map>
    		<entry key="as" value-ref="dea"></entry>
    	</map>
    </property>
    </bean>
以上配置文件中 entry中的key是映射地址，映射地址路径中不能有数字，不然报错。


> 在设置过滤器后，仍然出现页面传值获取get和post中文乱码问题

- 参考<https://www.cnblogs.com/Hxinguan/p/5971779.html>


> spring mvc的拦截器只拦截controller不拦截jsp文件，如果不拦截jsp文件也会给系统带安全性问题。      

1.将所有的jsp文件放入到WEB-INF文件夹下    
2.jsp如果不放在WEB-INF文件下，spring mvc是无法拦截的，这种请情况下需要用最原始的servlet的Filter接口



### 一.springMVC组件

1.HandlerAdapter(处理器适配器)

```java
public class SimpleControllerHandlerAdapter implements HandlerAdapter {

	@Override
	public boolean supports(Object handler) {
		return (handler instanceof Controller);
	}

	@Override
	@Nullable
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//这里推出handlerAdapter是给controller处理请求用的
		return ((Controller) handler).handleRequest(request, response);
	}

	@Override
	public long getLastModified(HttpServletRequest request, Object handler) {
		if (handler instanceof LastModified) {
			return ((LastModified) handler).getLastModified(request);
		}
		return -1L;
	}

}
```

所以HandlerAdapter就是执行处理controller中的handlerMethod用的.

