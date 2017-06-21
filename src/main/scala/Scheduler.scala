import scala.concurrent.ExecutionContext

trait Scheduler {
  def schedule(task: => Unit, timestamp: Long): Unit
  def start()(implicit ec: ExecutionContext): Unit
  def stop(): Unit
}
