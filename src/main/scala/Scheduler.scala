import scala.concurrent.{ExecutionContext, Future}

trait Scheduler {
  def schedule[T](task: () => T, timestamp: Long): Future[T]
  def start()(implicit ec: ExecutionContext): Unit
  def stop(): Unit
}
