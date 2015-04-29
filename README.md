# TweetApp

A real time tweet analysis application

# Dependencies
- Zookeeper
- MySQL
- Apache Kafka
- Druid

# Setup local Druid cluster

Download and install [Apache Kafka](http://kafka.apache.org/downloads.html), [MySQL](http://dev.mysql.com/downloads/mysql/) community server, and [Druid](http://druid.io/downloads.html).

##### I. Apache Zookeeper

We are going to use Zookeeper that comes with Apache Kafka. Zookeeper server startup script is located under *\<KAFKA_DIR\>/bin* folder.
To start Zookeeper execute the following command

> bin/zookeeper-server-start.sh config/zookeeper.properties 

##### II. MySQL

1. Install MySQL community server edition

2. Execute the following statements to create *druid* user and *druid* database
	```SQL
	GRANT ALL ON druid.* TO 'druid'@'localhost' IDENTIFIED BY 'diurd';
	CREATE DATABASE druid DEFAULT CHARACTER SET utf8;
	```

##### III. Apache Kafka
1. Start Kafka server
> bin/kafka-server-start.sh config/server.properties

2. Create a topic
> bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 192 --topic tweet

3. Start Kafka producer
> bin/kafka-console-producer.sh --broker-list localhost:9092 --topic tweet