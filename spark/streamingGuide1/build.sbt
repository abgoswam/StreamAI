name := "streamingGuide1"

version := "1.0"

scalaVersion := "2.11.8"

// https://mvnrepository.com/artifact/org.apache.spark/spark-streaming_2.11
libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.0.1"

// https://mvnrepository.com/artifact/org.apache.spark/spark-sql_2.11
libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.0.1"

// https://mvnrepository.com/artifact/org.apache.spark/spark-mllib_2.11
libraryDependencies += "org.apache.spark" % "spark-mllib_2.11" % "2.0.1"

//libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.0.1"

//libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-10_2.11" % "2.0.1"

libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-8-assembly_2.11" % "2.0.1"

//libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-10-assembly_2.11" % "2.0.1"

//libraryDependencies += "com.github.benfradet" %% "spark-kafka-0-8-writer" % "0.2.0"