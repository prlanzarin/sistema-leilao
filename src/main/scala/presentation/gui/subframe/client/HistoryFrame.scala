package main.scala.presentation.gui.subframe.client

import business.entities.{Bid, Auction, Client}
import main.scala.presentation.controller.Connection
import main.scala.presentation.gui.panel.{LabelSearchPanel, ButtonsPanel, LabelRadioButtonsPanel}
import main.scala.presentation.gui.subframe.ChildFrame
import main.scala.presentation.gui.table.SortableTable

import scala.swing._

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

  val headers = Seq("Bem", "Tipo", "Lance Dado", "Maior Lance", "Lances", "Início", "Término")
  var rowData: Array[Array[Any]] = Array()
  var table = new SortableTable(rowData, headers)
  val scrollTable = new ScrollPane(table)

  val allAuctionsReport = new Button{
    action = Action("Relatório de Todos Leilões"){

    }
  }
  val auctionReport = new Button{
    action = Action("Relatório do Leilão"){

    }
  }
  val payment = new Button{
    action = Action("Solicitar Pagamento"){

    }
  }
  val cancelBid = new Button{
    action = Action("Cancelar Lance"){

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

  def getPropertyKindFilter: Option[String] = {
    val sel = propertyKind.selected.get
    if (sel == all)
      None
    else
      Some(sel.text)
  }

  def updateAuctionBidTable: Unit = {
    val propertyKind = getPropertyKindFilter
    val propertyName = searchText.text
    val auctionList = Connection.sendQueryAuctionHistoryRequest(client, propertyName, propertyKind)
    rowData = new Array[Array[Any]](auctionList.size)
    auctionList.zipWithIndex.foreach { case ((x, y), i) => rowData(i) = auctionBidToRow(x,y) }
    table = new SortableTable(rowData, headers)
    scrollTable.contents = table
  }

  def auctionBidToRow(auction: Auction, bid: Bid): Array[Any] = {
    new Array[Any](7)
  }

  def allAuctionsReportAction: Unit = {
    //TODO all auctions report code here
  }

  def auctionReportAction: Unit = {
    //TODO auction report code here
  }

  def paymentAction: Unit = {
    //TODO payment action code here
  }

  def cancelBidAction: Unit = {
    //TODO cancel bid action code here
  }
}
