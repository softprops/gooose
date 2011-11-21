package gooose

/** Duck-duck-gooose
 *  a watch service for triggered execution
 * */
class Ddg extends xsbti.AppMain {
  case class Exit(val code: Int) extends xsbti.Exit
  def run(config: xsbti.AppConfiguration) = {
    println(config.arguments)
    Exit(0)
  }
}
