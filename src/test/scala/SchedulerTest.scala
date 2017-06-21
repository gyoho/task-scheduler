import java.util.concurrent.{ExecutorService, Executors, PriorityBlockingQueue}

import org.scalatest.concurrent.Eventually
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, Matchers, WordSpec}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

class SchedulerTest extends WordSpec with Matchers with Eventually with BeforeAndAfterAll with BeforeAndAfter {

  object TimeOrder extends Ordering[ScheduledTask] {
    override def compare(x: ScheduledTask, y: ScheduledTask): Int = x.timestamp compare y.timestamp
  }

  val NUM_OF_THREAD = 5
  val QUEUE_SIZE = 100
  val SECOND_IN_MILLI = 1000

  val ex: ExecutorService = Executors.newFixedThreadPool(NUM_OF_THREAD)
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(ex)

  /**
    * scalatest BeforeAndAfterAll provides setUpModule/tearDownModule
    * this runs before/after this file execution
    */

  override def beforeAll(): Unit = {

  }

  override def afterAll(): Unit = {
    ex.shutdown()
    super.afterAll()
  }

  /**
    * scalatest BeforeAndAfter provides setUp/tearDown
    * this runs before/after EACH test
    */

  before {
    // runs before each test
  }

  after {
    // runs after each test
  }

  "The scheduler" should {
    "run task on schedule" in {
      val minHeap = new PriorityBlockingQueue[ScheduledTask](QUEUE_SIZE, TimeOrder)
      val scheduler = new SchedulerImp(minHeap)
      scheduler.start()  // if start is not wrapped by future, this will block the code below

      @volatile var executedAt: Long = 0
      val executionTime = System.currentTimeMillis() + SECOND_IN_MILLI // run 1 second in the future
      scheduler.schedule(() => {executedAt = System.currentTimeMillis()}, executionTime)

      /**
        * scalatest.concurrent.Eventually provides timeout for executing a task to prevent infinite loop job
        * if not finished within the time window, eventually will throw "TestFailedDueToTimeoutException"
        * Use scala.concurrent.duration._ for specifying units (seconds, minutes, etc)
        */
      eventually(timeout(10.seconds)) {
        executedAt should not be 0
      }

      executedAt shouldBe executionTime +- 200

      println(s"Scheduled to run at $executionTime but actually ran at $executedAt")

      scheduler.stop()
    }
  }
}
