import java.util.concurrent.{Future, PriorityBlockingQueue}

import scala.concurrent.ExecutionContext

trait Scheduler {
  /**
    * should include the queue as a property like class?
    */
  //  val minHeap: PriorityBlockingQueue[(Task, Long)]
  def schedule[T](task: () => T, timestamp: Long): Future[T]
  def start()(implicit ec: ExecutionContext): Unit
}

/**
  * Choose among trait, object, class, or case class?
  */
class SchedulerImp(val minHeap: PriorityBlockingQueue[(Task, Long)]) extends Scheduler {
  override def schedule[T](task: () => T, timestamp: Long): Future[T] = ???
  override def start()(implicit ec: ExecutionContext): Unit = ???
}
