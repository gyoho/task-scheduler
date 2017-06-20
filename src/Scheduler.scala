import java.util.concurrent.{Executors, PriorityBlockingQueue}

import scala.concurrent.{ExecutionContext, Future}

//type Task = () => Unit

trait Scheduler {
  def schedule(task: () => Unit, timestamp: Long): Unit
  def start()(implicit ec: ExecutionContext): Unit
}

class SchedulerImp(
    val minHeap: PriorityBlockingQueue[ScheduledTask],
    // ExecutionContext for Future to execute a task in worker thread (not calling thread)
    implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))
) extends Scheduler {

  override def schedule(task: () => Unit, timestamp: Long): Unit = {
    minHeap.offer(ScheduledTask(task, timestamp))
  }

  override def start()(implicit ec: ExecutionContext): Unit = {
    while (true) {
      // put and take are blocking
      val nextTask = minHeap.take()
      if (nextTask.timestamp < System.currentTimeMillis) {
        Future {
          nextTask.task()
        }
      } else {
        minHeap.put(nextTask)
        Thread.sleep(100)
      }
    }
  }
}

case class ScheduledTask(task: () => Unit, timestamp: Long = System.currentTimeMillis() + 1000)
