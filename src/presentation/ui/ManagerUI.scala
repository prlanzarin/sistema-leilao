package presentation.ui

class ViewManager extends UI {
  def readIndebted: String = {
    println("Preencha as informações do endividado:")
    val name = readString("Informe o nome:")
    val bday = readDate("Informe a data de nascimento: (dd/MM/aaaa")
    val cpf = readCPF("Informe o CPF do endividado: (somente dígitos)")
    val debt = readDouble("Informe o valor da dívida", 0.0)
    val json = "{ action: createIndebted"
      .+(" params: { '")
      .+(name).+("' '")
      .+(dateFormatter.format(bday)).+("' '")
      .+(cpf).+("' '")
      .+(debt).+("' } }")
    return json
  }
}