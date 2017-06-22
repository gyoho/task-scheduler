import scala.concurrent.Promise

case class ScheduledTask[T](task: () => T, timestamp: Long, promise: Promise[T])
