

-  流是“从支持数据处理操作的源生成的一系列元素”。
-  流利用内部迭代：迭代通过 filter 、 map 、 sorted 等操作被抽象掉了。
-  流操作有两类：中间操作和终端操作。
-  filter 和 map 等中间操作会返回一个流，并可以链接在一起。可以用它们来设置一条流
  水线，但并不会生成任何结果。
-  forEach 和 count 等终端操作会返回一个非流的值，并处理流水线以返回结果。
-  流中的元素是按需计算的。





### 一. 流是什么

流是Java API的新成员，它允许你以声明性方式处理数据集合（通过查询语句来表达，而不
是临时编写一个实现）。就现在来说，你可以把它们看成遍历数据集的高级迭代器。

#### 1.1流操作

#####  1.1.1 中间操作

诸如 filter 或 sorted 等**中间操作会返回另一个流**。这让多个操作可以连接起来形成一个查
询。重要的是，除非流水线上触发一个终端操作，否则中间操作不会执行任何处理——它们很懒。
这是因为中间操作一般都可以合并起来，在**终端操作时一次性全部处理**。

**总结:中间操作返回的还是流**

##### 1.1.2 终端操作

终端操作会从流的流水线生成结果。其**结果是任何不是流的值**，比如 List 、 Integer ，甚
至 void 。

**总结:终端操作返回的结果一定不是流**

##### 1.1.3 使用流

*  一个数据源（如集合）来执行一个查询
*  一个中间操作链，形成一条流的流水线
*  一个终端操作，执行流水线，并能生成结果

流的流水线背后的理念类似于构建器模式。构建器模式中有一个调用链用来设置一套配
置（对流来说这就是一个中间操作链），接着是调用 built 方法（对流来说就是终端操作）。

### 二. 使用流

#### 2.1 筛选和切片

在本节中，我们来看看如何选择流中的元素：用谓词筛选，筛选出各不相同的元素，忽略流
中的头几个元素，或将流截短至指定长度。

##### 2.1.1 用谓词筛选

Streams 接口支持 filter 方法。该操作会接受一个谓词（一个返回boolean 的函数）作为参数，并返回一个包括所有符合谓词的元素的流。

```java
List<Dish> vegetarianMenu = menu.stream()
                            .filter(Dish::isVegetarian) //这里用方法引用筛选素食
                            .collect(toList());
```

**filter**实现筛选

**方法引用不是方法调用.**



##### 2.1.2 筛选各异的元素

```java
List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
numbers.stream()
                .filter(i -> i % 2 == 0)
                .distinct()		//去除流中重复的元素
                .forEach(System.out::println);
```

**distinct**去除流中重复的元素.



##### 2.1.3 截短流



````java
List<Dish> dishes = menu.stream()
                                .filter(d -> d.getCalories() > 300)
                                .limit(3)	//截断流,只获取流中前三个元素
                                .collect(toList());
````

**limit(n)**截断流,此方法接受一个数字,**截取**流中前n个元素的**流** .



##### 2.1.4 跳过元素

````java
List<Dish> dishes = menu.stream()
                                .filter(d -> d.getCalories() > 300)
                                .skip(3)	//跳过流中的3个元素
                                .collect(toList());
````

**skip(n)  **,此方法接受一个数字,跳过流中的n个元素,**截取**跳过元素后面的元素作为新流.

**limit(n) 和 skip(n) 是互补的！** 



#### 2.2 映射

一个非常常见的数据处理套路就是从某些对象中选择信息.从元素中选择信息,然后**映射(只创建,不修改)**成另外一种信息.

##### 2.2.1 对流中每一个元素应用函数

流支持 map 方法，它会**接受一个函数作为参数**。这个函数会被应用到每个元素上.

````java
List<String> dishNames = menu.stream()
                                        .map(Dish::getName)	//映射,这里是方法引用
                                        .collect(toList());
````



##### 2.2.2 流的扁平化

```java
   public void test1(){
        String[] words={"hello","world"};
                Arrays.stream(words)	//生成一个流,这个流里面有两个元素
                .map(w -> w.split(""))       // 将每个单词转换为由其字母构成的数组
                .flatMap(Arrays::stream)	//流的扁平化,把每个生成流扁平为单个流
                .distinct()
                .collect(Collectors.toList())
                .forEach(System.out::println);
    }

```

 Arrays.stream(words)的方法声明

```java
public static <T> java.util.stream.Stream<T> stream(@NotNull T[] array)
```

**流的扁平化** 就是将多个单独的流**合并**成一个流. 类似于集合中的**List.toList()** ,可以将两个集合合并成一个集合.

一言以蔽之， **flatmap** 方法让你把一个流中的**每个元素**都换成另一个**流**，这个流中有**多个元素**,然后就会**转换成多个流**,最后把所有的流连接起来成为一个**流**。



#### 2.3 查找和匹配

另一个常见的数据处理套路是看看数据集中的某些元素是否匹配一个给定的属性。Stream API通过 allMatch 、 anyMatch 、 noneMatch 、 findFirst 和 findAny 方法提供了这样的工具。

##### 2.3.1 检查谓词是否至少匹配一个元素

**anyMatch**会检查流中的元素**是否至少有一个满足条件.**

检测菜单里的菜是否满足有一个是素菜

```java
if(menu.stream().anyMatch(Dish::isVegetarian)){
	System.out.println("The menu is (somewhat) vegetarian friendly!!");
}
```

**anyMatch**返回的是一个boolean值,所以它是**终端操作.** 



##### 2.3.2 检查谓词是否匹配所有元素

**allMatch** 方法的工作原理和 **anyMatch** 类似,但它会看流中的元素**是否全部满足条件**.

```java
if(menu.stream().allMatch(Dish::isVegetarian)){
    System.out.println("全部都是素菜");
}
```





和 **allMatch** 相对的是 **noneMatch** ,它可以**确保流中没有任何元素与给定的谓词匹配**。

```JAVA
boolean isHealthy = menu.stream()
								.noneMatch(d -> d.getCalories() >= 1000);
```

anyMatch 、 allMatch 和 noneMatch 这三个操作都用到了我们所谓的**短路**，这就是大家熟悉
的Java中 && 和 || 运算符短路在流中的版本。



##### 2.3.3 查找元素

findAny 方法将返回当前流中的任意元素。它可以与其他流操作结合使用。比如，你可能想找到一道素食菜肴。你可以结合使用 filter 和 findAny 方法来实现这个查询：

```java
Optional<Dish> dish =
                        menu.stream()
                        .filter(Dish::isVegetarian)
                        .findAny();
```

**findAny** 注意这个方法没有任何参数,找到符合条件的菜肴后,立即终止对流的操作.

以下 是**findAny**方法的声明

```java
public abstract java.util.Optional<T> findAny()
```



##### 2.3.4 查找第一个元素

有些流有一个出现顺序,对于这种流,你可能想要找到第一个满足条件的元素.为此有一个 **findFirst**方法

例如，给定一个数字列表，下面的代码能找出第一个平方能被3整除的数：

```java
List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
                            Optional<Integer> firstSquareDivisibleByThree =
                            someNumbers.stream()
                            .map(x -> x * x)		
                            .filter(x -> x % 3 == 0)
                            .findFirst(); // 9
```



**何时使用 findFirst 和 findAny**

答案是并行。找到第一个元素,在并行上限制更多。如果你不关心返回的元素是哪个，请使用 findAny ，因为它在使用并行流时限制较少。



#### 2.4 归约

将流中所有元素反复结合起来操作，最后得到**一个结果**值

