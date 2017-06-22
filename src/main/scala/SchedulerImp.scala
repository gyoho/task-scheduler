import java.util.concurrent.PriorityBlockingQueue

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.reflect.runtime.universe._
import scala.util.Try

class SchedulerImp(
    val minHeap: PriorityBlockingQueue[ScheduledTask[_]]
) extends Scheduler {

  @volatile var running: Boolean = false

  val taskRunner = new {
    def run[T]()(implicit ec: ExecutionContext, tag: TypeTag[T]): Unit = {
      while (running) {
        // put and take are blocking
        val nextTask = minHeap.take().asInstanceOf[ScheduledTask[tag.type ]]
        val interval = nextTask.timestamp - System.currentTimeMillis
        if (interval <= 0) {
          Future {
            nextTask.promise.complete(Try(nextTask.task()))
          }
        } else {
          minHeap.put(nextTask)
          this.synchronized {
            this.wait(interval)
          }
        }
      }
    }
  }

  override def schedule[T](task: () => T, timestamp: Long)(implicit tag: TypeTag[T]): Future[T] = {
    val promise = Promise[T]()

    minHeap.offer(ScheduledTask(task, timestamp, promise))
    taskRunner.synchronized {
      taskRunner.notify()
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
