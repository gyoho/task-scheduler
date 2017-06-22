import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.runtime.universe._

trait Scheduler {
  def schedule[T](task: () => T, timestamp: Long)(implicit tag: TypeTag[T]): Future[T]
  def start()(implicit ec: ExecutionContext): Unit
  def stop(): Unit
}
