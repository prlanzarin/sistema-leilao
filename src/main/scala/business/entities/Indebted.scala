package business.entities

import java.util.Date
import presentation.ui.UIUtils

case class Indebted (name: String,
                     birthDay: Date,
                     debt: Double,
                     cpf: String,
                     var properties: List[Property] = List.empty[Property]){

    def addProperty(p: Property) = p :: this.properties

    override def toString: String =
        "Nome: " + name + "\nCPF: " + cpf + "\nDívida (R$): " + debt
            .+("\nAniversário: " + UIUtils.dateFormatter.format(birthDay))
}