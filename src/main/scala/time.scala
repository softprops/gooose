package gooose

case class Interval(delay: Int, unit: java.util.concurrent.TimeUnit)
object Interval {
  import java.util.concurrent.TimeUnit
  def seconds(n: Int) = Interval(n, TimeUnit.SECONDS)
  def minutes(n: Int) = Interval(n, TimeUnit.MINUTES)
  def hours(n: Int) = Interval(n, TimeUnit.HOURS)
}

