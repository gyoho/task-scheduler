/**
  * Structural Recursion using Pattern Matching
  */
sealed trait Task {
  def execute(): Result = this match {
    case StringTask(str) => StringResult(str.toUpperCase)
    case NumericTask(num) => NumericResult(num + 10)
  }
}

// common type for Int, Double, and Float?
case class NumericTask(num: Int) extends Task
case class StringTask(str: String) extends Task

/**
  * Whenever we throw an exception we lose type safety as there is nothing in the type system that will remind us
  * to deal with the error. It would be much better to return some kind of result that encodes we can succeed or failure
  */
sealed trait Result
case class StringResult(str: String) extends Result
case class NumericResult(num: Int) extends Result



/**
  * How to implement Java interface in scala
  */
//class MyComparator[T] extends Comparator[T] {
//  override def compare(o1: T, o2: T): Int = ???
//}