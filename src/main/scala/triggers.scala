package gooose

/** Triggers define a input descriptor,
 *  an ammount of time to watch in between
 *  input tests, and a cmd to execute when
 *  and input test passes */
sealed trait Trigger {
  def input: String
  def interval: Option[Interval]
  def cmd: String => Any
}

case class PathTrigger(
  input: String, interval: Option[Interval], outcmd: String
) extends Trigger {
  def cmd = { str =>
     outcmd
  }
}

case class FuncTrigger(
  input: String, interval: Option[Interval], trigger: String => Unit
) extends Trigger {
  def cmd = { str =>
    trigger(str)
    str
  }
}
