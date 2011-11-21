package gooose

object Goose {  

  // a school of gooses
  case class Geese(taps: Seq[FuncTrigger]) {
    def tap(dir: String, interval: Option[Interval] = None)(after: String => Unit) =
      Geese(FuncTrigger(dir, interval, after) +: taps)
    def goose = Taps.listen(taps)
  }

  // starting point for func builder dsl
  def tap(dir: String, interval: Option[Interval] = None)(after: String => Unit) =
    Geese(Seq(FuncTrigger(dir, interval, after)))


  object LocalGoose {
    def goose = Taps.listen(LocalConfig.triggers.toSeq)
  }

  def local = LocalGoose

  def main(a: Array[String]) {
    val tap =
      a match {
      case Array("l") =>
        Goose.local.goose
      case Array(path) =>
        val t = new java.io.File(path)
        t.createNewFile
        Goose.tap(t.getPath) { println }.goose
      case _ => sys.error("usage: path/to/file")
    }

    
    Thread.currentThread.getName() match {
      case "main" => 
        tap.start
      case _ =>
        tap.start
        def awaitInput {
          try { Thread.sleep(1000) } catch { case _: InterruptedException => () }
          if(System.in.available() <= 0) awaitInput
        }
        awaitInput
        tap.stop
    }
  }
}
