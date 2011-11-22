package gooose

/** Duck-duck-gooose
 *  a watch service for triggered execution
 * */
class Ddg extends xsbti.AppMain {
  case class Exit(val code: Int) extends xsbti.Exit
  def run(config: xsbti.AppConfiguration) = {
    val tap = config.arguments match {
      case Array("-w", path, cmd:_*) =>
        Goose.tap(path) { chged =>
          println(sys.process.Process(cmd)!!)
        }
      case Array("-c", conf) =>
        Goose.collect(PathConfig(conf :: Nil)).goose
      case _ => Goose.local.goose
    }
    Threads.fold({
      tap.start
    },{
      tap.stop
    })
    Exit(0)
  }
}
