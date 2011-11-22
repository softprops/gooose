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

  def collect(c: Config) = new {
    def goose = Taps.listen(c.triggers)
  }

  def local = LocalGoose

  def main(a: Array[String]) {
    val tap =
      a match {
        case Array("-l") =>
          Goose.local.goose
        case Array("-c", conf) =>
          Goose.collect(PathConfig(conf :: Nil)).goose
        case Array(path) =>
          val t = new java.io.File(path)
          t.createNewFile
          Goose.tap(t.getPath) { println }.goose
        case _ => sys.error("usage: path/to/file")
      }

    Threads.fold({
      tap.start
    }, {
      tap.stop
    })
  }
}
