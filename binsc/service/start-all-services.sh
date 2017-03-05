echo '...Starting ZooKeeper...'
which zookeeper-server-start.sh
nohup zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties &

sleep 1m

echo '...Starting Kafka...'
which kafka-server-start.sh
nohup kafka-server-start.sh $KAFKA_HOME/config/server.properties & 

sleep 1m

echo '...Starting Flask Server...'
nohup flask run &

echo '...End..'
