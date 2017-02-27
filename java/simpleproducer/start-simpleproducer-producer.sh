echo '...Building Producer App to pump data to Kafka '

mvn package

echo '...Starting Producer App...'

echo $STREAMAI_HOME/logs/java/simpleproducer/producer.logs

mvn exec:java -Dexec.mainClass="SimpleProducer" -Dexec.args="teststreamai1" 2>&1 1>$STREAMAI_HOME/logs/java/simpleproducer/producer.log &

echo echo '...logs available with "tail -f $STREAMAI_HOME/logs/java/simpleproducer/producer.log"'


