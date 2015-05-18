package presentation.controller

import presentation.ui.UIUtils
import business.entities.Indebted
import business.entities.Property
import business.entities.PropertyKind
import business.entities.PropertyKind._

abstract class Command {
  def execute
}

class CreateIndebtedCommand extends Command {
  override def execute {
    val indebted = readIndebted
    Connection.sendAddIndebtedRequest(indebted)
    //send indebted to app-server
    //recieve result from app-server
  }

  def readIndebted: Indebted = {
    println("Preencha com as informações do endividado:")
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
    println("Preencha com as informações do bem:")
    val name = UIUtils.readString("Informe o nome:")
    val value = UIUtils.readDouble("Informe o valor: (R$)", 0.0)
    val properties = "Imóvel" :: "Jóia" :: "Veículo" :: "Outro" :: List()
    val kind = UIUtils.select("Informe o tipo do bem:", properties)
    return new Property(name, value, PropertyKind.withName(kind))
  }
  
}

class SelectIndebtedCommand extends Command {
  override def execute {
    //send request to app-server
    //recieve result from app-server
    //val indebted = selectIndebted(recieved list)
  }
  
  def selectIndebted(loi: List[Indebted]): Indebted = {
    println("Selecione um endividado:")
    for (i <- 0 until loi.size){
      println(i + " - " + loi(i))
    }
    val i = UIUtils.readInt("Informe o índice do endividado correspondente:")
    return loi(i)
  }
  
  override def toString: String = "Selecionar endividado"
}

class ExitCommand extends Command {
  override def execute = Unit
  override def toString = "Sair"
}
