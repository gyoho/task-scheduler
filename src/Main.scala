import java.util.concurrent.PriorityBlockingQueue
import scala.concurrent.ExecutionContext.Implicits.global

object Main {
  object TimeOrder extends Ordering[(Task, Long)] {
    def compare(x:(Task, Long), y:(Task, Long)): Int = y._2 compare x._2
  }

  def main(args: Array[String]): Unit = {
    val size = args(0).toInt
    val minHeap: PriorityBlockingQueue[(Task, Long)] = new PriorityBlockingQueue[(Task, Long)](size, TimeOrder)

    // create thread pool

  }




}
