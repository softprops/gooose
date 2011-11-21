package gooose

case class PollTap(triggers: Seq[Trigger]) extends gooose.Tap {
  import java.util.concurrent.Executors
  import java.util.concurrent.TimeUnit
  import java.io.File

  case class History(t: Trigger) extends Runnable {
    var mtime: Option[Long] = None
    def run {
      val mod = new File(t.input).lastModified
      mtime match {
        case Some(before) =>            
          if(before != mod) t match {
            case PathTrigger(_, _, cmd) => exec(cmd, t.input)
            case FuncTrigger(_, _, cmd) => cmd(t.input)
            case t => sys.error("unknown trigger type %s" format t)
          }            
        case _ => () // duck, duck, ...
      }
      mtime = Some(mod)
    }

    def exec(cmd: String, dir: String) = try {
      println(scala.sys.process.Process(cmd)!!)
    } catch {
      case e => e.printStackTrace
    }
  }

  private lazy val sched = Executors.newSingleThreadScheduledExecutor()
  private lazy val futures = triggers.map { t =>
    def configure(del: Int, unit: java.util.concurrent.TimeUnit) =
      sched.scheduleAtFixedRate(History(t), 0, del, unit)
    t.interval match {
      case Some(Interval(del, unit)) =>
        configure(del, unit)
      case _ =>
        configure(1, TimeUnit.SECONDS)
    }
  }

  // tap interface
  def start = futures

  // tap interval
  def stop = {
    futures.foreach(_.cancel(true))
    sched.shutdownNow
  }
}
