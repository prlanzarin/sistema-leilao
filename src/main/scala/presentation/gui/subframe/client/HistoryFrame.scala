package main.scala.presentation.gui.subframe.client

import business.entities.{Bid, Auction, Client}
import main.scala.presentation.controller.Connection
import main.scala.presentation.gui.panel.{LabelSearchPanel, ButtonsPanel, LabelRadioButtonsPanel}
import main.scala.presentation.gui.subframe.ChildFrame
import main.scala.presentation.gui.table.SortableTable
import main.scala.presentation.gui.validator.Validator

import scala.swing._
import scala.swing.event.ButtonClicked

/**
 * Created by mhbackes on 30/05/15.
 */
class HistoryFrame(parent: Frame, client: Client) extends ChildFrame(parent) {
  title = "Histórico"
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
      updateAuctionBidTable
    }
  }

  val headers = Seq("Leilão", "Bem", "Tipo", "Lance Dado", "Maior Lance", "Lances", "Início", "Término")
  var rowData: Array[Array[Any]] = Array()
  var table = new SortableTable(rowData, headers)
  val scrollTable = new ScrollPane(table)

  val allAuctionsReport = new Button{
    action = Action("Relatório de Todos Leilões"){
      allAuctionsReportAction
    }
  }
  val auctionReport = new Button{
    action = Action("Relatório do Leilão"){
      auctionReportAction
    }
  }
  val payment = new Button{
    action = Action("Solicitar Pagamento"){
      fakeBoletoAction
    }
  }
  val cancelBid = new Button{
    action = Action("Cancelar Lance"){
      cancelBidAction
    }
  }

  contents = new BorderPanel {
    layout(new GridPanel(2, 1) {
      contents += new LabelRadioButtonsPanel("Filtrar Bens:", propertyKind)
      contents += new LabelSearchPanel("Nome do Bem:", searchText, searchButton)
    }) = BorderPanel.Position.North
    layout(scrollTable) = BorderPanel.Position.Center
    layout(new ButtonsPanel(List(allAuctionsReport, auctionReport, payment, cancelBid))) = BorderPanel.Position.South
  }
  updateAuctionBidTable

  listenTo(all, royalty, jewel, vehicle, other)
  reactions += {
    case ButtonClicked(_) => updateAuctionBidTable
  }

  def getPropertyKindFilter: Option[String] = {
    val sel = propertyKind.selected.get
    if (sel == all)
      None
    else
      Some(sel.text)
  }

  def updateAuctionBidTable: Unit = {
    val propertyKind = getPropertyKindFilter
    val propertyName = if (searchText.text.isEmpty) None else Option(searchText.text)
    val auctionBidList = Connection.sendQueryAuctionHistoryRequest(client, propertyName, propertyKind)
    rowData = new Array[Array[Any]](auctionBidList.size)
    auctionBidList.zipWithIndex.foreach { case ((x, y), i) => rowData(i) = auctionBidToRow(x,y) }
    table = new SortableTable(rowData, headers)
    scrollTable.contents = table
  }

  def auctionBidToRow(auction: Auction, bid: Bid): Array[Any] = {
    Array(auction.auctionID.get, auction.property.name, auction.property.kind, bid.value, isHighestBid(auction, client),
    auction.numberOfBids.get, Validator.dateFormatter.format(auction.begin), Validator.dateFormatter.format(auction.end))
  }

  def rowToAuctionId(row: Int): Long = {
    table(row, 0).toString.toLong
  }

  def rowToBidValue(row: Int): Double = {
    table(row, 3).toString.toDouble
  }

  def isHighestBid(auction: Auction, client: Client): String ={
    if (auction.highestBid.get.client.userName == client.userName)
      "Sim"
    else "Não"
  }

  def allAuctionsReportAction: Unit = {
    //TODO all auctions report code here
  }

  def auctionReportAction: Unit = {
    //TODO auction report code here
  }

  def fakeBoletoAction: Unit = {
    //TODO payment action code here
  }

  def cancelBidAction: Unit = {
    val row = table.selection.rows
    if(row.isEmpty)
      Dialog.showMessage(table, "Selecione um lance", "Erro", Dialog.Message.Error)
    else {
      val auctionId = rowToAuctionId(row.anchorIndex)
      val clientId = client.userName
      val value = rowToBidValue(row.anchorIndex)
      if (Connection.sendCancelBidRequest(clientId, auctionId, value))
        Dialog.showMessage(table, "Lance cancelado com sucesso", "Sucesso", Dialog.Message.Info)
      else
        Dialog.showMessage(table, "Lance não pode ser cancelado", "Erro", Dialog.Message.Error)
      updateAuctionBidTable
    }
  }
}
