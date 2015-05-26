package main.scala.presentation.gui.panel

import scala.swing.{FlowPanel, ComboBox, Label}

/**
 * Created by mhbackes on 25/05/15.
 */
class LabelComboBoxPanel(label: String, comboBox: ComboBox[String])
  extends FlowPanel(FlowPanel.Alignment.Right)() {
  contents += new Label(label)
  contents += comboBox
}
