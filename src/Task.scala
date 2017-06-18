/**
  * Structural Recursion using Pattern Matching
  */
sealed trait Executable {
  def execute(): Result[T] = this match {
    case Task(contents, _) => contents match {
      case str: String => Success(str.toUpperCase)
      case num: Int => Success(num + 10)
    }
//    case Task[String](str) => Success(str.toUpperCase)
//    case Task[Int](num) => Success(num + 10)
  }
}

abstract class Task[T](contents: T, timestamp: Long) extends Executable
case class JsonParserTask(contents: String, timestamp: Long) extends Task[String](contents, timestamp)
case class PiCalucTask(contents: Int, timestamp: Long) extends Task[Double](contents, timestamp)

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