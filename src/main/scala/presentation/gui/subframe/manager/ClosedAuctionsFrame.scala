package main.scala.presentation.gui.subframe.manager

import business.entities.{Auction, Manager}
import main.scala.presentation.controller.Connection
import main.scala.presentation.gui.panel.{ButtonsPanel, LabelRadioButtonsPanel, LabelSearchPanel}
import main.scala.presentation.gui.subframe.{AuctionsFrame, ChildFrame}
import main.scala.presentation.gui.table.SortableTable
import main.scala.presentation.gui.validator.Validator

import scala.swing._

/**
 * Created by mhbackes on 30/05/15.
 */
class ClosedAuctionsFrame(parent: Frame, manager: Manager) extends AuctionsFrame(parent) {
  title = "Leilões Fechados"
  resizable = false

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
  updateAuctionTable

  def updateAuctionTable: Unit = {
    val propertyKind = getPropertyKindFilter
    val propertyName = if (searchText.text.isEmpty) None else Some(searchText.text)
    val auctionList = Connection.sendQueryOpenedAuctionsRequest(propertyName, propertyKind)
    rowData = new Array[Array[Any]](auctionList.size)
    auctionList.zipWithIndex.foreach { case (x, i) => rowData(i) = auctionToRow(x) }
    table = new SortableTable(rowData, headers)
    scrollTable.contents = table
  }

  def allAuctionsReportAction: Unit ={
    //TODO all auctions report code here
  }

  def auctionReportAction: Unit = {
    //TODO auction report code here
  }

}
