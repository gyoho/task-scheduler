import java.util.concurrent.PriorityBlockingQueue

import scala.concurrent.ExecutionContext

//type Task = () => Unit

trait Scheduler {
  def schedule(task: () => Unit, timestamp: Long): Unit
  def start()(implicit ec: ExecutionContext): Unit
}


class SchedulerImp(val minHeap: PriorityBlockingQueue[ScheduledTask]) extends Scheduler {

  override def schedule(task: () => Unit, timestamp: Long): Unit = {
    minHeap.offer(ScheduledTask(task, timestamp))
  }

  override def start()(implicit ec: ExecutionContext): Unit = {
    while(true) {
      // put and take are blocking
      val nextTask = minHeap.take()
      if (nextTask.timestamp < System.currentTimeMillis) nextTask.task() else minHeap.put(nextTask)
    }
  }
}

case class ScheduledTask(task: () => Unit, timestamp: Long = System.currentTimeMillis() + 1000)
