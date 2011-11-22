package gooose

/** A tap starts monitoring */
trait Tap {
  def start: Unit
  def stop: Unit
}

object Taps {
  def listen(ts: Seq[Trigger]): Tap =
    try {
      if(Seq("linux", "windows", "mac os x").find(System.getProperty("os.name").toLowerCase.equals).isDefined) Inotap(ts)
      else PollTap(ts)
    } catch {
      case _ => PollTap(ts)
    }
}
