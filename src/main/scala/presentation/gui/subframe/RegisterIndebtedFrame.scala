package main.scala.presentation.gui.subframe

import business.entities.{Indebted, Manager}
import main.scala.presentation.controller.Connection
import main.scala.presentation.gui.panel.{LabelTextFieldPanel, ButtonsPanel}
import main.scala.presentation.gui.validator.{ValidationException, Validator}

import scala.swing._

/**
 * Created by mhbackes on 26/05/15.
 */
class RegisterIndebtedFrame(parent: Frame, manager: Manager) extends ChildFrame(parent) {
  title = "Novo Endividado"
  resizable = false
  val completeName = new TextField(12)
  val bday = new TextField(12)
  val cpf = new TextField(12)
  val debt = new TextField(12)
  val register = new Button {
    action = Action("Cadastrar") {
      registerIndebtedAction
    }
  }

  contents = new GridPanel(5, 1) {
    contents += new LabelTextFieldPanel("Nome Completo", completeName)
    contents += new LabelTextFieldPanel("Data de Nascimento", bday)
    contents += new LabelTextFieldPanel("CPF", cpf)
    contents += new LabelTextFieldPanel("Dívida (R$)", debt)
    contents += new ButtonsPanel(List(register))
  }

  def registerIndebtedAction: Unit = {
    try {
      val indebted = parseIndebted
      Connection.sendAddIndebtedRequest(indebted)
      Dialog.showMessage(register, "Endividado cadastrado com sucesso")
      closeOperation
    } catch {
      case e: ValidationException => Dialog.showMessage(register, e.getMessage, "Erro", Dialog.Message.Error)
    }
  }

  def parseIndebted: Indebted = {
    val indebtedName = Validator.validateName(completeName.text)
    val indebtedBday = Validator.validateDate(bday.text)
    val indebtedCpf = Validator.validateCpf(cpf.text)
    val indebtedDebt = Validator.validateValue(debt.text)
    new Indebted(indebtedName, indebtedBday, indebtedDebt, indebtedCpf)
  }

  override def closeOperation: Unit = {
    parent match {
      case p: IndebtedsFrame => p.updateIndebtedTable
      case _ =>
    }
    super.closeOperation
  }
}
