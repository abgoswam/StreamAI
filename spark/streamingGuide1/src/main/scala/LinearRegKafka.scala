import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, StreamingLinearRegressionWithSGD}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka._

/**
  * Created by abgoswam on 3/12/17.
  */
object LinearRegKafka {
  def main(args: Array[String]): Unit = {
    // Create a local StreamingContext with two working thread and batch interval of 1 second.
    // The master requires 2 cores to prevent from a starvation scenario.

    val conf = new SparkConf().setMaster("local[*]").setAppName("LinearReg")
    val ssc = new StreamingContext(conf, Seconds(30))

    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)

//    val trainingData = ssc.textFileStream("trainDir").map(LabeledPoint.parse).cache()
//    val testData = ssc.textFileStream("trainDir").map(LabeledPoint.parse)

    val topicMap = "teststreamai1".split(",").map((_, "5".toInt)).toMap
    val lines = KafkaUtils.createStream(ssc, "localhost:2181", "testgrp", topicMap).map(_._2)

//    lines.print()

    val data = lines.map(LabeledPoint.parse)
    val numFeatures = 3
    val model = new StreamingLinearRegressionWithSGD()
      .setInitialWeights(Vectors.zeros(numFeatures))

//    model.predictOnValues(testData.map(lp => (lp.label, lp.features))).print()
//    model.predictOnValues(testData.map(lp => (-1, lp.features))).print()
//    model.trainOn(trainingData)

    model.predictOnValues(data.map(lp => (-1, lp.features))).print()
    model.trainOn(data)

    ssc.start()
    ssc.awaitTermination()
  }
}
