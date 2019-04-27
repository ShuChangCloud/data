### 数据库的日期和jdk工具包中的日期不一致

从web页面中获取的日期是string格式的,传送到后台后,要用**simpledateformat.parse()** 转换成日期,在把转换以后的date转换成**java.sql.Date**才能存储到数据库中.



### IDEA解决中文乱码的问题

* ```java
  req.setCharacterencoding("utf-8");
  ```

* ```java
  new String(str.getbytes("iso-8859-1"),"utf-8");
  ```

* ```java
  IDEA Tomcat中配置-Dfile.encoding=UTF-8
  IDEA安装目录的两个配置文件中加入-Dfile.encoding=UTF-8
  Tomcat 配置文件全局设置成UTF-8
  ```



