## mybatis动态SQL 

### if用法

**在where条件中使用**

```xml
<select id="selectByUser" resultMap="baseMap">
    select  * from sys_user 
    where 1=1
    <if test=" userName!=null and userName!=''">
       and  user_name=#{userName}
    </if>
    <if test=" userEmail!=null and userEmail!=''">
       and  user_email=#{userEmail}
    </if>
</select>
```

上面的demo所涉及的知识点有很多

* where 1=1防止后面的条件不成立,导致生成的SQL报错
* if标签test属性中是实体属性userEmail而不是数据库字段user_email 原因是sql执行顺序
* baseMap可以复用,时mapper命名空间下的其它sql语句更加灵活



**在update更新列中使用**

updaet table set id=#{id} where id=#{id}

防止造成sql语法错误



**在insert动态插入列中使用**

有则插入字段,无则忽略字段

```xml
<insert>
    insert into sys_user(user_name,user_email
    <if test="passWord!=null and passWord!=''">
    	,pass_word
    </if>
    ) values(#{userName},#{userEmail}
    <if test="passWord!=null and passWord!=''">
    	,pass_word=#{passWord}
    </if>
    )
</insert>
```

值得注意的是 上面列中使用了if标签,下面插入的时候也要使用if标签

另外if标签中还可以使用_databaseId来适配不同数据库,但是要在全局配置文件中开启多数据库支持.

```xml
<select>
select * from sys_user
    <where>
        <if test="userName!=null and userName!=''">
             <if test="_databaseId=='mysql'">
            and user_name like concat("%",#{userName},"%")
            </if>
          <if test="_databaseId=='oracle'">
        	and user_name like '%'||#{userName}||'%'
        	</if>
        </if> 
    </where> 
</select>
```



### choose用法

if标签已经很灵活但它仍然无法满足if..else, if..else.if..的逻辑

所以这时候就轮到choose when otherwise标签



choose组合标签类似于java中的if..else if..else

```xml
<select>
select user_name userName,
    user_email userEmail.
    pass_word passWord
    from sys_user
    where 1=1
    <choose>
        <when test="userName!=null and userName!=''">
            and use_name=#{userName}
        </when>
         <when test="id!=null and id!=''">
            and id=#{id}
        </when>
        <otherwise>
            and 1=2
        </otherwise>
    </choose>
</select>
```

在if..else if ..else结构中满足一个,后续的就不会在执行了.choose标签也是如此,使用该标签时,一定要严谨,避免得到错误的sql语句.



### where   set  trim标签

where和set标签都是trim的一种特殊形式

**where用法**

where标签作用: 如果where标签内有内容就插入where关键字,如果where后面的内容是以and 或 or开头的,则去掉第一个and或or.

```xml
<select>
	select * from sys_user 
    <where>
        <if id!=null and id!=''>
            and id=#{id}
        </if>
         <if userName!=null and userName!=''>
           and user_name=#{userName}
        </if>
    </where>
</select>
```



### set用法

set标签作用 :如果标签里有内如则插入set关键字,如果set结尾的字符串是逗号,则去掉结尾的逗号

```xml
<update>
	update  sys_user
    <set>
         <if userEmail!=null and userEmail!=''>
            and user_email=#{userEmail},
        </if>
         <if userName!=null and userName!=''>
           and user_name=#{userName},
        </if> 
        id=#{id},
    </set>
   where id=#{id}
</insert>
```

set没能解决标签内容不存在会报错的问题,因此还是需要 where id=#{id}



### trim用法

* **prefix** 标签中有内容时,在最前面加上的内容
* **prefixOverride** 标签有内容时,去掉最前面的内容
* **suffix** 标签有内容时,在最后面要加的内容
* **suffixOverride** 标签有内容时,去掉最后面的内容

trim中都是先去掉内容在加上内容.



### foreach语法

foreach主要用来遍历iterable(List和Set实现了该接口,而数组会转换成List类型)和map类型的数据.



#### foreach实现in集合

```java
/**
根据用户id集合查询
*/
List<SysUser> selectByIdList(List idList)
```



```xml
<select>
select * from sys_user where id in
    <foreach collection="list" open="(" close=")" separator="," item="id">
        #{id}
    </foreach>
</select>
```

collection的值和接口中传入的参数有关,

* 如果是数组,则值是array. 
* 如果是List,则值是list
* 如果是map,值是collection
*  使用@param注解会更加灵活.



#### foreach实现批量插入

如果数据库支持批量插入语法,就可以通过foreach实现,目前支持的数据库有MySQL,sqlserver 2008以上,DB2,PostGreSQL,H2等, Oracle不直接支持(有别的办法)

接口方法声明:

```java
int insertUserList(List<SysUser> userList)
```

xml中:

```xml
<insert>
insert into sys_user(user_name,user_email,user_info)  values
   <foreach collection="list" open="(" close=")" seprator="," item="user">
       #{user.userName},#{user.userEmail},{user.userInfo}
    </foreach>
</insert>
```

**注意:item指定了循环变量名后,在引用值的时候应该使用 "属性.属性" 的方式,如user.userName**



如果要使用批量插入返回自增主键值,只需简单的改动

```xml
<insert id="insertList" useGeneratedKeys="true" keyProperty="id"></insert>
```

* **useGeneratedKeys** 返回自增主键
* **keyProperty** 将返回的主键值封装到实体的哪个属性中

在使用insertList方法后,

```java
//打印返回的id
idList.stream.map(User::getId).foreach(System.out::println)
```

**但是完美支持批量插入返回主键功能的数据库目前只有MySQL**



#### foreach动态实现update

当参数类型是Map的时候,foreach标签中的index属性不是索引值,而是map的key

```xml
<update>
	update sys_user set
    <foreach collection="_parameter" serprator="," index="key" item="value">
        #{key} = #{value}
    </foreach>
    where id=#{id}
</update>
```

通过map参数更新列的接口方法

```java
int updateByMap(Map<String,Object> map)
```

实例

```java
Map<String,Object> map=new HashMap();
//id作为查询条件必须存在,上面的sql语句可以看出来
map.put("id",1L);
map.put("user_name","zhangasns");
userMapper.updateByMap(map);
```





## MyBatis高级查询





