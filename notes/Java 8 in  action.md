###第四章
-  流是“从支持数据处理操作的源生成的一系列元素”。
-  流利用内部迭代：迭代通过 filter 、 map 、 sorted 等操作被抽象掉了。
-  流操作有两类：中间操作和终端操作。
-  filter 和 map 等中间操作会返回一个流，并可以链接在一起。可以用它们来设置一条流
水线，但并不会生成任何结果。
-  forEach 和 count 等终端操作会返回一个非流的值，并处理流水线以返回结果。
-  流中的元素是按需计算的。