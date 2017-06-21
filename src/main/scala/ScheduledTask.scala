
/**
  * Created by yaopeng.wu on 6/20/17.
  */
case class ScheduledTask(task: () => Unit, timestamp: Long = System.currentTimeMillis() + 1000)
