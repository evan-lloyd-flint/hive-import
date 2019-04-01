package hive_import

import org.apache.spark._
import org.apache.spark.sql._
import org.apache.spark.sql.types.{StructType, StructField, IntegerType, StringType}
import org.apache.spark.sql.{SparkSession}

object spark_hive_import {

  def main (args: Array[String]): Unit = {



    val spark = SparkSession
      .builder
      .master("local")
      .appName("HDFS to Hive Using Spark")
      .enableHiveSupport()
      .getOrCreate()

    val eventsSchema = StructType(List(
      StructField("patient_id", IntegerType, true),
      StructField("event_id", StringType, true),
      StructField("event_description", StringType, true),
      StructField("timestamp", StringType, true),
      StructField("value", StringType, true)
    ))

    val df = spark.read.schema(eventsSchema).csv("/user/maria_dev/med_input/events.csv")

    spark.sql("CREATE TABLE events_spark (PatientID int, EventID string, EventDescription string, TimeStamp string, Value string) USING hive")

    df.write.format("orc").saveAsTable("events_spark")

    spark.stop()
  }

}