# TweetApp

A real time tweet analysis application

# Dependencies
- Zookeeper
- MySQL
- Apache Kafka
- Druid

# Setup local Druid cluster

Download and install [Apache Kafka](http://kafka.apache.org/downloads.html), [MySQL](http://dev.mysql.com/downloads/mysql/) community server, and [Druid](http://druid.io/downloads.html).

#### I. Apache Zookeeper

We are going to use Zookeeper that comes with Apache Kafka. Zookeeper server startup script is located under *\<KAFKA_DIR\>/bin* folder.
To start Zookeeper execute the following command

> bin/zookeeper-server-start.sh config/zookeeper.properties 

#### II. MySQL server

1. Install MySQL community server edition

2. Execute the following statements to create *druid* user and *druid* database
	```SQL
	GRANT ALL ON druid.* TO 'druid'@'localhost' IDENTIFIED BY 'diurd';
	CREATE DATABASE druid DEFAULT CHARACTER SET utf8;
	```

#### III. Apache Kafka

1. Start Kafka server
> bin/kafka-server-start.sh config/server.properties

2. Create a topic
> bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 192 --topic tweet

#### IV. Druid

common.runtime.properties
```properties
# Extensions (no deep storage model is listed - using local fs for deep storage - not recommended for production)
druid.extensions.coordinates=["io.druid.extensions:druid-examples","io.druid.extensions:druid-kafka-eight","io.druid.extensions:mysql-metadata-storage", "io.druid.extensions:druid-cassandra-storage:0.7.1.1"]

# Zookeeper
druid.zk.service.host=localhost

# Metadata Storage (mysql)
druid.metadata.storage.type=mysql
druid.metadata.storage.connector.connectURI=jdbc\:mysql\://localhost\:3306/druid
druid.metadata.storage.connector.user=druid
druid.metadata.storage.connector.password=diurd

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

Realtime runtime.properties
```properties
druid.service=realtime

# We can only 1 scan segment in parallel with these configs.
# Our intermediate buffer is also very small so longer topNs will be slow.
# In production sizeBytes should be 512mb, and numThreads should be # cores - 1
druid.processing.buffer.sizeBytes=100000000
druid.processing.numThreads=1

# Enable Real monitoring
# druid.monitoring.monitors=["com.metamx.metrics.SysMonitor","com.metamx.metrics.JvmMonitor","io.druid.segment.realtime.RealtimeMetricsMonitor"]
```

Historical runtime.properties
```properties
druid.service=historical

# Our intermediate buffer is also very small so longer topNs will be slow.
# In prod: set sizeBytes = 512mb
druid.processing.buffer.sizeBytes=100000000
# We can only 1 scan segment in parallel with these configs.
# In prod: set numThreads = # cores - 1
druid.processing.numThreads=1

# maxSize should reflect the performance you want.
# Druid memory maps segments.
# memory_for_segments = total_memory - heap_size - (processing.buffer.sizeBytes * (processing.numThreads+1)) - JVM overhead (~1G)
# The greater the memory/disk ratio, the better performance you should see
druid.segmentCache.locations=[{"path": "/tmp/druid/indexCache", "maxSize"\: 10000000000}]
druid.server.maxSize=10000000000
```
1. Start coordinator node
> java -Xmx256m -Duser.timezone=UTC -Dfile.encoding=UTF-8 -classpath config/_common:config/coordinator:lib/* io.druid.cli.Main server coordinator

2. Start historical node
> java -Xmx256m -Duser.timezone=UTC -Dfile.encoding=UTF-8 -classpath config/_common:config/historical:lib/* io.druid.cli.Main server historical

3. Start broker node
> java -Xmx256m -Duser.timezone=UTC -Dfile.encoding=UTF-8 -classpath config/_common:config/broker:lib/* io.druid.cli.Main server broker

4. Start realtime
> java -Xmx512m -Duser.timezone=UTC -Dfile.encoding=UTF-8 -Ddruid.realtime.specFile=twitter.spec -classpath config/_common:config/realtime:lib/* io.druid.cli.Main server realtime