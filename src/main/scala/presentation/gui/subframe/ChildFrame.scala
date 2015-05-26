package main.scala.presentation.gui.subframe

import scala.swing.Frame

/**
 * Created by mhbackes on 25/05/15.
 */
class ChildFrame(parent: Frame) extends Frame {
  location = parent.location
  visible = true

  override def closeOperation = {
    parent.visible = true
    parent.location = location
    super.closeOperation
    dispose
  }
}
