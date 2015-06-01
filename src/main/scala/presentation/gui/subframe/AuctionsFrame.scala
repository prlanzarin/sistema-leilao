package main.scala.presentation.gui.subframe

import business.entities.Auction
import main.scala.presentation.gui.table.SortableTable
import main.scala.presentation.gui.validator.Validator

import scala.swing._
import scala.swing.event.ButtonClicked

/**
 * Created by mhbackes on 01/06/15.
 */
abstract class AuctionsFrame(parent: Frame) extends ChildFrame(parent){
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

  listenTo(all, royalty, jewel, vehicle, other)
  reactions += {
    case ButtonClicked(_) => updateAuctionTable
  }
  updateAuctionTable

  def updateAuctionTable

  def auctionToRow(auction: Auction): Array[Any] = {
    Array(auction.property.name, auction.property.kind, auction.highestBid.get.value, auction.numberOfBids.get,
      Validator.dateFormatter.format(auction.begin), Validator.dateFormatter.format(auction.end))
  }

  def getPropertyKindFilter: Option[String] = {
    val sel = propertyKind.selected.get
    if (sel == all)
      None
    else
      Some(sel.text)
  }
}
