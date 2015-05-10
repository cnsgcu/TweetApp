# TweetApp

A real time tweet analysis application.

![Alt text](https://raw.githubusercontent.com/cnsgcu/TweetApp/master/screenshot/screenshot.png "Screenshot")

# Dependencies

- Zookeeper
- MySQL
- Apache Kafka
- Apache Cassandra 1.2
- Druid

# Setup Druid's local cluster

Download and install [Apache Kafka](http://kafka.apache.org/downloads.html), [MySQL](http://dev.mysql.com/downloads/mysql/) community server, [Apache Cassandra 1.2](http://www.apache.org/dyn/closer.cgi?path=/cassandra/1.2.19/apache-cassandra-1.2.19-bin.tar.gz), and [Druid](http://druid.io/downloads.html).

#### I. Apache Zookeeper

We are going to use Zookeeper that comes with Apache Kafka. Zookeeper server startup script is located under *\<KAFKA_DIR\>/bin* folder.
To start Zookeeper execute the following command.

> bin/zookeeper-server-start.sh config/zookeeper.properties 

#### II. MySQL

1. Install MySQL community server edition.

2. Execute the following statements to create *druid* user and *druid* database
	```SQL
	GRANT ALL ON druid.* TO 'druid'@'localhost' IDENTIFIED BY 'diurd';
	CREATE DATABASE druid DEFAULT CHARACTER SET utf8;
	```

#### III. Apache Kafka

1. Start Kafka server
    ```Shell 
    bin/kafka-server-start.sh config/server.properties
    ```

2. Create *tweet* and *tweet_point* topics
> bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 192 --topic tweet
>
> bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 192 --topic tweet_point

#### IV. Apache Cassandra

Segments and their metadata are stored in Cassandra in two tables: *index_storage* and *descriptor_storage*. Execute below statements to create the tables.
```SQL
CREATE TABLE index_storage(key text,
                           chunk text,
                           value blob,
                           PRIMARY KEY (key, chunk)) WITH COMPACT STORAGE;

CREATE TABLE descriptor_storage(key varchar,
                                lastModified timestamp,
                                descriptor varchar,
                                PRIMARY KEY (key)) WITH COMPACT STORAGE;
```

#### V. Druid

Use the following configuration to setup Cassandra as deep storage.

**Common configuration - common.runtime.properties**

```properties
druid.extensions.coordinates=["io.druid.extensions:druid-examples","io.druid.extensions:druid-kafka-eight","io.druid.extensions:mysql-metadata-storage", "io.druid.extensions:druid-cassandra-storage:0.7.1.1"]

druid.zk.service.host=localhost

druid.metadata.storage.type=mysql
druid.metadata.storage.connector.connectURI=jdbc\:mysql\://localhost\:3306/druid
druid.metadata.storage.connector.user=druid
druid.metadata.storage.connector.password=diurd

druid.storage.type=c*
druid.storage.host=localhost:9160
druid.storage.keyspace=druid

druid.cache.type=local
druid.cache.sizeInBytes=10000000

druid.selectors.indexing.serviceName=overlord

druid.emitter=noop
```

**Realtime node config - runtime.properties**
```properties
druid.service=realtime

druid.processing.buffer.sizeBytes=100000000
druid.processing.numThreads=1
```

**Historical node configuration - runtime.properties**
```properties
druid.service=historical

druid.processing.buffer.sizeBytes=100000000
druid.processing.numThreads=1

druid.segmentCache.locations=[{"path": "/tmp/druid/indexCache", "maxSize"\: 10000000000}]
druid.server.maxSize=10000000000
```
#### VI. Bootstrapping

1. Start coordinator node
> java -Xmx256m -Duser.timezone=UTC -Dfile.encoding=UTF-8 -classpath config/_common:config/coordinator:lib/* io.druid.cli.Main server coordinator

2. Start historical node
> java -Xmx256m -Duser.timezone=UTC -Dfile.encoding=UTF-8 -classpath config/_common:config/historical:lib/* io.druid.cli.Main server historical

3. Start broker node
> java -Xmx256m -Duser.timezone=UTC -Dfile.encoding=UTF-8 -classpath config/_common:config/broker:lib/* io.druid.cli.Main server broker

4. Start realtime node
> java -Xmx512m -Duser.timezone=UTC -Dfile.encoding=UTF-8 -Ddruid.realtime.specFile=twitter.spec -classpath config/_common:config/realtime:lib/* io.druid.cli.Main server realtime

#### VII. Testing

1. Use the following command to verify broker node is running
> curl -XPOST -H'Content-type: application/json' "http://localhost:8082/druid/v2/?pretty" -d'{"queryType":"timeBoundary","dataSource":"twitter"}'

2. Use the following command to verify  historical node is running
> curl -XPOST -H'Content-type: application/json' "http://localhost:8083/druid/v2/?pretty" -d'{"queryType":"timeBoundary","dataSource":"twitter"}'

3. Use the following command to verify  real-time node is running
> curl -XPOST -H'Content-type: application/json' "http://localhost:8084/druid/v2/?pretty" -d'{"queryType":"timeBoundary","dataSource":"twitter"}'
