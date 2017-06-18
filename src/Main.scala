import java.util.concurrent.{Executors, PriorityBlockingQueue}

import scala.concurrent.ExecutionContext
import scala.util.Try

object Main {

  object TimeOrder extends Ordering[ScheduledTask] {
    override def compare(x: ScheduledTask, y: ScheduledTask): Int = x.timestamp compare y.timestamp
  }

  val SECOND_IN_MILLI = 1000

  def main(args: Array[String]): Unit = {

    implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(10))

    val scheduler = new Scheduler {
      val size: Int = Try(args(0)).map(_.toInt).getOrElse(100)
      // Java's PriorityQueue is MIN heap by default
      val minHeap = new PriorityBlockingQueue[ScheduledTask](size, TimeOrder)

      override def schedule(task: () => Unit, timestamp: Long): Unit = {
        minHeap.offer(ScheduledTask(task, timestamp))
      }

      override def start()(implicit ec: ExecutionContext): Unit = {
        while(true) {
          if (minHeap.peek().timestamp < System.currentTimeMillis()) {
            val nextTask = minHeap.poll()
            nextTask.task()
          }        }
      }
    }


    for (gap <- 0 to SECOND_IN_MILLI * 10 by SECOND_IN_MILLI) {
      scheduler.schedule(() => println(gap), System.currentTimeMillis() + gap)
    }

    scheduler.start()
  }
}
