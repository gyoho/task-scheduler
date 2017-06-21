import java.util.concurrent.PriorityBlockingQueue

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by yaopeng.wu on 6/20/17.
  */
class SchedulerImp(
    val minHeap: PriorityBlockingQueue[ScheduledTask]
) extends Scheduler {

  @volatile var running = false

  override def schedule(task: () => Unit, timestamp: Long): Unit = {
    minHeap.offer(ScheduledTask(task, timestamp))
  }

  override def start()(implicit ec: ExecutionContext): Unit = {
    if (!running) {
      running = true
      Future {
        while (running) {
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
  }

  override def stop(): Unit = {
    running = false
  }
}
