import scala.concurrent.ExecutionContext

/**
  * Created by yaopeng.wu on 6/20/17.
  */
trait Scheduler {
  def schedule(task: => Unit, timestamp: Long): Unit
  def start()(implicit ec: ExecutionContext): Unit
  def stop(): Unit
}
