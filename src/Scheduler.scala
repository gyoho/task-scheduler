import java.util.concurrent.{Executors, PriorityBlockingQueue}

import scala.concurrent.{ExecutionContext, Future}

trait Scheduler {
  def schedule[T](task: () => T, timestamp: Long): Future[T]
  def start()(implicit ec: ExecutionContext): Unit
}


class SchedulerImp(val minHeap: PriorityBlockingQueue[ScheduledTask[_]]) extends Scheduler {

  // ExecutionContext for Future
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))

  override def schedule[T](task: () => T, timestamp: Long): Future[T] = {
    minHeap.offer(ScheduledTask[T](task, timestamp))
    Future {
      task()
    }
  }

  override def start()(implicit ec: ExecutionContext): Unit = {
    while(true) {
      // put and take are blocking
      val nextTask = minHeap.take()
      if (nextTask.timestamp < System.currentTimeMillis) nextTask.task() else minHeap.put(nextTask)
    }
  }
}

case class ScheduledTask[T](task: () => T, timestamp: Long = System.currentTimeMillis() + 1000)
