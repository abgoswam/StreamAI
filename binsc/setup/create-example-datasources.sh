#!/bin/bash

echo '...**** YOU MUST START ALL SERVICES BEFORE RUNNING THIS SCRIPT ****...'
echo '...**** IGNORE ANY ERRORS RELATED TO THINGS THAT ALREADY EXIST.  THIS IS OK. ****...'

echo '...Creating Example Kafka Topics...'
kafka-topics.sh --zookeeper localhost:2181 --delete --topic teststreamai1
kafka-topics.sh --zookeeper localhost:2181 --create --topic teststreamai1 --partitions 1 --replication-factor 1
