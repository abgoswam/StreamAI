echo '...Building Spark Streaming App '

sbt compile

sbt "run-main KafkaWordCount" 2>&1 1>$STREAMAI_HOME/logs/spark/sparkstreaming101/kafkawordcount.log &

echo echo '...logs available with "tail -f $STREAMAI_HOME/logs/spark/sparkstreaming101/kafkawordcount.log"'


