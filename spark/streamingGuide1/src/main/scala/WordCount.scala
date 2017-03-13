/**
  * Created by abgoswam on 3/8/17.
  */

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.sql.SparkSession

object WordCount {
  def main(args: Array[String]): Unit = {

    // Create a local StreamingContext with two working thread and batch interval of 1 second.
    // The master requires 2 cores to prevent from a starvation scenario.

    val conf = new SparkConf().setMaster("local[2]").setAppName("WordCount")
    val ssc = new StreamingContext(conf, Seconds(10))

    // Create a DStream that will connect to hostname:port, like localhost:9999
    val lines = ssc.socketTextStream("localhost", 9999)
    // lines.print()

    // Split each line into words
    val words = lines.flatMap(_.split(" "))
    val wc = words.count()
    // words.print()
    wc.print()

    words.foreachRDD { rdd =>

      // Get the singleton instance of SparkSession
      val spark =  SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
      import spark.implicits._

      // Convert RDD[String] to DataFrame
      val wordsDataFrame = rdd.toDF("word")

      // Create a temporary view
      wordsDataFrame.createOrReplaceTempView("words")

      // Do word count on DataFrame using SQL and print it
      val wordCountsDataFrame = spark.sql("select word, count(*) as total from words group by word")
      wordCountsDataFrame.show()
    }

    // Count each word in each batch
    val pairs = words.map(word => (word, 1))
    // pairs.print()

    val wordCounts = pairs.reduceByKey(_ + _)

    // Print the first ten elements of each RDD generated in this DStream to the console
    // wordCounts.print()

    ssc.start()             // Start the computation
    ssc.awaitTermination()  // Wait for the computation to terminate
  }
}
