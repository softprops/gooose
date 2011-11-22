package gooose

case class Inotap(triggers: Seq[Trigger]) extends Tap {
  import net.contentobjects.jnotify.{ JNotify, JNotifyListener }

  val when = JNotify.FILE_RENAMED | JNotify.FILE_DELETED | JNotify.FILE_CREATED | JNotify.FILE_MODIFIED

  val wids = triggers.map { t =>
    JNotify.addWatch(t.input, when, false, new JNotifyListener {

      def handle(path: String) = t match {
        case PathTrigger(_, _, cmd) => exec(cmd, t.input)
        case FuncTrigger(_, _, cmd) => cmd(t.input)
        case t => sys.error("unknown trigger type %s" format t)
      }

      def exec(cmd: String, dir: String) = try {
        println(scala.sys.process.Process(cmd)!!)
      } catch {
        case e => e.printStackTrace
      }

      def fileRenamed(wd: Int, rootPath: String, oldName: String, newName: String) {
        handle(newName)
      }

      def fileModified(wd: Int, rootPath: String, name: String) {
        handle(name)
      }

      def fileDeleted(wd: Int, rootPath: String, name: String) {
        handle(name)
      }

      def fileCreated(wd: Int, rootPath: String, name: String) {
        handle(name)
      }
    })
  }

  def start = wids
  def stop  = wids.foreach(JNotify.removeWatch)
}
