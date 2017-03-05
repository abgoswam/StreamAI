# Dev Install Home (Tools)
export DEV_INSTALL_HOME=~

# Pipeline Home
export STREAMAI_HOME=$DEV_INSTALL_HOME/streamai

# Config Home
export CONFIG_HOME=$STREAMAI_HOME/config

# Scripts Home
export SCRIPTS_HOME=$STREAMAI_HOME/binsc

# Logs Home (where log data from apps is written)
export LOGS_HOME=$STREAMAI_HOME/logs

# Java Home
export JAVA_HOME=/usr/lib/jvm/java-8-oracle
export JRE_HOME=$JAVA_HOME/jre
export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH

# Scripts Home
export PATH=$SCRIPTS_HOME/service:$SCRIPTS_HOME/util:$PATH

# Kafka
export KAFKA_HOME=$DEV_INSTALL_HOME/kafka_2.11-0.10.1.0
export PATH=$KAFKA_HOME/bin:$PATH

# ZooKeeper
export ZOOKEEPER_HOME=$KAFKA_HOME
export PATH=$ZOOKEEPER_HOME/bin:$PATH

# Flask
export FLASK_APP=$STREAMAI_HOME/python/flask-producer.py
export LC_ALL=C.UTF-8
export LANG=C.UTF-8

