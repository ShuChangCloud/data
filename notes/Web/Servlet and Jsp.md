## IDE部分

>**注意**
 
- jsp和xml中的内容更新不需要重启服务器
- servlet和类更新都需要重启服务器
- /WEB-INF下的内容不能直接访问，否则404
- mvc:resource 处理静态资源 location属性是工程下的目录  mapping属性是浏览器url地址的映射
location和mapping资源位置要保持一致

> 路径问题 web项目的相对路径和绝对路径
> ##加"/"的都是绝对路径，只有`req.getRequestDispatcher("page")`中加上"/"是表示从WebContent目录下的路径<br>其他情况下加"/"都表示从服务器根目录下（webapps）的绝对路径##

一个浏览器同时开两个访问百度网页的窗口是几个session
从面向对象编程：session和cookie都是会话跟踪技术，这就是为什么只要登录了百度或者淘宝网不管打开多少个浏览器窗口都是同一个账号原因
如果一个web应用不使用会话跟踪技术，打开多少个窗口就是多少个session


springmvc作用域传值：如果用参数（request/respones/application/session传值  实际上是把键值对存放在作用域中，底层再通过默认的forward的转发方式把值带给请求的另外一个控制器或页面）   传值还可以通过在方法的参数中使用Map接口等，springmvc底层会实例化一个对象来传值
在最新的springmvc中，springmvc推荐使用modelandview来传值



springmvc文件上传出现空指针异常 ， 检查了jsp页面中的input-type参数名和控制器中参数的名字一样没问题
原因可能是springmvc.xml中bean的配置   bean的id必须是multipartResolver，少一个错一个字符都会报错！


> SpringMVC拦截器

- springmvc中也不允许在多个控制器中出现相同的映射地址 即requestMapping，否则出现There is already 'XX' bean method异常

- preHandle：在进入控制器之前执行  若返回false，则阻止进入控制器。此功能用于用户登录验证的控制，如用户登录就跳转到主页，未登录的话就让控制器阻止进入
- postHandle：//控制器执行完成，进入到jsp之前执行   	用于日志记录，敏感词语过滤

- afterCompletion：//jsp执行完成后执行  用于错误日志收集

> Filter过滤器

- 在doFilter（）方法中，chain.doFilter()前的一般是对request执行的过滤操作，chain.doFilter后面的代码一般是对response执行的操作
也就是说web.xml中监听器配置在过滤器之前，过滤器配置在servlet之前，否则会出错。

> ###HTTP GET  POST方法###
GET是幂等的 POST不是幂等的，意味着GET可以多次重复提交，而POST每次提交就都有可能使服务器上的数据发生变化，要特别小心
但是你仍然可以在doget方法中用来更新市局，但请不要那么做，因为这是一个糟糕的做法。要注意的是HTTP GET和Servlet中的doGet（）区别

响应一旦提交（sendRedirect，write,flush），就意味着响应已经结束了，就不能再转发请求
同样的如果把请求转发到别的jsp或servlet，就不能自己在发送响应了，所以servletRequest调用了getRequestDispatcher.forward就不能发送自己的响应

> EL表达式  

- EL和定制标签可以解决大量的scriptlet代码在JSP页面中的问题

- EL表达式中的[ ]是 属性bean/Map/List/数组的性质，如person【“name”】就是person属性的性质name，整个表达式${person["name"]}的意思就是取person属性的性质name，并把name性质输出

- 作用域对象是一个映射（Map），如pageScope对象，但pageContext不是映射，可以把pageContext对象看成是一个bean
为什么要使用作用域对象？ 作用域对象除了可以指定从指定作用域取属性，避免命名冲突以外，还可以解决属性名不遵从JAVA命名规范的问题。
除了属性名可以不遵从Java命名规范以外，性质名也可能不遵从java命名规范

- EL的作用：获得属性值，性质值，调用java函数


