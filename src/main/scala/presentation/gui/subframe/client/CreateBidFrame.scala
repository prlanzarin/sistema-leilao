package main.scala.presentation.gui.subframe.client

import business.entities.{PropertyKind, Auction, Client}
import main.scala.presentation.controller.{ConnectionException, Connection}
import main.scala.presentation.gui.panel.{LabelRadioButtonsPanel, LabelSearchPanel}
import main.scala.presentation.gui.subframe.{AuctionsFrame, ChildFrame}
import main.scala.presentation.gui.table.SortableTable
import main.scala.presentation.gui.validator.{ValidationException, Validator}

import scala.swing._

/**
 * Created by mhbackes on 30/05/15.
 */
class CreateBidFrame(parent: Frame, client: Client) extends AuctionsFrame(parent) {
  title = "Novo Lance"
  resizable = false

  val newBidValue = new TextField(37)
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
    layout(scrollTable) = BorderPanel.Position.Center
    layout(new LabelSearchPanel("Valor do Lance:", newBidValue, newBidButton)) = BorderPanel.Position.South
  }
  updateAuctionTable

  def updateAuctionTable: Unit = {
    val propertyKind = getPropertyKindFilter
    val propertyName = if(searchText.text.isEmpty) None else Some(searchText.text)
    val auctionList = Connection.sendQueryOpenedAuctionsRequest(propertyName, propertyKind)
    rowData = new Array[Array[Any]](auctionList.size)
    auctionList.zipWithIndex.foreach { case (x, i) => rowData(i) = auctionToRow(x) }
    table = new SortableTable(rowData, headers)
    scrollTable.contents = table
  }

  def createBidAction: Unit = {
    val row = table.selection.rows
    if(row.isEmpty)
      Dialog.showMessage(table, "Selecione um leilão", "Erro", Dialog.Message.Error)
    else{
      try {
        val clientId = client.userName
        val auctionId = rowToAuctionId(row.anchorIndex)
        val value = Validator.validateValue(newBidValue.text)
        Connection.sendAddBidRequest(clientId, auctionId, value)
          Dialog.showMessage(table, "Lance realizado com sucesso", "Sucesso", Dialog.Message.Info)
      }
      catch {
        case e: ValidationException => Dialog.showMessage(table, e.getMessage, "Erro", Dialog.Message.Error)
        case e: ConnectionException => Dialog.showMessage(table, e.getMessage, "Erro", Dialog.Message.Error)
      } finally {
        updateAuctionTable
      }
    }
  }
}
