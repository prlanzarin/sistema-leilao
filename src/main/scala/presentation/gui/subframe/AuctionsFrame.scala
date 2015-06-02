package main.scala.presentation.gui.subframe

import business.entities.Auction
import main.scala.presentation.gui.subframe.manager.IndebtedPropertiesFrame
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

  val headers = Seq("Leilão", "Bem", "Tipo", "Lance Mínimo", "Maior Lance", "Lances", "Início", "Término")
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
      val value = auction.highestBid map (_.value)
    Array(auction.auctionID.get, auction.property.name, auction.property.kind, auction.property.value,
        value.getOrElse("Nenhum"), auction.numberOfBids.getOrElse(0), Validator.dateTimeFormatter.
            format(auction.begin), Validator.dateTimeFormatter.format(auction.end))
  }

  def rowToAuctionId(row: Int) : Long = {
    table(row, 0).toString.toLong
  }

  def getPropertyKindFilter: Option[String] = {
    val sel = propertyKind.selected.get
    if (sel == all) None else Some(sel.text)
  }
}
