package main.scala.presentation.gui.subframe

import main.scala.presentation.gui.panel.{ButtonsPanel, LabelTextFieldPanel}

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
  val password = new PasswordField(12)
  val register = new Button("Cadastrar")
  contents = new GridPanel(7, 1) {
    contents += new LabelTextFieldPanel("Nome Completo", completeName)
    contents += new LabelTextFieldPanel("Data de Nascimento", bDay)
    contents += new LabelTextFieldPanel("CPF", cpf)
    contents += new LabelTextFieldPanel("Telefone", phone)
    contents += new LabelTextFieldPanel("E-mail", email)
    contents += new LabelTextFieldPanel("Senha", password)
    contents += new ButtonsPanel(List(register))
  }
}



