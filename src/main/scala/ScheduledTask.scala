import scala.concurrent.Promise

case class ScheduledTask[T : Manifest](task: () => T, timestamp: Long, promise: Promise[T])
