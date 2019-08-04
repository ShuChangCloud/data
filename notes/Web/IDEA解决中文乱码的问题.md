### IDEA解决中文乱码的问题

```java
req.setCharacterencoding("utf-8")
```



```java
new String(str.getbytes("iso-8859-1"),"utf-8");
```



```html
IDEA Tomcat中配置-Dfile.encoding=UTF-8
IDEA安装目录的两个配置文件中加入-Dfile.encoding=UTF-8
Tomcat 配置文件全局设置成UTF-8
```