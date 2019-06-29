# Log4j



### log4j日志的使用



使用任何日志框架必须使用日志门面,减少对日志实现的依赖

日志门面有:

1. slf4j
2. jcl

### 二.maven依赖

```xml
<!--log4j 依赖-->

<!--slf4j日志门面-->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.7.5</version>
</dependency>

<!--log4j日志适配-->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.12</version>
</dependency>

<!--log4j日志实现-->
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```



### 三.log4j.properties

```properties
log4j.rootLogger=INFO, console, file

log4j.appender.console=org.apache.log4j.ConsoleAppender
log4j.appender.console.layout=org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern=%d %p [%c] - %m%n

log4j.appender.file=org.apache.log4j.DailyRollingFileAppender
log4j.appender.file.File=logs/log.log
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.A3.MaxFileSize=1024KB
log4j.appender.A3.MaxBackupIndex=10
log4j.appender.file.layout.ConversionPattern=%d %p [%c] - %m%n
```



日志配置相关说明：

- `log4j.rootLogger`：根日志，配置了日志级别为 `INFO`，预定义了名称为 `console`、`file` 两种附加器 
- `log4j.appender.console`：console 附加器，日志输出位置在控制台
- `log4j.appender.console.layout`：console 附加器，采用匹配器布局模式
- `log4j.appender.console.layout.ConversionPattern`：console 附加器，日志输出格式为：日期 日志级别 [类名] - 消息`换行符` 
- `log4j.appender.file`：file 附加器，每天产生一个日志文件
- `log4j.appender.file.File`：file 附加器，日志文件输出位置 `logs/log.log`
- `log4j.appender.file.layout`：file 附加器，采用匹配器布局模式
- `log4j.appender.A3.MaxFileSize`：日志文件最大值
- `log4j.appender.A3.MaxBackupIndex`：最多纪录文件数
- `log4j.appender.file.layout.ConversionPattern`：file 附加器，日志输出格式为：日期 日志级别 [类名] - 消息`换行符`

简单的

```properties
log4j.rootLogger=info,stdout,log
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d [%-5p] %l %rms: %m%n
log4j.appender.log=org.apache.log4j.DailyRollingFileAppender
log4j.appender.log.DatePattern='.'yyyy-MM-dd
log4j.appender.log.File=D://debug.log
log4j.appender.log.Encoding=UTF-8
#log4j.appender.log.Threshold = INFO
log4j.appender.log.layout=org.apache.log4j.PatternLayout
log4j.appender.log.layout.ConversionPattern=%d [%-5p] (%c.%t): %m%n
log4j.logger.com.xmcc.mapper=TRACE
```



### 四.Java中使用

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
Logger logger = LoggerFactory.getLogger(this.getClass());
logger.debug("debug");
```

