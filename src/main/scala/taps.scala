package gooose

/** A tap starts monitoring */
trait Tap {
  def start: Unit
  def stop: Unit
}

object Taps {
  def listen(ts: Seq[Trigger]): Tap = PollTap(ts)
}
