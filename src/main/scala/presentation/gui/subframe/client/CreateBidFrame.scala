package main.scala.presentation.gui.subframe.client

import business.entities.{PropertyKind, Auction, Client}
import main.scala.presentation.controller.Connection
import main.scala.presentation.gui.panel.{LabelRadioButtonsPanel, LabelSearchPanel}
import main.scala.presentation.gui.subframe.ChildFrame
import main.scala.presentation.gui.table.SortableTable

import scala.swing._

/**
 * Created by mhbackes on 30/05/15.
 */
class CreateBidFrame(parent: Frame, client: Client) extends ChildFrame(parent) {
  title = "Novo Lance"
  resizable = false
  val all = new RadioButton("Todos") {
    selected = true
  }
  val royalty = new RadioButton("Imóvel")
  val jewel = new RadioButton("Jóia")
  val vehicle = new RadioButton("Veículo")
  val other = new RadioButton("Outro")
  val propertyKind = new ButtonGroup(all, royalty, jewel, vehicle, other)
  val searchText = new TextField(40)
  val searchButton = new Button {
    action = Action("Buscar") {
      updateAuctionTable
    }
  }

  val headers = Seq("Bem", "Tipo", "Maior Lance", "Lances", "Início", "Término")
  var rowData: Array[Array[Any]] = Array()
  var table = new SortableTable(rowData, headers)
  val scrollTable = new ScrollPane(table)

  val newBitValue = new TextField(37)
  val newBidButton = new Button{
    action = Action("Fazer Lance"){
      createBidAction
    }
  }

  contents = new BorderPanel {
    layout(new GridPanel(2, 1) {
      contents += new LabelRadioButtonsPanel("Filtrar Bens:", propertyKind)
      contents += new LabelSearchPanel("Nome do Bem:", searchText, searchButton)
    }) = BorderPanel.Position.North
    layout(new ScrollPane(table)) = BorderPanel.Position.Center
    layout(new LabelSearchPanel("Valor do Lance:", newBitValue, newBidButton)) = BorderPanel.Position.South
  }

  def getPropertyKindFilter: Option[String] = {
    val sel = propertyKind.selected.get
    if (sel == all)
      None
    else
      Some(sel.text)
  }

  def updateAuctionTable: Unit = {
    val propertyKind = getPropertyKindFilter
    val propertyName = Some(searchText.text) // FIXME OPTION?
    val auctionList = Connection.sendQueryOpenedAuctionsRequest(propertyName, propertyKind)
    rowData = new Array[Array[Any]](auctionList.size)
    auctionList.zipWithIndex.foreach { case (x, i) => rowData(i) = auctionToRow(x) }
    table = new SortableTable(rowData, headers)
    scrollTable.contents = table
  }

  def auctionToRow(auction: Auction): Array[Any] = {
    new Array[Any](6)
  }

  def createBidAction: Unit = {
    //TODO create bid code here
  }
}
