
sealed trait Task {
  def execute
}

case class UpperCase(str: String) extends Task
case class Calculate(num: Numeric) extends Task

