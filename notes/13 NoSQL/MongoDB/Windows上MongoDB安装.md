# Windows 平台安装 MongoDB

### 安装

MongoDB 提供了可用于 32 位和 64 位系统的预编译二进制包，你可以从MongoDB官网下载安装，MongoDB 预编译二进制包下载地址：https://www.mongodb.com/download-center/community

在根目录下新建data文件夹

data文件夹下新建log和db文件夹



db是用来存放数据的,log文件夹是存放日志的。

创建配置文件config.yml

```yml
systemLog:
    destination: file
    #存放日志的文件
    path: c:\data\log\mongod.log
storage:
	#存放数据的文件夹
    dbPath: c:\data\db
    
```

cmd**管理员**运行 在bin目录下把MongoDB注册为系统服务。

```shell
C:\mongodb\bin\mongod.exe --config "C:\mongodb\mongod.cfg" --install
```



### 开启登录认证

先连接上MongoDB,切换到admin数据库。并创建用户

```shel
use admin
db.createUser(
  {
    user: "userName",
    pwd: "password",
    roles: [ { role: "userAdminAnyDatabase", db: "admin" }, "readWriteAnyDatabase" ]
  }
)
```

关闭数据库服务或者在命令行中关闭

```shell
db.adminCommand( { shutdown: 1 } )
```



修改配置文件加上

```yml
security:
    authorization: enabled
```

修改完毕配置文件后,重启数据库服务。

再次登录需要输入密码，否则验证失败。