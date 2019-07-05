# Mybatis

### 一.用来循环容器的标签 foreach

主要有三种方法.

* 第一种

```java
List<Account> selByIds(Integer[] ids);
```

相应的配置文件应该

```xml
<select id="selByIds" resultType="account">
    select * from account
    where id in
    <foreach collection="array" item="id" separator="," open="(" close=")">
        #{id}
    </foreach>
</select>
```

注意**collection**属性的值是**array** .

* 第二种

```java
List<Account> selByIds(List<Integer> ids);
```

相应的配置文件应该

```xml
<select id="selByIds" resultType="account">
    select * from account
    where id in
    <foreach collection="list" item="id" separator="," open="(" close=")">
        #{id}
    </foreach>
</select>
```

注意**collection**属性的值是**list** ,这里的**parameterType**可省略不写,要写的话必须是**list** .

* 第三种 使用@Param注解(推荐)

```java
List<Account> selByIds(@Param("ids")List<Integer> ids)
```

相应的配置文件应该

```xml
<select id="selByIds" resultType="account"  parameterType="accountParam">
    select * from account
    where id in
    <foreach collection="ids" item="id" separator="," open="(" close=")">
        #{id}
    </foreach>
</select>
```



**官方文档** 原文:

> 注意: 你可以将一个 List 实例或者数组作为参数对象传给 MyBatis，当你这么做的时候，MyBatis 会自动将它包装在一个 Map 中并以名称为键。List 实例将会以“list”作为键，而数组实例的键将是“array”

