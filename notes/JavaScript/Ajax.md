# Ajax

### 一. 基本概念


- 基本概念：异步请求，局部刷新
- 实现原理：异步请求到数据后，通过脚本修改html**局部**页面

### 二.使用

```javascript
<script type="text/javascript" src="js/jquery-3.3.1.js"></script>
<script type="text/javascript">
    $(function () {
        $("#btn").click(function () {
           $.ajax({
               url:"/mvc02/stu/json",
               contentType:"application/json",
               dataType:"json",
               data:'{"name":"zhangsna","pass":"lisi","date":"1995-08-22"}',
               type:"post",
               success:function (data) {
                   alert(data.pass+"***"+data.name)
               }
           })
        })
    })
</script>
```

**url:** 请求的控制器地址  

**contentType** : 请求的数据类型

**data:** 请求参数

**success:** 请求成功后执行的回调函数

**type:** 请求的类型

**dataType** :预期服务器返回的数据类型



### 三. 坑

与**springmvc**的**@RequestBody** 注解一起使用时,ajax中的data必须**'{"name":"zhangsna","pass":"lisi"}'** ,字面量对象需要用单引号引起来,字面量对象里的属性,如name也必须用双引号引起来,否则可能出现如下异常

而在参数上不使用**@RequestBody** 注解的话,字面量写法就没有那么严格.

**ajax中的data**

```javascript
 data:'{"name":"zhangsna","pass":"lisi","date":"1995-08-22"}',
```

date日期类型的月 必须是两位数,否则传到后端,springMVC框架无法解析,

转换异常错误. 



**[org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Unexpected character ('n' (code 110)): was expecting double-quote to start field name; nested exception is com.fasterxml.jackson.core.JsonParseException: Unexpected character ('n' (code 110)): was expecting double-quote to start field name at [Source: (PushbackInputStream); line: 1, column: 3**

