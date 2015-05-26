package main.scala.presentation.gui.subframe

import business.entities.Client
import main.scala.presentation.gui.panel.ButtonsPanel

import scala.swing.{Action, Button, Frame}

/**
 * Created by mhbackes on 25/05/15.
 */
class ClientMenuFrame(parent: Frame, client: Client) extends ChildFrame(parent) {
  title = "Bem Vindo, " + client.name + "!"
  resizable = false
  val newBid = new Button {
    action = Action("Novo Lance") {
      newBidAction
    }
  }
  val history = new Button {
    action = Action("Histórico") {
      historyAction
    }
  }
  contents = new ButtonsPanel(List(newBid, history))

  def newBidAction = {
//    visible = false
//    new NewBidFrame(this, client)
  }

  def historyAction = {
//    visible = false
//    new HistoryFrame(this, client)
  }
}
