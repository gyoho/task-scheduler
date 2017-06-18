import java.util.concurrent.{Executors, PriorityBlockingQueue}

import scala.concurrent.ExecutionContext
import scala.util.Try

object Main {

  def main(args: Array[String]): Unit = {
    val size: Int = Try(args(0)).map(_.toInt).getOrElse(100)
    val minHeap = new PriorityBlockingQueue[ScheduledTask](size)

    implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(10))

    /**
      * defining an anonymous class that extends the trait and instantiating it at the same time
      */
    val scheduler = new Scheduler {
      // or minHeap is better to be in this scope
      // so it'll be destroyed with this scheduler
      override def schedule(task: Executable, timestamp: Long): Unit = {
        minHeap.offer(ScheduledTask(task, timestamp))
      }

      override def start()(implicit ec: ExecutionContext): Unit = {
        // with the implicit ExecutionContext, this will be executed by the 10 threads?
        while (true) {
          if (minHeap.peek().timestamp < System.currentTimeMillis()) {
            val nextTask = minHeap.poll()
            nextTask.task.execute()
          }
        }
      }
    }

//    val scheduler = new SchedulerImp(minHeap)

    scheduler.start()
  }
}
