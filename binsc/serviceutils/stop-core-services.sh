#!/bin/bash

echo '...Stopping Kafka...'
kafka-server-stop.sh

sleep 1m

echo '...Stopping ZooKeeper...'
zookeeper-server-stop.sh

sleep 1m

echo '...End..'
