package com.flink.watermark

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

/**
  * @author xy
  * @date ：Created in 2020/11/6 16:26
  * @desc：waterMark
  */
object WatermarkDemo {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //监听 linux服务器 s100 7777端口
    val dataStream = env.socketTextStream("192.168.1.100",7777)
    import org.apache.flink.streaming.api.scala._

    val sss = dataStream.flatMap(_.split(" ")).filter(_.nonEmpty).map((_,1)).keyBy(0)
      //统计5秒钟之内的数据
      //.timeWindow(Time.seconds(10)).sum(1)
      // 滑动窗口 5s 窗口间距 15s 移动距离
      //    .timeWindow(Time.seconds(5),Time.seconds(10)).sum(1)
      // 滚动窗口 当统计数量达到5个 就开始输出
      .countWindow(5).sum(1)
    sss.print().setParallelism(1)
    // 这一句必须要 ,启动executor ,执行任务
    env.execute("stream word count")
  }

}
