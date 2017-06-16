import java.util.concurrent.Future

import scala.concurrent.ExecutionContext

trait Scheduler {
  def schedule[T](task: () => T, timestamp: Long): Future[T]
  def start()(implicit ec: ExecutionContext): Unit
}
