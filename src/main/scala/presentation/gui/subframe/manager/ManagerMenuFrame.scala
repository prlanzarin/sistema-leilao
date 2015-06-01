package main.scala.presentation.gui.subframe.manager

import business.entities.Manager
import main.scala.presentation.gui.subframe.ChildFrame

import scala.swing.{Action, Button, Frame, GridPanel}

/**
 * Created by mhbackes on 25/05/15.
 */
class ManagerMenuFrame(parent: Frame, manager: Manager) extends ChildFrame(parent) {
  title = "Bem Vindo, " + manager.name + "!"
  resizable = false
  val indebteds = new Button {
    action = Action("Endividados") {
      indebtedsAction
    }
  }
  val properties = new Button {
    action = Action("Bens") {
      propertiesAction
    }
  }
  val openedAuctions = new Button {
    action = Action("Leilões Abertos") {
      openedAuctionsAction
    }
  }
  val closedAuctions = new Button {
    action = Action("Leilões Fechados") {
      closedAuctionsAction
    }
  }
  contents = new GridPanel(2, 2) {
    contents += indebteds
    contents += properties
    contents += openedAuctions
    contents += closedAuctions
  }

  def indebtedsAction = {
    visible = false
    new IndebtedsFrame(this, manager)
  }

  def propertiesAction = {
    visible = false
    new PropertiesFrame(this, manager)
  }

  def openedAuctionsAction = {
    visible = false
    new OpenedAuctionsFrame(this, manager)
  }

  def closedAuctionsAction = {
    visible = false
    new ClosedAuctionsFrame(this, manager)
  }
}

