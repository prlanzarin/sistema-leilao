package main.scala.presentation.gui

import business.entities.{Client, Manager}
import main.scala.presentation.gui.panel.{ButtonsPanel, LabelTextFieldPanel}
import main.scala.presentation.gui.subframe.{ManagerMenuFrame, ClientMenuFrame, RegisterClientFrame}
import main.scala.presentation.gui.validator.{ValidationException, Validator}
import presentation.controller.Connection

import scala.swing._

/**
 * Created by mhbackes on 25/05/15.
 */
class LoginFrame extends MainFrame {
  setLocationRelativeTo(this)
  title = "Login"
  resizable = false
  Connection.init

  val userName = new TextField(12)
  val password = new PasswordField(12)
  val register = new Button {
    action = Action("Cadastrar-se") {
      registerAction
    }
  }
  val login = new Button {
    action = Action("Login") {
      loginAction
    }
  }
  contents = new GridPanel(3, 1) {
    contents += new LabelTextFieldPanel("Usuário", userName)
    contents += new LabelTextFieldPanel("Senha", password)
    contents += new ButtonsPanel(List(register, login))
  }

  override def closeOperation(): Unit = {
    Connection.end
    super.closeOperation()
    dispose()
  }

  def registerAction {
    visible = false
    new RegisterClientFrame(this)
  }

  def loginAction {
    try {
    val user = Connection.sendLoginRequest(userName.text, Validator.validatePassword(password.password))
      user match {
        case Some(client: Client )=> visible = false; new ClientMenuFrame(this, client)
        case Some(manager: Manager) => visible = false; new ManagerMenuFrame(this, manager)
        case None => throw new ValidationException("Usuário ou senha inválidos")
      }
    } catch {
      case e: ValidationException => Dialog.showMessage(register, e.getMessage, "Erro", Dialog.Message.Error)
    } finally {
      password.peer.setText("")
    }
  }
}
