package main.scala.presentation.gui.subframe.manager

import business.entities.{Manager, Property}
import main.scala.presentation.controller.Connection
import main.scala.presentation.gui.panel.{ButtonsPanel, LabelTextFieldPanel}
import main.scala.presentation.gui.subframe.ChildFrame
import main.scala.presentation.gui.validator.{ValidationException, Validator}

import scala.swing._

/**
 * Created by mhbackes on 26/05/15.
 */
class CreateAuctionFrame(parent: Frame, manager: Manager, property: Property) extends ChildFrame(parent) {
  val beginDate = new TextField(12)
  val beginTime = new TextField(12)
  val endDate = new TextField(12)
  val endTime = new TextField(12)
  val create = new Button{
    action = Action("Criar"){
      createAuctionAction
    }
  }

  contents = new GridPanel(5, 1) {
    contents += new LabelTextFieldPanel("Data de Início", beginDate)
    contents += new LabelTextFieldPanel("Horário de Início", beginTime)
    contents += new LabelTextFieldPanel("Data de Término", endDate)
    contents += new LabelTextFieldPanel("Horário de Término", endTime)
    contents += new ButtonsPanel(List(create))
  }

  def createAuctionAction: Unit = {
    try {
      val begin = Validator.validateDateTimeAfterNow(beginDate.text, beginTime.text)
      val end = Validator.validateDateTimeAfterNow(endDate.text, endTime.text)
      if(begin.after(end)) throw new ValidationException("A data de início deve ser antes da data de término")
      Connection.sendAddAuctionRequest(property, begin, end)
      Dialog.showMessage(create, "Leilão criado com sucesso")
      closeOperation
    } catch {
      case e: ValidationException => Dialog.showMessage(create, e.getMessage, "Erro", Dialog.Message.Error)
    }
  }

  override def closeOperation: Unit = {
    parent match {
      case p: IndebtedPropertiesFrame => p.updateIndebtedTable
      case p: PropertiesFrame => p.updatePropertiesTable
      case _ =>
    }
    super.closeOperation
  }
}
