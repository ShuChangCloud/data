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