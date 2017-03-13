# streamai
---------------
Commands to start stack.

git clone https://github.com/abgoswam/streamai

source streamai/config/bash/streamai.bashrc 

streamai/binsc/setup/RUNME_ONCE.sh

start-spark-streaming.sh

start-data-producer.sh

----------------

Other Useful Commands:

sudo docker build -t streamai .
 
sudo docker run --rm -it --net=host streamai

kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic teststreamai1 --from-beginning
