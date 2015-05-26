package main.scala.presentation.gui.subframe

import business.entities.{Indebted, Manager}
import main.scala.presentation.controller.Connection
import main.scala.presentation.gui.panel.ButtonsPanel
import main.scala.presentation.gui.table.SortableTable

import scala.swing._

/**
 * Created by mhbackes on 26/05/15.
 */
class IndebtedsFrame(parent: Frame, manager: Manager) extends ChildFrame(parent) {
  title = "Endividados"
  resizable = false

  val headers = Seq("Endividado", "Data de Nascimento", "CPF", "Dívida")
  var rowData: Array[Array[Any]] = Array()
  var table: SortableTable = new SortableTable(rowData, headers)
  val scrollTable: ScrollPane = new ScrollPane(table)
  val newIndebted = new Button {
    action = Action("Novo Endividado") {
      newIndebtedAction
    }
  }
  val allIndebtedsReport = new Button("Relatório de Todos Endividados")
  val indebtedReport = new Button("Relatório do Endividado")
  val properties = new Button {
    action = Action("Bens") {
      propertiesAction
    }
  }
  updateIndebtedTable
  contents = new BorderPanel {
    layout(scrollTable) = BorderPanel.Position.Center
    layout(new ButtonsPanel(
      List(newIndebted, allIndebtedsReport, indebtedReport, properties))) = BorderPanel.Position.South
  }

  def updateIndebtedTable = {
    val indebtedList = Connection.sendQueryIndebtedsRequest
    rowData = new Array[Array[Any]](indebtedList.size)
    indebtedList.zipWithIndex.foreach { case (x, i) => rowData(i) = indebtedToCells(x) }
    table = new SortableTable(rowData, headers)
    scrollTable.contents = table
  }
  def indebtedToCells(indebted: Indebted): Array[Any] = {
    Array(indebted.name, indebted.birthDay, indebted.cpf, indebted.debt)
  }

  def newIndebtedAction = {
    //    visible = false
    //    new NewIndebtedFrame(this)
  }

  def propertiesAction = {
    //    visible = false
    //    new IndebtedPropertiesFrame(this)
  }
}
