/**
  * Created by abgoswam on 3/11/17.
  */

import org.apache.spark.SparkConf
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.regression.StreamingLinearRegressionWithSGD
import org.apache.spark.streaming.{Seconds, StreamingContext}

object LinearReg {
  def main(args: Array[String]): Unit = {

    // Create a local StreamingContext with two working thread and batch interval of 1 second.
    // The master requires 2 cores to prevent from a starvation scenario.

    val conf = new SparkConf().setMaster("local[*]").setAppName("LinearReg")
    val ssc = new StreamingContext(conf, Seconds(60))

    val trainingData = ssc.textFileStream("trainDir").map(LabeledPoint.parse).cache()
    val testData = ssc.textFileStream("trainDir").map(LabeledPoint.parse)

    val numFeatures = 3
    val model = new StreamingLinearRegressionWithSGD()
      .setInitialWeights(Vectors.zeros(numFeatures))

    // model.predictOnValues(testData.map(lp => (lp.label, lp.features))).print()
    model.predictOnValues(testData.map(lp => (-1, lp.features))).print()
    model.trainOn(trainingData)

    ssc.start()
    ssc.awaitTermination()
  }
}
