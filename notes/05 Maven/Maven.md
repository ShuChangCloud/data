# Maven的使用



### 一 Maven的web工程结构

![目录结构](https://github.com/ShuChangCloud/data/raw/master/iamge/%E7%9B%AE%E5%BD%95%E7%BB%93%E6%9E%84.png)

1. -src:存放源代码的目录
2. --main 编译后的类路径 
3. ---java 存放java代码的文件夹
4. ---resources 存放资源的文件,如xml,properties等
5. --test 测试代码的存放的地方  里面也有java文件夹和resources文件夹,打包后不参与编译
6. -target 存放编译后的结果的文件夹
7. -pom.xml 引入资源 添加依赖,和项目的构建有关的配置文件



### 二 注意包和文件夹的区别,尤其当IDEA创建的maven项目结构不完整时,可手动添加对对应文件夹

1. 找到工程的modules设置![修改1](https://github.com/ShuChangCloud/data/raw/master/iamge/%E4%BF%AE%E6%94%B91.png)



2.  通过修改文件夹的状态或者创建新文件夹达到目的![修改2](https://github.com/ShuChangCloud/data/raw/master/iamge/%E4%BF%AE%E6%94%B92.png)

   从头修改的时候,先点击右边第一个叉叉,移除掉整个项目,在重新通过路径导入整个项目,然后在配置整个装填.



