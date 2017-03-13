echo '...Building Spark Streaming App '

sbt compile

sbt "run-main LinearRegKafka" 2>&1 1>$STREAMAI_HOME/logs/spark/streamingguide1/linearregkafka.log &

sleep 1m

echo echo '...logs available with "tail -f $STREAMAI_HOME/logs/spark/streamingguide1/linearregkafka.log"'


