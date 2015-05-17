package presentation.ui

class ManagerUI extends UI {
  def readIndebted: String = {
    println("Preencha as informações do endividado:")
    val name = readString("Informe o nome:")
    val cpf = readCPF("Informe o CPF do endividado: (somente dígitos)")
    val bday = dateFormatter.format(
        readDate("Informe a data de nascimento: ("+dateFormat+")"))
    val debt = readDouble("Informe o valor da dívida: (R$)", 0.0).toString()
    val json = toJsonAction("readIndebted", List(name, cpf, bday, debt))   
    return json
  }
  
  def readProperty(indebtedCpf: String): String = {
    println("Preencha as informações do bem:")
    val name = readString("Informe o nome:")
    val value = readDouble("Informe o valor: (R$)", 0.0).toString()
    val properties = "Imóvel"::"Jóia"::"Veículo"::"Outro"::List()
    val kind = select("Informe o tipo do bem:", properties)
    val json = toJsonAction("createProperty", List(indebtedCpf, name, value, kind))
    return json
  }
}