### web项目的绝对路径和相对路径

* **服务器端相对地址**

服务器端的相对地址指的是相对于你的web应用的地址，这个地址是在服务器端解析的。也就是说在jsp和servlet中的相对地址是相对于你的web应用，即相对于`http://192.168.0.1/webapp/`的。

```html
举例：
1.servlet中：
request.getRequestDispatcher("/user/index.jsp")，这个"/user/index.jsp"是相对于当前web应用的webapp目录的,
其绝对地址就是：http://192.168.0.1/webapp/user/index.jsp

2.jsp中：
<%response.sendRedirect("/user/a.jsp");%>
其绝对地址是：http://192.168.0.1/webapp/user/a.jsp
```

* **客户端相对地址**

所有的HTML页面中的相对地址都是相对于服务器根目录(`http://192.168.0.1/`)的，而不是相对于服务器根目录下Web应用目录(`http://192.168.0.1/webapp/`)的。

```html
举例：
HTML中form表单的action属性的地址是相对于服务器根目录(http://192.168.0.1)的，
所以提交到index.jsp为：action="/webapp/user/index.jsp"或action="<%=request.getContextPath()%>/user/a.jsp";

说明：
一般情况下，在JSP/HTML页面等引用的CSS,JavaScript.Action等属性前面最好都加上<%=request.getContextPath()%>，以确保所引用的文件都属于Web应用中的目录。
```

注意：
应该尽量避免使用 <font color="red">".","./","../../"</font>等类似的相对该文件位置的相对路径，否则当文件移动时，很容易出现问题。

  <font color="red">"./"代表当前目录</font>

  <font color="red">"../"代表上级目录</font>

  <font color="red">"../../"代表上级目录的上级目录</font>

