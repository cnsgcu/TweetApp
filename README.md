# TweetApp

A real time tweet analysis application

# Dependencies
- Zookeeper
- MySQL
- Apache Kafka
- Druid

# Setup local Druid cluster

Download and install [Apache Kafka](http://kafka.apache.org/downloads.html), [MySQL](http://dev.mysql.com/downloads/mysql/) community server, and [Druid](http://druid.io/downloads.html).

##### I. Start Apache Zookeeper

We are going to use Zookeeper that comes with Apache Kafka. Zookeeper server startup script is located under *\<KAFKA_DIR\>/bin* folder.
To start Zookeeper execute the following command

> bin/zookeeper-server-start.sh config/zookeeper.properties 

##### II. Configure MySQL

a. Install MySQL community server edition

b. Execute the following statements to create *druid* user and *druid* database
	```SQL
	GRANT ALL ON druid.* TO 'druid'@'localhost' IDENTIFIED BY 'diurd';
	CREATE DATABASE druid DEFAULT CHARACTER SET utf8;
	```

##### III. Apache Kafka

a. Start Kafka server
> bin/kafka-server-start.sh config/server.properties

b. Create a topic
> bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 192 --topic tweet

##### IV. Druid

common.runtime.properties
```Java
# Extensions (no deep storage model is listed - using local fs for deep storage - not recommended for production)
druid.extensions.coordinates=["io.druid.extensions:druid-examples","io.druid.extensions:druid-kafka-eight","io.druid.extensions:mysql-metadata-storage", "io.druid.extensions:druid-cassandra-storage:0.7.1.1"]

# Zookeeper
druid.zk.service.host=localhost

# Metadata Storage (mysql)
druid.metadata.storage.type=mysql
druid.metadata.storage.connector.connectURI=jdbc\:mysql\://localhost\:3306/druid
druid.metadata.storage.connector.user=druid
druid.metadata.storage.connector.password=diurd

# Deep storage (local filesystem for examples - don't use this in production)
#druid.storage.type=local
#druid.storage.storageDirectory=/tmp/druid/localStorage

# Deep storage configuration for HDFS
#druid.storage.type=hdfs
#druid.storage.storageDirectory=hdfs://localhost:9000/user/cuong

# Deep storage configuration for Cassandra
druid.storage.type=c*
druid.storage.host=localhost:9160
druid.storage.keyspace=druid

# Query Cache (we use a simple 10mb heap-based local cache on the broker)
druid.cache.type=local
druid.cache.sizeInBytes=10000000

# Indexing service discovery
druid.selectors.indexing.serviceName=overlord

# Monitoring (disabled for examples, if you enable SysMonitor, make sure to include sigar jar in your cp)
# druid.monitoring.monitors=["com.metamx.metrics.SysMonitor","com.metamx.metrics.JvmMonitor"]

# Metrics logging (disabled for examples - change this to logging or http in production)
druid.emitter=noop
```

a. Start coordinator node
> java -Xmx256m -Duser.timezone=UTC -Dfile.encoding=UTF-8 -classpath config/_common:config/coordinator:lib/* io.druid.cli.Main server coordinator

b. Start historical node
> java -Xmx256m -Duser.timezone=UTC -Dfile.encoding=UTF-8 -classpath config/_common:config/historical:lib/* io.druid.cli.Main server historical

c. Start broker node
> java -Xmx256m -Duser.timezone=UTC -Dfile.encoding=UTF-8 -classpath config/_common:config/broker:lib/* io.druid.cli.Main server broker

d. Start realtime
> java -Xmx512m -Duser.timezone=UTC -Dfile.encoding=UTF-8 -Ddruid.realtime.specFile=twitter.spec -classpath config/_common:config/realtime:lib/* io.druid.cli.Main server realtime