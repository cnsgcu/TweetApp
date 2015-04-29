# TweetApp

A real time tweet analysis application

# Requirement
- Zookeeper
- MySQL
- Kafka
- Druid

# Setup Instructions
## Zookeepr
Follow official instruction to install Zookeeper

## MySQL
Download and install MySQL community edition

```SQL
	GRANT ALL ON druid.* TO 'druid'@'localhost' IDENTIFIED BY 'diurd';
	CREATE DATABASE druid DEFAULT CHARACTER SET utf8;
```
