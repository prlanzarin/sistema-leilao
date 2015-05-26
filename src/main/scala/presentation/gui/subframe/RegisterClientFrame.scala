package main.scala.presentation.gui.subframe

import business.entities.Client
import main.scala.presentation.controller.Connection
import main.scala.presentation.gui.panel.{ButtonsPanel, LabelTextFieldPanel}
import main.scala.presentation.gui.validator.{ValidationException, Validator}

import scala.swing._

/**
 * Created by mhbackes on 25/05/15.
 */
class RegisterClientFrame(parent: Frame) extends ChildFrame(parent) {
  title = "Cadastro de Usuário"
  resizable = false
  val completeName = new TextField(12)
  val bDay = new TextField(12)
  val cpf = new TextField(12)
  val phone = new TextField(12)
  val address = new TextField(12)
  val email = new TextField(12)
  val userName = new TextField(12)
  val password = new PasswordField(12)
  val register = new Button {
    action = Action("Cadastrar") {
      registerClient
    }
  }
  contents = new GridPanel(9, 1) {
    contents += new LabelTextFieldPanel("Nome Completo", completeName)
    contents += new LabelTextFieldPanel("Data de Nascimento", bDay)
    contents += new LabelTextFieldPanel("CPF", cpf)
    contents += new LabelTextFieldPanel("Telefone", phone)
    contents += new LabelTextFieldPanel("E-mail", email)
    contents += new LabelTextFieldPanel("Endereço", address)
    contents += new LabelTextFieldPanel("Nome de Usuário", userName)
    contents += new LabelTextFieldPanel("Senha", password)
    contents += new ButtonsPanel(List(register))
  }

  def parseClient: Client = {
    val clientName = Validator.validateName(completeName.text)
    val clientBDay = Validator.validateDate(bDay.text)
    val clientCpf = Validator.validateCpf(cpf.text)
    val clientPhone = Validator.validatePhone(phone.text)
    val clientAddress = Validator.validateAddress(address.text)
    val clientEmail = Validator.validateEmail(email.text)
    val clientUserName = Validator.validateUsername(userName.text)
    val clientPassword = Validator.validatePassword(password.password)
    new Client(clientUserName, clientPassword, clientName, clientCpf, clientBDay, clientPhone, clientAddress, clientEmail)
  }

  def registerClient: Unit = {
    try {
      val client = parseClient
      Connection.sendAddUserRequest(client)
      Dialog.showMessage(register, "Usuário cadastrado com sucesso")
      closeOperation
    } catch {
      case e: ValidationException => Dialog.showMessage(register, e.getMessage, "Erro", Dialog.Message.Error)
    }
  }
}



