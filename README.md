# TweetApp

A real time tweet analysis application

# Requirement
- Zookeeper
- MySQL
- Kafka
- Druid

# Setup local Druid cluster

##### Zookeepr
Follow official instruction to install Zookeeper

##### MySQL
1. Download and install MySQL community edition

2. Create druid user and druid database with the following statements
	```SQL
	GRANT ALL ON druid.* TO 'druid'@'localhost' IDENTIFIED BY 'diurd';
	CREATE DATABASE druid DEFAULT CHARACTER SET utf8;
	```
