import java.util.concurrent.PriorityBlockingQueue

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.Try

class SchedulerImp(
    val minHeap: PriorityBlockingQueue[ScheduledTask[_]]
) extends Scheduler {

  @volatile var running: Boolean = false

  val taskRunner = new {
    @volatile var notified: Boolean = false

    def run()(implicit ec: ExecutionContext): Unit = {
      while (running) {
        // put and take are blocking
        val nextTask = minHeap.take()
        val interval = nextTask.timestamp - System.currentTimeMillis
        if (interval <= 0) {
          Future {
            val p = nextTask.promise.asInstanceOf[Promise[Any]]
            p.complete(Try(nextTask.task()))
          }
        } else {
          minHeap.put(nextTask)
          this.synchronized {
            if (!notified) {
              this.wait(interval)
            }
          }
          notified = false
        }
      }
    }
  }

  override def schedule[T](task: () => T, timestamp: Long): Future[T] = {
    val promise = Promise[T]()

    minHeap.offer(ScheduledTask(task, timestamp, promise))

    if (!minHeap.isEmpty) {
      val nextTask = minHeap.peek()

      if (timestamp < nextTask.timestamp) {
        taskRunner.synchronized {
          taskRunner.notified = true
          taskRunner.notify()
        }
      }
    }

    promise.future
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
