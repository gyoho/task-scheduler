import scala.concurrent.Promise
import scala.reflect.runtime.universe._

case class ScheduledTask[T](task: () => T, timestamp: Long, promise: Promise[T])(implicit tag: TypeTag[T])
