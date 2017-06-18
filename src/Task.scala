
sealed trait Executable {
  /**
    * Structural Recursion using Pattern Matching
    */
  def execute(): Result = this match {
    case UpperCaseTask(s) => Success(s.toUpperCase)
    case AreaCalucTask(r) => Success(r * r * Math.PI)
    case JsonParserTask(_) => Failure("not supported")
  }
}


case class UpperCaseTask(str: String) extends Executable
case class JsonParserTask(radius: String) extends Executable
case class AreaCalucTask(payload: Double) extends Executable


case class ScheduledTask(task: Executable, timestamp: Long)


/**
  * Use abstract class
  */
//abstract class Task[T](content: T) extends Executable
//case class UpperCaseTask(content: String) extends Task[String](content)
//case class JsonParserTask(content: String) extends Task[String](content)
//case class AreaCalucTask(content: Double) extends Task[Double](content)



/**
  * Whenever we throw an exception we lose type safety as there is nothing in the type system that will remind us
  * to deal with the error. It would be much better to return some kind of result that encodes we can succeed or failure
  */
sealed trait Result
final case class Success[T](result: T) extends Result
final case class Failure(reason: String) extends Result


/**
  * How to implement Java interface in scala
  */
//class MyComparator[T] extends Comparator[T] {
//  override def compare(o1: T, o2: T): Int = ???
//}