### 一. 导入maven依赖:

```xml
 <!-- https://mvnrepository.com/artifact/org.hibernate/hibernate-validator -->
    <dependency>
      <groupId>org.hibernate</groupId>
      <artifactId>hibernate-validator</artifactId>
      <version>5.4.2.Final</version>
    </dependency>
    <!-- https://mvnrepository.com/artifact/javax.validation/validation-api -->
    <dependency>
      <groupId>javax.validation</groupId>
      <artifactId>validation-api</artifactId>
      <version>2.0.1.Final</version>
    </dependency>
```



### 二. 对普通对象校验

```java
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {

    private Integer id;
    @NotNull				//此字段不能为空
    @Size(min = 3,max = 5)	//最小3个字符,最大长度5个字符
    private String name;
}

```

### 三. 使用

```java

    @RequestMapping("/register")
	//查看valid注解的源码可知,@Valid 可以使用在方法参数上
    public ModelAndView register(@Valid Student student, Errors errors, ModelAndView mav) throws IOException {
        if (errors.hasErrors()){	 //使用errors.haserror()判断校验是否通过
            mav.setViewName("redirect:index.jsp");
            return mav;
        }
        mav.addObject("student",student);
        mav.setViewName("forward:s.jsp");
        return mav;
    }
```

