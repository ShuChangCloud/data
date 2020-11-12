# GitHub的使用



### 概念

工作区 workspace  看到的实际文件或实际内容

暂存区index/stage  对想要跟踪变化的文件做索引或标记。这样就能跟踪这个文件的历史版本

本地仓库 repository 某次提交的快照。可根据版本号回退或恢复到某次提交。

远程仓库 remote repository



### 常用命令

 * 仓库路径查询查询： git remote -v
 * 添加远程仓库  git remote add origin <你的项目地址> 
 * 删除指定的远程： git remote rm origin


### 本地初始化一个项目

 * git config --global user.name "你的名字或昵称"
 * git config --global user.email "你的邮箱"
 * 如果设置了全局user.name 和 user.email 就不用在这个新项目中设置了

 

### 同步到本地仓库
 * 先查看工作修改后的状态 ：git status
 * git add <文件名>
 * git commit -m "此处添加描述"


### 同步到远程仓库

 * 关联远程仓库：如下:

```bash
git remote add origin git@github.com:ShuChangCloud/wx_pay.git
```

 * 推送到远程仓库,第一次推送：

```bash
git push -u origin master
```

 *  后续推送直接使用：

```bash
git push origin master
```



### 取消本地仓库和远程仓库的关联
 * git remote rm origin

### 克隆远程仓库到本地
 * git clone + 远程仓库地址 



### 分支管理

删除远程分支dev

```shell
git push origin --delete dev
```

