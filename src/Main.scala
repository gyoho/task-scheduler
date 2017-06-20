import java.util.concurrent.{ExecutorService, Executors, PriorityBlockingQueue}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

object Main {

  object TimeOrder extends Ordering[ScheduledTask] {
    override def compare(x: ScheduledTask, y: ScheduledTask): Int = x.timestamp compare y.timestamp
  }

  val NUM_OF_THREAD = 5
  val SECOND_IN_MILLI = 1000

  def main(args: Array[String]): Unit = {

    val ex = Executors.newFixedThreadPool(NUM_OF_THREAD)
    implicit val ec = ExecutionContext.fromExecutorService(ex)

    val size: Int = Try(args(0)).map(_.toInt).getOrElse(100)
    val minHeap = new PriorityBlockingQueue[ScheduledTask](size, TimeOrder)
    val scheduler = new SchedulerImp(minHeap, ec)

//    scheduler.start()  e.g if start is not wrapped by future, this will block next part

    for (gap <- 0 to SECOND_IN_MILLI * 10 by SECOND_IN_MILLI) {
        Future {
          val threadId = Thread.currentThread.getId
          scheduler.schedule(() => println(s"Thread # = $threadId, Gap = $gap"), System.currentTimeMillis() + gap)
        }
    }

    scheduler.start()
  }
}
