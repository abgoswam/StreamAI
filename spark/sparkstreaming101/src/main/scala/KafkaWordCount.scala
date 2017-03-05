/**
  * Created by abgoswam on 12/6/16.
  */

import java.util.HashMap

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.log4j.Logger
import org.apache.log4j.Level

object KafkaWordCount {
  def main(args: Array[String]) {
//    if (args.length < 4) {
//      System.err.println("Usage: KafkaWordCount <zkQuorum><group> <topics> <numThreads>")
//      System.exit(1)
//    }

//    val Array(zkQuorum, group, topics, numThreads) = ("localhost:2181", "testgrp",  "test", "2")

    val sparkConf = new SparkConf().setAppName("KafkaWordCount").setMaster("local[*]")
    val ssc = new StreamingContext(sparkConf, Seconds(10))
//    ssc.checkpoint("checkpoint")

    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)

//    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val topicMap = "teststreamai1".split(",").map((_, "5".toInt)).toMap
    val lines = KafkaUtils.createStream(ssc, "localhost:2181", "testgrp", topicMap).map(_._2)

    lines.print()

//    val words = lines.flatMap(_.split(" "))
////    val wordCounts = words.map(x => (x, 1L))
////      .reduceByKeyAndWindow(_ + _, _ - _, Seconds(30), Seconds(10), 2)
//
//    words.print()
//
//    val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)
//    wordCounts.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
