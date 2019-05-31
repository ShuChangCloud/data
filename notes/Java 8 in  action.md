

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

##### 2.4.1 元素求和

可以像下面这样对流中所有的元素求和：

```java
int sum = numbers.stream().reduce(0, (a, b) -> a + b);
```

reduce 接受两个参数：

*  一个初始值，这里是0  
*   一个 **BinaryOperator**<T> 来将两个元素结合起来产生一个新值，这里我们用的是lambda (a, b) -> a + b 。

也很容易把所有的元素相乘，只需要将另一个Lambda： (a, b) -> a * b 传递给 reduce操作就可以了：

```java
int product = numbers.stream().reduce(1, (a, b) -> a * b);
```



**无初始值**

reduce 还有一个重载的变体，它不接受初始值，但是会返回一个 **Optional** 对象：

```java
Optional<Integer> sum = numbers.stream().reduce((a, b) -> (a + b));
```

考虑流中没有任何元素的情况。 reduce 操作无法返回其和，因为它没有初始值。这就是为什么结果被包裹在一个 Optional 对象里，以表明和可能不存在。



##### 2.4.2 最大值和最小值

你可以像下面这样使用 reduce 来计算流中的最大值

```java
Optional<Integer> max = numbers.stream().reduce(Integer::max);
```

要计算最小值，你需要把 Integer.min 传给 reduce 来替换 Integer.max ：

```java
Optional<Integer> min = numbers.stream().reduce(Integer::min);
```

你当然也可以写成Lambda  (x, y) -> x < y ? x : y 而不是 **Integer::min** ，不过后者比较易读。





**流操作：无状态和有状态** 

> ​	在从集合生成流的时候把 Stream 换成 parallelStream 就可以实现并行。
>
> ​	诸如 map 或 filter 等操作会从输入流中获取每一个元素，并在输出流中得到0或1个结果。
> 这些操作一般都是 无状态的：它们**没有内部状态**（假设用户提供的Lambda或方法引用没有内部可变态）。
>
> ​	但诸如 reduce 、 sum 、 max 等操作需要内部状态来累积结果。在上面的情况下，**内部状态很小**.
>
> ​	相反，诸如 sort 或 distinct 等操作一开始都和 filter 和 map 差不多——都是接受一个流，再生成一个流	（中间操作），但有一个关键的区别。从流中排序和删除重复项时都需要知道先前的历史。我们把这些操作叫作 **有状态操作**。



#### 2.5 数值流

##### 2.5.1 原始类型流特化

**映射到数值流** 

```java
int calories = menu.stream()
    .mapToInt(Dish::getCalories)	//映射流,将流转化为原始类型流.
    .sum();
```

 **IntStream** 还支持其他的方便方法，如**max** 、 **min** 、 **average** 等

**转换回对象流**

```java
IntStream intStream = menu.stream().mapToInt(Dish::getCalories);	//转换为原始流
Stream<Integer> stream = intStream.boxed()	//转换回对象流
```

在需要将数值范围装箱成为一个一般流时， **boxed** 尤其有用。



通过下图可以看出stream和原始类型流处在**同一个层级**上.

![](assets/stream.png)



##### 2.5.2 数值范围

Java 8引入了两个可以用于 IntStream 和 LongStream 的静态方法，帮助生成这种范围：range 和 rangeClosed 

```java
IntStream evenNumbers = IntStream.rangeClosed(1, 100)	//生成流中的元素范围是[0,100]
													.filter(n -> n % 2 == 0);
```

**rangeClosed左右都是闭区间 ,range 是不包含结束值的。**



#### 2.6 构建流

##### 2.6.1 由值创建流

可以使用静态方法 **Stream.of** ，通过显式值创建一个流。它可以接受任意数量的参数。

```java
Stream<String> stream = Stream.of("Java 8 ", "Lambdas ", "In ", "Action");
```

你可以使用 empty 得到一个空流，如下所示：

```java
Stream<String> emptyStream = Stream.empty();
```



##### 2.6.2 由数组创建流

可以使用静态方法 **Arrays.stream**  从数组创建一个流。它接受一个数组作为参数,这个数组可以是任意类型的.

```java
int [] numbers = {2, 3, 5, 7, 11, 13};	//注意这里的数组类型是int 而不是Intger
IntStream stream = Arrays.stream(numbers);
stream.sum();
```



##### 2.6.3 由文件生成流

```java
Stream<String> strLines = Files.lines(Paths.get("test.xml"),
                                      Charset.defaultCharset());
	strLines.flatMap(line -> Arrays.stream(line.split("")))
    												.forEach(System.out::println);
```



##### 2.6.4 由函数生成流：创建无限流

Stream API提供了两个静态方法来从函数生成流： Stream.iterate 和 Stream.generate 。

**迭代**

```java
Stream.iterate(0, n -> n + 2)
							.limit(10)
							.forEach(System.out::println);
```



**生成**

```java
Stream.generate(Math::random)
                            .limit(5)
                            .forEach(System.out::println);
```

 generate 方法也可让你按需生成一个无限流。但 generate 不是依次对每个新生成的值应用函数的。它接受一个 **Supplier**<T> 类型的Lambda提供新的值。



#### 2.7 小结

流让你可以简洁地表达复杂的数据处理查询。此外，流可以透明地**并行化**。以下是你应从本章中学到的关键概念。

*  Streams API可以表达复杂的数据处理查询。
*  你可以使用 filter 、 distinct 、 skip 和 limit 对流做筛选和切片。
*  你可以使用 map 和 flatMap 提取或转换流中的元素。
* 你可以使用 findFirst 和 findAny 方法查找流中的元素。你可以用 allMatch 、noneMatch 和 anyMatch 方法让流匹配给定的谓词。
*  这些方法都利用了短路：找到结果就立即停止计算；没有必要处理整个流。
*  你可以利用 reduce 方法将流中所有的元素迭代合并成一个结果，例如求和或查找最大元素。
* filter 和 map 等操作是无状态的，它们并不存储任何状态。 reduce 等操作要存储状态才能计算出一个值。sorted 和 distinct 等操作也要存储状态，因为它们需要把流中的所有元素缓存起来才能返回一个新的流。这种操作称为有状态操作。
* 流有三种基本的原始类型特化： IntStream 、 DoubleStream 和 LongStream 。它们的操作也有相应的特化。
*  流不仅可以从集合创建，也可从值、数组、文件以及 iterate 与 generate 等特定方法创建。
* 无限流是没有固定大小的流。



