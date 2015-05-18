package business.entities

import java.util.Date
import presentation.ui.UIUtils

class Indebted (val name: String,
                val birthDay: Date,
                val debt: Double,
                val cpf: String)
extends Serializable {
    private var properties: List[Property] = List.empty[Property]
    def addProperty(p: Property) = p :: this.properties
    override def toString: String =
        "Nome: " + name + "\nCPF: " + cpf + "\nDívida (R$): " + debt
        .+("\nAniversário: " + UIUtils.dateFormatter.format(birthDay))
}