package main.scala.presentation.gui.subframe.manager

import business.entities.{Indebted, Manager}
import main.scala.presentation.controller.Connection
import main.scala.presentation.gui.panel.ButtonsPanel
import main.scala.presentation.gui.subframe.ChildFrame
import main.scala.presentation.gui.table.SortableTable
import main.scala.presentation.gui.validator.Validator

import scala.swing._

/**
 * Created by mhbackes on 26/05/15.
 */
class ReportFrame(parent: Frame, frameTitle: String,  text: String)
    extends ChildFrame(parent) {
    resizable = true
    preferredSize = new Dimension(400, 400)
    title = frameTitle

    val textArea = new TextArea(text)
    contents = new ScrollPane(textArea)
}


