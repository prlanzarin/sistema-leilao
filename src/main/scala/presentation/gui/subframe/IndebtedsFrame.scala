package main.scala.presentation.gui.subframe

import business.entities.{Indebted, Manager}
import main.scala.presentation.controller.Connection
import main.scala.presentation.gui.panel.ButtonsPanel
import main.scala.presentation.gui.table.SortableTable
import main.scala.presentation.gui.validator.Validator

import scala.swing._

/**
 * Created by mhbackes on 26/05/15.
 */
class IndebtedsFrame(parent: Frame, manager: Manager) extends ChildFrame(parent) {
  title = "Endividados"
  resizable = false

  val headers = Seq("Endividado", "Data de Nascimento", "CPF", "Dívida (R$)")
  var rowData: Array[Array[Any]] = Array()
  var table: SortableTable = new SortableTable(rowData, headers)
  val scrollTable: ScrollPane = new ScrollPane(table)
  val registerIndebted = new Button {
    action = Action("Novo Endividado") {
      registerIndebtedAction
    }
  }
  val allIndebtedsReport = new Button("Relatório de Todos Endividados")
  //TODO implement action
  val indebtedReport = new Button("Relatório do Endividado")
  //TODO implement action
  val properties = new Button {
    action = Action("Bens") {
      propertiesAction
    }
  }
  updateIndebtedTable
  contents = new BorderPanel {
    layout(scrollTable) = BorderPanel.Position.Center
    layout(new ButtonsPanel(
      List(registerIndebted, allIndebtedsReport, indebtedReport, properties))) = BorderPanel.Position.South
  }

  def updateIndebtedTable: Unit = {
    val indebtedList = Connection.sendQueryIndebtedsRequest
    rowData = new Array[Array[Any]](indebtedList.size)
    indebtedList.zipWithIndex.foreach { case (x, i) => rowData(i) = indebtedToRow(x) }
    table = new SortableTable(rowData, headers)
    scrollTable.contents = table
  }

  def indebtedToRow(indebted: Indebted): Array[Any] = {
    Array(indebted.name, Validator.dateFormatter format indebted.birthDay, indebted.cpf, indebted.debt)
  }

  def rowToIndebted(row: Int): Indebted = {
    new Indebted(table(row,0).toString, Validator.dateFormatter.parse(table(row,1).toString),
      table(row,3).toString.toDouble, table(row,2).toString)
  }

  def registerIndebtedAction = {
    visible = false
    new RegisterIndebtedFrame(this, manager)
  }

  def propertiesAction: Unit = {
    val row = table.selection.rows
    if (row.isEmpty)
      Dialog.showMessage(properties, "Selecione um endividado", "Erro", Dialog.Message.Error)
    else {
      val indebted = rowToIndebted(row.anchorIndex)
      visible = false
      new IndebtedPropertiesFrame(this, manager, indebted)
    }
  }
}


