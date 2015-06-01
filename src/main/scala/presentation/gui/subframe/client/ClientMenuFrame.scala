package main.scala.presentation.gui.subframe.client

import business.entities.Client
import main.scala.presentation.gui.panel.ButtonsPanel
import main.scala.presentation.gui.subframe.ChildFrame

import scala.swing.{Action, Button, Frame}

/**
 * Created by mhbackes on 25/05/15.
 */
class ClientMenuFrame(parent: Frame, client: Client) extends ChildFrame(parent) {
  title = "Bem Vindo, " + client.name + "!"
  resizable = false
  val createBid = new Button {
    action = Action("Novo Lance") {
      createBidAction
    }
  }
  val history = new Button {
    action = Action("Histórico") {
      historyAction
    }
  }
  contents = new ButtonsPanel(List(createBid, history))

  def createBidAction = {
    visible = false
    new CreateBidFrame(this, client)
  }

  def historyAction = {
    visible = false
    new HistoryFrame(this, client)
  }
}


