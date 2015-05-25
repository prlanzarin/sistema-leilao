package main.scala.presentation.gui.panel

import scala.swing.{Label, FlowPanel, ButtonGroup}

/**
 * Created by mhbackes on 25/05/15.
 */
class LabelRadioButtonsPanel(label: String, radios: ButtonGroup)
  extends FlowPanel(FlowPanel.Alignment.Left)() {
  contents += new Label(label)
  radios.buttons.foreach(b => contents += b)
}
