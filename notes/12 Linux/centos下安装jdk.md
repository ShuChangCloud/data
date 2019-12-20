**一. 下载tar包** 

**二. 解压tar包**

```bash
mkdir /usr/local/soft

tar -zxvf xx.x.xx.jdk
```



**三. 设置环境变量**

```bash
vi /etc/profile
```

添加如下内容

```bash
# JAVA_ENV SET
set java environment
JAVA_HOME=/usr/local/soft/jdk1.8.0_171
JRE_HOME=/usr/local/soft/jdk1.8.0_171/jre
CLASS_PATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JRE_HOME/lib
PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin
export JAVA_HOME JRE_HOME CLASS_PATH PATH
```

**四. 让环境生效**

```bash
source /etc/profile
```



**五. 验证安装**

```bash
java -version
```

