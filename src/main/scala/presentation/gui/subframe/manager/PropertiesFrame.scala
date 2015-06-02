package main.scala.presentation.gui.subframe.manager

import business.entities.{Manager, Property, PropertyKind}
import main.scala.presentation.controller.Connection
import main.scala.presentation.gui.panel.{ButtonsPanel, LabelRadioButtonsPanel}
import main.scala.presentation.gui.subframe.ChildFrame
import main.scala.presentation.gui.table.SortableTable

import scala.swing._
import scala.swing.event.ButtonClicked

/**
 * Created by mhbackes on 26/05/15.
 */
class PropertiesFrame(parent: Frame, manager: Manager) extends ChildFrame(parent) {
  title = "Bens"
  resizable = false

  val allProperties = new RadioButton("Todos") {
    selected = true
  }
  val royalty = new RadioButton("Imóvel")
  val jewel = new RadioButton("Jóia")
  val vehicle = new RadioButton("Veículo")
  val other = new RadioButton("Outro")
  val propertyKind = new ButtonGroup(allProperties, royalty, jewel, vehicle, other)
  val propertyKindPanel = new LabelRadioButtonsPanel("Tipo:", propertyKind)
  val allAuction = new RadioButton("Todos") {
    selected = true
  }
  val yes = new RadioButton("Sim")
  val no = new RadioButton("Não")
  val inAuction = new ButtonGroup(allAuction, yes, no)
  val inAuctionPanel = new LabelRadioButtonsPanel("Em Leilão:", inAuction)

  val headers = Seq("Bem", "Nome", "Ano da Compra", "Valor Estimado (R$)", "Tipo")
  var rowData: Array[Array[Any]] = Array()
  var table = new SortableTable(rowData, headers)
  val scrollTable = new ScrollPane(table)

  val createAuction = new Button {
    action = Action("Novo Leilão") {
      createAuctionAction
    }
  }

  updatePropertiesTable
  contents = new BorderPanel {
    layout(new GridPanel(2, 1) {
      contents += propertyKindPanel
      contents += inAuctionPanel
    }) = BorderPanel.Position.North
    layout(scrollTable) = BorderPanel.Position.Center
    layout(new ButtonsPanel(List(createAuction))) = BorderPanel.Position.South
  }

  listenTo(allProperties, royalty, jewel, vehicle, other, allAuction, yes, no)
  reactions += {
    case ButtonClicked(_) => updatePropertiesTable
  }

  def getPropertyKindFilter: Option[String] = {
    val sel = propertyKind.selected.get
    if (sel == allProperties)
      None
    else
      Some(sel.text)
  }

  def getInAuctionFilter: Option[Boolean] = {
    val sel = inAuction.selected.get
    if (sel == allAuction)
      None
    else if (sel == yes)
      Some(true)
    else
      Some(false)
  }

  def updatePropertiesTable: Unit = {
    val kind = getPropertyKindFilter
    val inAuction = getInAuctionFilter
    val propertyList = Connection.sendQueryPropertiesRequest(kind, inAuction)
    rowData = new Array[Array[Any]](propertyList.size)
    propertyList.zipWithIndex.foreach { case (x, i) => rowData(i) = propertyToRow(x) }
    table = new SortableTable(rowData, headers)
    scrollTable.contents = table
  }

  def propertyToRow(property: Property): Array[Any] ={
    Array(property.propertyID.get, property.name, property.boughtIn, property.value, property.kind)
  }

  def rowToProperty(row: Int) = {
    new Property(table(row, 1).toString, table(row,3).toString.toDouble,
      PropertyKind.withName(table(row,4).toString), table(row,2).toString.toInt, Some(table(row, 0).toString.toLong))
  }

  def createAuctionAction = {
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
