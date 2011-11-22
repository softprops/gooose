package gooose

import java.io.File

trait Config {
  def triggers: Seq[Trigger]
}

trait FileParser {
  def parse(path: String) = new File(path) match {
    case f if(f.exists) =>
      io.Source.fromFile(path).getLines().map { l =>
        l.split(""":""") match {
          case Array(in, time, cmd) =>
            PathTrigger(in.trim, Some(Interval.seconds(time.toInt)), cmd.trim)
          case Array(in, cmd) =>
            PathTrigger(in.trim, None, cmd.trim)
          case line => sys.error("invalid line format %s" format line.toList)
        }
      }.toSeq
    case _ => sys.error("%s does not exist" format path)
  }
}

object LocalConfig extends Config with FileParser {
  def triggers =
    parse(new File(System.getProperty("user.home"), ".gooose").getPath)
}

case class PathConfig(paths: Seq[String]) extends Config with FileParser {
  def triggers = (Seq.empty[Trigger] /: paths)((a,e) =>
    a ++ parse(e)
  )
}
