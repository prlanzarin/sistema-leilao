package main.scala.presentation.gui.subframe

import business.entities.{Auction, Property, Manager}
import main.scala.presentation.gui.panel.{ButtonsPanel, LabelTextFieldPanel}
import main.scala.presentation.gui.validator.{Validator, ValidationException}

import scala.swing._

/**
 * Created by mhbackes on 26/05/15.
 */
class RegisterAuctionFrame(parent: Frame, manager: Manager, property: Property) extends ChildFrame(parent) {
  val beginDate = new TextField(12)
  val beginTime = new TextField(12)
  val endDate = new TextField(12)
  val endTime = new TextField(12)
  val register = new Button("Cadastrar")

  contents = new GridPanel(5, 1) {
    contents += new LabelTextFieldPanel("Data de Início", beginDate)
    contents += new LabelTextFieldPanel("Horário de Início", beginTime)
    contents += new LabelTextFieldPanel("Data de Término", endDate)
    contents += new LabelTextFieldPanel("Horário de Término", endTime)
    contents += new ButtonsPanel(List(register))
  }

//  def registerAuctionAction: Unit = {
//    try {
//      val indebted = parseAuction
//      //Connection.sendAddAuctionRequest(indebted)
//      Dialog.showMessage(register, "Leilão criado com sucesso")
//      closeOperation
//    } catch {
//      case e: ValidationException => Dialog.showMessage(register, e.getMessage, "Erro", Dialog.Message.Error)
//    }
//  }

//  def parseAuction: Auction = {
//    //new Auction()
//  }

  override def closeOperation: Unit = {
    parent match {
      case p: IndebtedPropertiesFrame => p.updateIndebtedTable
      //case p: PropertiesFrame => p.
      case _ =>
    }
    super.closeOperation
  }
}
