/**
  * Structural Recursion using Pattern Matching
  */
sealed trait Executable[T] {

  def content: T

  def execute(): Result[_] = this match {
    case UpperCaseTask(str) => Success(str.toUpperCase)
    case AreaCalucTask(radius) => Success(radius * radius * Math.PI)
    case JsonParserTask(payload) => ???
  }
}

case class UpperCaseTask(content: String) extends Executable[String]
case class JsonParserTask(content: String) extends Executable[String]
case class AreaCalucTask(content: Double) extends Executable[Double]


case class ScheduledTask[T](task: Executable[T], timestamp: Long)


/**
  * Use abstract class
  */
//abstract class Task[T](content: T) extends Executable
//case class JsonParserTask(content: String) extends Task[String](content, timestamp)
//case class PiCalucTask(content: Int) extends Task[Double](content, timestamp)



/**
  * Whenever we throw an exception we lose type safety as there is nothing in the type system that will remind us
  * to deal with the error. It would be much better to return some kind of result that encodes we can succeed or failure
  */
sealed trait Result[T]
final case class Success[T](result: T) extends Result[T]
final case class Failure[T](reason: String) extends Result[T]


/**
  * How to implement Java interface in scala
  */
//class MyComparator[T] extends Comparator[T] {
//  override def compare(o1: T, o2: T): Int = ???
//}