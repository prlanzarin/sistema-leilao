package main.scala.presentation.gui.panel

import scala.swing.{Label, FlowPanel, TextField}

/**
 * Created by mhbackes on 25/05/15.
 */
class LabelTextFieldPanel(label: String, textField: TextField)
  extends FlowPanel(FlowPanel.Alignment.Right)() {
  contents += new Label(label)
  contents += textField
}










