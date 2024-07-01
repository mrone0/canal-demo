canal同步数据库到Es
前景：本文操作一切以docker工具进行操作
1. 数据库重要知识
1. 数据库的重要日志功能之binlog
2. binlog日志的使用与开启
3. binlog日志的作用
1.1 binlog是什么
简单介绍一下，可以自己系统学习或者让我出教程！
MySQL 的 Binlog 日志是一种二进制格式的日志，Binlog 记录所有的 DDL 和 DML 语句(除了数据查询语句SELECT、SHOW等)，以 Event 的形式记录，同时记录语句执行时间。
什么意思呢，binlog将任意除查询以外的DDL，DML以事件形式记录，并记录事件发生的时间，上图：
以下图片为当前数据库所存在的所有binlog日志文件

以下图为当前binlog日志文件名为000001所发生的所有事件

可以看到在Info栏中存在有begin commit开头和结束的关键字，学过mysql的同学都不会陌生吧，这是事务的开启与提交关键字。
当然，binlog日志文件是以2进制的形式写入的，普通文件是无法查看的，需要用mysqlbinlog特殊语法进行解析，或者查看事件进行分析。
1.2 binlog日志的开启
编辑mysql.conf，在文件的最下面加上
创建mysql
docker pull mysql:5.7.36
docker run --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=admin -d mysql:5.7.36
docker exec -it mysql /bin/bash
apt-get update
apt-get install vim
cd /etc/mysql/mysql.conf.d
vim mysqld.cnf

log-bin=mysql-bin  // 开启logbin
binlog-format=ROW  // binlog日志格式
server-id=1  // mysql主从备份serverId,canal中不能与此相同1

1.3 binlog日志的作用
以上我们介绍到binlog日志能够记录DDL，DML语句的操作，并以事件形式记录，以2进制形式写入文件中，那么我们根据事件的时间顺序分析其binlog日志文件，不就能知道数据库发生了那些操作吗？数据库的主从同步也是如此实现的，从数据库与主数据通过tcp建立连接，从数据库开启IO线程读取主数据生产的binlog日志文件，分析到本地并开启sql线程进行分析和数据同步。
三个线程：首先是主数据库在写入binlog的IO线程，以及从数据库所开启的读IO和sql线程
2.canal
1. canal是什么
2. canal的工作原理
3. 使用canal
2.1 canal
canal是阿里巴巴旗下的一款开源项目，纯Java开发。基于数据库增量日志解析，提供增量数据订阅&消费，目前主要支持MySQL；
通过canal我们可以实现:
1. 数据同步
2. 重要sql监控
2.2 原理
由上述所说，canal是通过解析数据库增量日志，提供数据的订阅与消费，简单理解就算，canal其实是java版本的数据库从库，为我们的服务提供消息订阅并消费，从而实现数据库增量操作进行同步或者监控。
2.3使用canal
docker：
docker pull canal/canal-server:v1.1.5
docker run --name canal -p 11111:11111  --link mysql:mysql -id canal/canal-server:v1.1.5
docker exec -it canal /bin/bash
cd canal-server/conf/example/
vi instance.properties  // 修改配置
# 把0改成10，只要不和mysql的id相同就行
canal.instance.mysql.slaveId=10
# 修改成mysql对应的账号密码，mysql就是mysql镜像的链接别名
canal.instance.master.address=mysql:3306
canal.instance.dbUsername=root
canal.instance.dbPassword=admin



cd canal-server/conf/
vi canal.properties 
canal.serverMode = rabbitMQ
rabbitmq.host = 192.168.174.131:5672
rabbitmq.virtual.host = /
rabbitmq.exchange = mysql
rabbitmq.username = admin
rabbitmq.password = admin
rabbitmq.deliveryMode =
instance.properties修改

canal.properties修改
注：在修改时，需要先手动创建好mq交换机

