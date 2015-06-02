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
class OpenedAuctionsFrame(parent: Frame, manager: Manager) extends AuctionsFrame(parent) {
  title = "Leilões Abertos"
  resizable = false

  val endAuction = new Button{
    action = Action("Encerrar Leilão"){
      endAuctionAction
    }
  }

  contents = new BorderPanel {
    layout(new GridPanel(2, 1) {
      contents += new LabelRadioButtonsPanel("Filtrar Bens:", propertyKind)
      contents += new LabelSearchPanel("Nome do Bem:", searchText, searchButton)
    }) = BorderPanel.Position.North
    layout(scrollTable) = BorderPanel.Position.Center
    layout(new ButtonsPanel(List(endAuction))) = BorderPanel.Position.South
  }

  def endAuctionAction: Unit = {
    val row = table.selection.rows
    if (row.isEmpty)
      Dialog.showMessage(table, "Selecione um leilão", "Erro", Dialog.Message.Error)
    else {
      val auctionId = rowToAuctionId(row.anchorIndex)
      if(Connection.sendEndAuctionRequest(auctionId))
        Dialog.showMessage(table, "Leilão cancelado com sucesso", "Sucesso", Dialog.Message.Info)
      else
        Dialog.showMessage(table, "Leilão não pode ser cancelado", "Erro", Dialog.Message.Error)
      updateAuctionTable
    }
  }



  def updateAuctionTable: Unit = {
    val propertyKind = getPropertyKindFilter
    val propertyName = if (searchText.text.isEmpty) None else Some(searchText.text)
    val auctionList = Connection.sendQueryOpenedAuctionsRequest(propertyName, propertyKind)
    rowData = new Array[Array[Any]](auctionList.size)
    auctionList.zipWithIndex.foreach { case (x, i) => rowData(i) = auctionToRow(x) }
    table = new SortableTable(rowData, headers)
    scrollTable.contents = table
  }
}


