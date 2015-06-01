package main.scala.presentation.gui.subframe.manager

import business.entities.{Auction, Manager}
import main.scala.presentation.controller.Connection
import main.scala.presentation.gui.panel.{ButtonsPanel, LabelRadioButtonsPanel, LabelSearchPanel}
import main.scala.presentation.gui.subframe.ChildFrame
import main.scala.presentation.gui.table.SortableTable

import scala.swing._

/**
 * Created by mhbackes on 30/05/15.
 */
class ClosedAuctionsFrame(parent: Frame, manager: Manager) extends ChildFrame(parent) {
  title = "Leilões Fechados"
  resizable = false

  val all = new RadioButton("Todos") {
    selected = true
  }
  val royalty = new RadioButton("Imóvel")
  val jewel = new RadioButton("Jóia")
  val vehicle = new RadioButton("Veículo")
  val other = new RadioButton("Outro")
  val propertyKind = new ButtonGroup(all, royalty, jewel, vehicle, other)
  val searchText = new TextField(58)
  val searchButton = new Button{
    action = Action("Buscar"){
      updateAuctionTable
    }
  }

  val headers = Seq("Bem", "Tipo", "Maior Lance", "Lances", "Início", "Término")
  var rowData: Array[Array[Any]] = Array()
  var table = new SortableTable(rowData, headers)
  val scrollTable = new ScrollPane(table)

  val allAuctionsReport = new Button{
    action = Action("Relatório de Todos Leilões"){
      allAuctionsReportAction
    }
  }
  val auctionReport = new Button("Relatório do Leilão"){
    auctionReportAction
  }

  contents = new BorderPanel {
    layout(new GridPanel(2, 1) {
      contents += new LabelRadioButtonsPanel("Filtrar Bens:", propertyKind)
      contents += new LabelSearchPanel("Nome do Bem:", searchText, searchButton)
    }) = BorderPanel.Position.North
    layout(new ScrollPane(table)) = BorderPanel.Position.Center
    layout(new ButtonsPanel(List(allAuctionsReport, auctionReport))) = BorderPanel.Position.South
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
    val propertyName = searchText.text
    val auctionList = Connection.sendQueryClosedAuctionsRequest(propertyName, propertyKind)
    rowData = new Array[Array[Any]](auctionList.size)
    auctionList.zipWithIndex.foreach { case (x, i) => rowData(i) = auctionToRow(x) }
    table = new SortableTable(rowData, headers)
    scrollTable.contents = table
  }

  def auctionToRow(auction: Auction): Array[Any] = {
    new Array[Any](6)
  }

  def allAuctionsReportAction: Unit ={
    //TODO all auctions report code here
  }

  def auctionReportAction: Unit = {
    //TODO auction report code here
  }

}