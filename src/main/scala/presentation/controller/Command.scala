package main.scala.presentation.controller

import main.scala.presentation.ui.UIUtils
import main.scala.business.entities.Indebted
import main.scala.business.entities.Property
import main.scala.business.entities.PropertyKind
import main.scala.business.entities.PropertyKind._

abstract class Command {
  def execute
}

class CreateIndebtedCommand extends Command {
  override def execute {
    val indebted = readIndebted
    //send indebted to app-server
    //recieve result from app-server
  }

  def readIndebted: Indebted = {
    println("Preencha as informações do endividado:")
    val name = UIUtils.readString("Informe o nome:")
    val cpf = UIUtils.readCPF("Informe o CPF do endividado: (somente dígitos)")
    val bday = UIUtils.readDate(
      "Informe a data de nascimento: (" + UIUtils.dateFormat + ")")
    val debt = UIUtils.readDouble(
      "Informe o valor da dívida: (R$)", 0.0)
    return new Indebted(name, bday, debt, cpf)
  }
  
  override def toString: String = "Cadastrar endividado"
}

class CreatePropertyCommand(indebtedCpf: String) extends Command {
  override def execute {
    val property = readProperty(indebtedCpf)
    //send property to app-server
    //recieve result from app-server
  }

  def readProperty(indebtedCpf: String): Property = {
    println("Preencha as informações do bem:")
    val name = UIUtils.readString("Informe o nome:")
    val value = UIUtils.readDouble("Informe o valor: (R$)", 0.0)
    val properties = "Imóvel" :: "Jóia" :: "Veículo" :: "Outro" :: List()
    val kind = UIUtils.select("Informe o tipo do bem:", properties)
    return new Property(0, name, value, PropertyKind.withName(kind))
  }
  
}

class SelectIndebtedCommand extends Command {
  override def execute {

  }
  
  override def toString: String = "Selecionar endividado"
}

class ExitCommand extends Command {
  override def execute = Unit
  override def toString = "Sair"
}