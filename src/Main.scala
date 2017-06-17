import java.util.concurrent.{Executors, PriorityBlockingQueue}
import scala.concurrent.{ExecutionContext, Future}

object Main {
  object TimeOrder extends Ordering[(Task, Long)] {
    override def compare(x: (Task, Long), y: (Task, Long)): Int = y._2 compare x._2
  }

  def main(args: Array[String]): Unit = {
    val size: Int = Option(args(0)).map(_.toInt).getOrElse(100)
    val minHeap = new PriorityBlockingQueue[(Task, Long)](size, TimeOrder)

    // create thread pool
    implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(10))

    // defining an anonymous class that extends the trait and instantiating it at the same time
    val scheduler = new Scheduler {
      // or minHeap is better to be in this scope
      // so it'll be destroyed with this scheduler
      override def schedule[T](task: Task, timestamp: Long): Future[T] = {
        minHeap.offer((task, timestamp))
      }

      override def start()(implicit ec: ExecutionContext): Unit = {
        // with the implicit ExecutionContext, this will be executed by the 10 threads?
        if (minHeap.peek()._2 > System.currentTimeMillis()) {
          val task = minHeap.poll()._1
          task.execute()
        }
      }
    }

//    val scheduler = new SchedulerImp(minHeap)

    scheduler.start()
  }
}
