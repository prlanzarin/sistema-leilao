package main.scala.presentation.gui.subframe.manager

import business.entities._
import main.scala.presentation.controller.Connection
import main.scala.presentation.gui.panel.{ButtonsPanel, LabelComboBoxPanel, LabelTextFieldPanel}
import main.scala.presentation.gui.subframe.ChildFrame
import main.scala.presentation.gui.validator.{ValidationException, Validator}

import scala.swing._

/**
 * Created by mhbackes on 26/05/15.
 */
class RegisterPropertyFrame(parent: Frame, manager: Manager, indebted: Indebted)
  extends ChildFrame(parent) {
  title = "Novo Bem"
  resizable = false
  val propertyName = new TextField(12)
  val buyYear = new TextField(12)
  val value = new TextField(12)
  val propertyKind = new ComboBox[String](Seq("Imóvel", "Jóia", "Veículo", "Outro"))
  val register = new Button{
    action = Action("Cadastrar"){
      registerPropertyAction
    }
  }

  contents = new GridPanel(5, 1) {
    contents += new LabelTextFieldPanel("Nome", propertyName)
    contents += new LabelTextFieldPanel("Ano da Compra", buyYear)
    contents += new LabelTextFieldPanel("Valor", value)
    contents += new LabelComboBoxPanel("Tipo", propertyKind)
    contents += new ButtonsPanel(List(register))
  }

  def registerPropertyAction: Unit = {
    try {
      val property = parseProperty
      Connection.sendAddPropertyRequest(indebted, property)
      Dialog.showMessage(register, "Bem cadastrado com sucesso")
      closeOperation
    } catch {
      case e: ValidationException => Dialog.showMessage(register, e.getMessage, "Erro", Dialog.Message.Error)
    }
  }

  def parseProperty: Property = {
    val pName = Validator.validateName(propertyName.text)
    val pBuyYear = Validator.validateYear(buyYear.text)
    val pValue = Validator.validateValue(value.text)
    val pKind = PropertyKind.withName(propertyKind.selection.item)
    new Property(pName, pValue, pKind, pBuyYear)
  }

  override def closeOperation():Unit = {
    parent match {
      case p: IndebtedPropertiesFrame => p.updateIndebtedTable
      case _ =>
    }
    super.closeOperation
  }
}
