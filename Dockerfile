############################################################
# Dockerfile to build MongoDB container images
# Based on Ubuntu
############################################################
#
#
#
# Set the base image to Ubuntu
FROM ubuntu:16.04

# File Author / Maintainer
MAINTAINER Example McAuthor

WORKDIR /root

# Update the repository sources list
RUN apt-get update

ENV \ 
 SCALA_MAJOR_VERSION=2.11 \
 SBT_VERSION=0.13.9 \
 KAFKA_CLIENT_VERSION=0.10.1.0 

RUN \
 apt-get update \
 && apt-get install -y software-properties-common \
 && add-apt-repository ppa:webupd8team/java \
 && apt-get update \
 && echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections \
 && apt-get install -y oracle-java8-installer \
 && apt-get install -y oracle-java8-set-default \
 && apt-get install -y curl \
 && apt-get install -y wget \
 && apt-get install -y vim \
 && apt-get install -y git \
 && apt-get install -y openssh-server \
 && apt-get install -y apache2 \
 && apt-get install -y libssl-dev

RUN \
# Maven for custom builds
 apt-get install -y maven 

RUN \
# Get Latest StreamAI Code
 cd ~ \
 && git clone https://github.com/abgoswam/streamai 

RUN \
# Sbt
 cd ~ \
 && wget https://dl.bintray.com/sbt/native-packages/sbt/${SBT_VERSION}/sbt-${SBT_VERSION}.tgz \
 && tar xvzf sbt-${SBT_VERSION}.tgz \
 && rm sbt-${SBT_VERSION}.tgz \
 && ln -s /root/sbt/bin/sbt /usr/local/bin \
# Sbt Clean - This seems weird, but it triggers the full Sbt install which involves a lot of external downloads
 && sbt clean clean-files 


RUN \
# Apache Kafka 
 cd ~ \
 && wget http://apache.claz.org/kafka/${KAFKA_CLIENT_VERSION}/kafka_${SCALA_MAJOR_VERSION}-${KAFKA_CLIENT_VERSION}.tgz \
 && tar -xvzf kafka_${SCALA_MAJOR_VERSION}-${KAFKA_CLIENT_VERSION}.tgz  \
 && rm kafka_${SCALA_MAJOR_VERSION}-${KAFKA_CLIENT_VERSION}.tgz


 
