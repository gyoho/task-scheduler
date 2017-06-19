import java.util.concurrent.{ExecutorService, Executors, PriorityBlockingQueue}

import scala.concurrent.ExecutionContext
import scala.util.Try

object Main {

  object TimeOrder extends Ordering[ScheduledTask] {
    override def compare(x: ScheduledTask, y: ScheduledTask): Int = x.timestamp compare y.timestamp
  }

  def main(args: Array[String]): Unit = {

    implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))

    val size: Int = Try(args(0)).map(_.toInt).getOrElse(100)
    // Java's PriorityQueue is MIN heap by default
    val minHeap = new PriorityBlockingQueue[ScheduledTask](size, TimeOrder)

    val scheduler = new SchedulerImp(minHeap)

    val SECOND_IN_MILLI = 1000
    val exec: ExecutorService = Executors.newFixedThreadPool(5)

    for (gap <- 0 to SECOND_IN_MILLI * 10 by SECOND_IN_MILLI) {
      exec.execute(new Runnable() {
        override def run(): Unit = {
          val threadId = Thread.currentThread.getId
          scheduler.schedule(() => println(s"Thread # = $threadId, Gap = $gap"), System.currentTimeMillis() + gap)
        }
      })
    }

    scheduler.start()
  }
}
