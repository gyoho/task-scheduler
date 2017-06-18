import java.util.concurrent.{Future, PriorityBlockingQueue}

import scala.concurrent.ExecutionContext

trait Scheduler {
  /**
    * should include the queue as a property like class?
    */
  //  val minHeap: PriorityBlockingQueue[(Task, Long)]
  def schedule(task: Executable[_], timestamp: Long): Unit
  def start()(implicit ec: ExecutionContext): Unit
}

/**
  * Choose among trait, object, class, or case class?
  */
class SchedulerImp(val minHeap: PriorityBlockingQueue[Executable]) extends Scheduler {
  override def schedule(task: Executable[_], timestamp: Long): Unit = ???
  override def start()(implicit ec: ExecutionContext): Unit = ???
}
