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
### set log levels ### 
log4j.rootLogger = INFO , console , debug , error 
 
### console ### 
log4j.appender.console = org.apache.log4j.ConsoleAppender 
log4j.appender.console.Target = System.out 
log4j.appender.console.layout = org.apache.log4j.PatternLayout 
log4j.appender.console.layout.ConversionPattern = %-d{yyyy-MM-dd HH\:mm\:ss} [%p]-[%c] %m%n 
 
### log file ### 
log4j.appender.debug = org.apache.log4j.DailyRollingFileAppender 
log4j.appender.debug.File = ../logs/springmvc-demo.log 
log4j.appender.debug.Append = true 
log4j.appender.debug.Threshold = INFO 
log4j.appender.debug.layout = org.apache.log4j.PatternLayout 
log4j.appender.debug.layout.ConversionPattern = %-d{yyyy-MM-dd HH\:mm\:ss} [%p]-[%c] %m%n 
 
### exception ### 
log4j.appender.error = org.apache.log4j.DailyRollingFileAppender 
log4j.appender.error.File = ../logs/springmvc-demo_error.log 
log4j.appender.error.Append = true 
log4j.appender.error.Threshold = ERROR 
log4j.appender.error.layout = org.apache.log4j.PatternLayout 
log4j.appender.error.layout.ConversionPattern = %-d{yyyy-MM-dd HH\:mm\:ss} [%p]-[%c] %m%n 
 
 
###需要声明，然后下方才可以使druid sql输出，否则会抛出log4j.error.key not found 
log4j.appender.stdout=org.apache.log4j.ConsoleAppender 
log4j.appender.stdout.Target=System.out 
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout 
log4j.appender.stdout.layout.ConversionPattern=%d{ISO8601} %l %c%n%p: %m%n 
 
### druid sql ### 
log4j.logger.druid.sql=warn,stdout 
log4j.logger.druid.sql.DataSource=warn,stdout 
log4j.logger.druid.sql.Connection=warn,stdout 
log4j.logger.druid.sql.Statement=warn,stdout 
log4j.logger.druid.sql.ResultSet=warn,stdout 
```



### 四.Java中使用

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
Logger logger = LoggerFactory.getLogger(this.getClass());
logger.debug("debug");
```

