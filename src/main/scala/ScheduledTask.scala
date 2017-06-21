case class ScheduledTask[T](task: () => T, timestamp: Long)
