package goose

// todo: make bindings for inotify
case class Inotap(triggers: Set[Trigger]) extends Tap {
  def start = {}
  def stop  = {}
}
