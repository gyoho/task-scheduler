case class ScheduledTask(task: () => Unit, timestamp: Long = System.currentTimeMillis() + 1000)

sealed trait Result
final case class Success[T](result: T) extends Result
final case class Failure(reason: String) extends Result

