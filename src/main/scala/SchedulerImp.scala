import java.util.concurrent.PriorityBlockingQueue

import scala.concurrent.{ExecutionContext, Future}

class SchedulerImp(
    val minHeap: PriorityBlockingQueue[ScheduledTask]
) extends Scheduler {

  @volatile var running: Boolean = false

  val taskRunner = new {
    def run()(implicit ec: ExecutionContext): Unit = {
      while (running) {
        // put and take are blocking
        val nextTask = minHeap.take()
        val executionTime = nextTask.timestamp
        if (executionTime < System.currentTimeMillis) {
          Future {
            nextTask.task()
          }
        } else {
          minHeap.put(nextTask)
          this.wait(executionTime)
        }
      }
    }
  }

  override def schedule(task: () => Unit, timestamp: Long): Unit = {
    minHeap.offer(ScheduledTask(task, timestamp))
    taskRunner.notify()
  }

  override def start()(implicit ec: ExecutionContext): Unit = {
    if (!running) {
      running = true
      Future {
        taskRunner.run()
      }
    }
  }

  override def stop(): Unit = {
    running = false
  }
}
