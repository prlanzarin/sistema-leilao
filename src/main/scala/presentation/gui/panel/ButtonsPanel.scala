package main.scala.presentation.gui.panel

import scala.swing.{Button, FlowPanel}

/**
 * Created by mhbackes on 25/05/15.
 */
class ButtonsPanel(buttons: List[Button])
  extends FlowPanel(FlowPanel.Alignment.Left)() {
  buttons.foreach(b => contents += b)
}
