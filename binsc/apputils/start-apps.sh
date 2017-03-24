#!/bin/bash

echo '...Starting Visualizer App...'
nodejs $STREAMAI_HOME/web/pubnub-rickshaw-realtime/test.js 2>&1 1>$LOGS_HOME/web/kafka-predictor-app/visualizer.log &
echo echo '...logs for visualizer available with "tail -f $STREAMAI_HOME/logs/web/kafka-predictor-app/visualizer.log"'

sleep 10s

echo '...Starting Spark Streaming App...'
start-spark-streaming.sh

sleep 30s

echo '...Starting Data Producer...'
start-data-producer.sh

sleep 30s

echo '...End..'
