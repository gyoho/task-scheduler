import scala.concurrent.ExecutionContext

//type Task = () => Unit

trait Scheduler {
  def schedule(task: () => Unit, timestamp: Long): Unit
  def start()(implicit ec: ExecutionContext): Unit
}