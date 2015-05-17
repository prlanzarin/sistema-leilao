package presentation.controller

import presentation.ui.UIUtils

abstract class Command {
  def execute
}

class CreateIndebtedCommand extends Command {
  override def execute {
    val json: String = readIndebted
    
  }

  def readIndebted: String = {
    println("Preencha as informações do endividado:")
    val name = UIUtils.readString("Informe o nome:")
    val cpf = UIUtils.readCPF("Informe o CPF do endividado: (somente dígitos)")
    val bday = UIUtils.dateFormatter.format(UIUtils.readDate(
      "Informe a data de nascimento: (" + UIUtils.dateFormat + ")"))
    val debt = UIUtils.readDouble(
      "Informe o valor da dívida: (R$)", 0.0).toString()
    val json = UIUtils.toJsonAction(
      "readIndebted", List(name, cpf, bday, debt))
    return json
  }
}

class CreatePropertyCommand(indebtedCpf: String) extends Command {
  override def execute {
    val json: String = readProperty(indebtedCpf)

  }

  def readProperty(indebtedCpf: String): String = {
    println("Preencha as informações do bem:")
    val name = UIUtils.readString("Informe o nome:")
    val value = UIUtils.readDouble("Informe o valor: (R$)", 0.0).toString()
    val properties = "Imóvel" :: "Jóia" :: "Veículo" :: "Outro" :: List()
    val kind = UIUtils.select("Informe o tipo do bem:", properties)
    val json = UIUtils.toJsonAction(
      "createProperty", List(indebtedCpf, name, value, kind))
    return json
  }
}

class SelectIndebtedCommand extends Command {
  override def execute {

  }
}

class ExitCommand extends Command {
  override def execute = Unit
}