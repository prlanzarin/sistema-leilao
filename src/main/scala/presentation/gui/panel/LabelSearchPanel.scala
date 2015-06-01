package main.scala.presentation.gui.panel

import scala.swing.{Label, Button, FlowPanel, TextField}

/**
 * Created by mhbackes on 25/05/15.
 */
class LabelSearchPanel(label: String, textField: TextField, button: Button)
  extends FlowPanel(FlowPanel.Alignment.Left)() {
  contents += new Label(label)
  contents += textField
  contents += button
}
