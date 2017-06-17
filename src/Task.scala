/**
  * Structural Recursion using Pattern Matching
  */
sealed trait Executable {
  def execute(): Result[T] = this match {
    case Task(elem) => elem match {
      case str: String => Success(str.toUpperCase)
      case num: Int => Success(num + 10)
    }
//    case Task[String](str) => Success(str.toUpperCase)
//    case Task[Int](num) => Success(num + 10)
  }
}

case class Task[T](elem: T) extends Executable

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