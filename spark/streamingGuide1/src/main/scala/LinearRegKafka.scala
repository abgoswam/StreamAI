import java.util

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, StreamingLinearRegressionWithSGD}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka._
import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

//// replace by kafka08 if you're using Kafka 0.8
//import com.github.benfradet.spark.kafka08.writer._
//import org.apache.kafka.common.serialization.StringSerializer
//import org.apache.kafka.clients.producer.ProducerRecord

/**
  * Created by abgoswam on 3/12/17.
  */
object LinearRegKafka {
  def main(args: Array[String]): Unit = {
    // Create a local StreamingContext with two working thread and batch interval of 1 second.
    // The master requires 2 cores to prevent from a starvation scenario.

    val conf = new SparkConf().setMaster("local[*]").setAppName("LinearReg")
    val ssc = new StreamingContext(conf, Seconds(10))

    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)

//    val trainingData = ssc.textFileStream("trainDir").map(LabeledPoint.parse).cache()
//    val testData = ssc.textFileStream("trainDir").map(LabeledPoint.parse)

    val topicMap = "teststreamai1".split(",").map((_, "5".toInt)).toMap
    val lines = KafkaUtils.createStream(ssc, "localhost:2181", "testgrp", topicMap).map(_._2)
    // lines.print()

    val data = lines.map(LabeledPoint.parse)
    val numFeatures = 11
    val model = new StreamingLinearRegressionWithSGD()
      .setInitialWeights(Vectors.zeros(numFeatures))

//    model.predictOnValues(testData.map(lp => (lp.label, lp.features))).print()
//    model.predictOnValues(testData.map(lp => (-1, lp.features))).print()
//    model.trainOn(trainingData)

    val predictionDStream = model.predictOnValues(data.map(lp => (lp.label, lp.features)))
    predictionDStream.print()

//    val topic = "teststreamai2"
//    val producerConfig = {
//      val p = new Properties()
//      p.setProperty("bootstrap.servers", "127.0.0.1:9092")
//      p.setProperty("key.serializer", classOf[StringSerializer].getName)
//      p.setProperty("value.serializer", classOf[StringSerializer].getName)
//      p
//    }
//
//    predictionDStream.writeToKafka(
//      producerConfig,
//      s => new ProducerRecord(topic, "abhishek")
//    )

    val kafkaBrokers = "localhost:9092"
    predictionDStream.foreachRDD( rdd => {
        System.out.println("# events = " + rdd.count())

        rdd.foreachPartition(partition => {
          // Print statements in this section are shown in the executor's stdout logs
          val kafkaOpTopic = "teststreamai2"

          val props = new util.HashMap[String, Object]()
          props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers)
          props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
          props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

          val producer = new KafkaProducer[String, String](props)
          partition.foreach(record => {
            val data = record.toString
            // As as debugging technique, users can write to DBFS to verify that records are being written out
            // dbutils.fs.put("/tmp/test_kafka_output",data,true)
            val message = new ProducerRecord[String, String](kafkaOpTopic, null, data)
            producer.send(message)
          })
          producer.close()
        })

      })

//    wordCountStream.foreachRDD( rdd => {
//      System.out.println("# events = " + rdd.count())
//
//      rdd.foreachPartition(partition => {
//        // Print statements in this section are shown in the executor's stdout logs
//        val kafkaOpTopic = "test-output"
//        val props = new HashMap[String, Object]()
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers)
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
//          "org.apache.kafka.common.serialization.StringSerializer")
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
//          "org.apache.kafka.common.serialization.StringSerializer")
//
//        val producer = new KafkaProducer[String, String](props)
//        partition.foreach(record => {
//          val data = record.toString
//          // As as debugging technique, users can write to DBFS to verify that records are being written out
//          // dbutils.fs.put("/tmp/test_kafka_output",data,true)
//          val message = new ProducerRecord[String, String](kafkaOpTopic, null, data)
//          producer.send(message)
//        })
//        producer.close()
//      })
//
//    })

    model.trainOn(data)

    ssc.start()
    ssc.awaitTermination()
  }
}
