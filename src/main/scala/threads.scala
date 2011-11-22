package gooose

object Threads {
  def fold(start: => Unit, stop: => Unit) = {
    Thread.currentThread.getName() match {
      case "main" => 
        start
      case _ =>
        start
        def awaitInput {
          try { Thread.sleep(1000) } catch { case _: InterruptedException => () }
          if(System.in.available() <= 0) awaitInput
        }
        awaitInput
        stop
    }
  }
}
