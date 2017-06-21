case class ScheduledTask(task: () => Unit, timestamp: Long = System.currentTimeMillis() + 1000)
