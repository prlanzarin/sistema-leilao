package main.scala.presentation.gui.subframe.manager

import business.entities.{Indebted, Manager, Property, PropertyKind}
import main.scala.presentation.controller.Connection
import main.scala.presentation.gui.panel.ButtonsPanel
import main.scala.presentation.gui.subframe.ChildFrame
import main.scala.presentation.gui.table.SortableTable

import scala.swing._

/**
 * Created by mhbackes on 26/05/15.
 */
class IndebtedPropertiesFrame(parent: Frame, manager: Manager, indebted: Indebted)
  extends ChildFrame(parent) {
  title = "Bens de " + indebted.name
  resizable = false
  val headers = Seq("Bem", "Ano da Compra", "Valor Estimado (R$)", "Tipo")
  var rowData: Array[Array[Any]] = Array()
  var table = new SortableTable(rowData, headers)
  val scrollTable = new ScrollPane(table)
  val newProperty = new Button {
    action = Action("Novo Bem") {
      newPropertyAction
    }
  }
  val newAuction = new Button {
    action = Action("Novo Leilão") {
      newAuctionAction
    }
  }

  updateIndebtedTable
  contents = new BorderPanel {
    layout(scrollTable) = BorderPanel.Position.Center
    layout(new ButtonsPanel(List(newProperty, newAuction))) = BorderPanel.Position.South
  }

  def updateIndebtedTable: Unit = {
    val propertyList = Connection.sendQueryIndebtedPropertiesRequest(indebted.cpf)
    rowData = new Array[Array[Any]](propertyList.size)
    propertyList.zipWithIndex.foreach { case (x, i) => rowData(i) = propertyToRow(x) }
    table = new SortableTable(rowData, headers)
    scrollTable.contents = table
  }

  def propertyToRow(property: Property): Array[Any] = {
    Array(property.name, property.boughtIn, property.value, property.kind)
  }

  def rowToProperty(row: Int): Property = {
    new Property(table(row, 0).toString, table(row,2).toString.toDouble,
      PropertyKind.withName(table(row,3).toString), table(row,1).toString.toInt)
  }

  def newPropertyAction = {
    visible = false
    new RegisterPropertyFrame(this, manager, indebted)
  }

  def newAuctionAction = {
    val row = table.selection.rows
    if (row.isEmpty)
      Dialog.showMessage(table, "Selecione um bem", "Erro", Dialog.Message.Error)
    else {
      val property = rowToProperty(row.anchorIndex)
      visible = false
      new CreateAuctionFrame(this, manager, property)
    }
  }
}


