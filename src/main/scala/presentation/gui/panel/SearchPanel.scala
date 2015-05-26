package main.scala.presentation.gui.panel

import scala.swing.{Button, FlowPanel, TextField}

/**
 * Created by mhbackes on 25/05/15.
 */
class SearchPanel(textField: TextField, button: Button)
  extends FlowPanel(FlowPanel.Alignment.Left)() {
  contents += textField
  contents += button
}
