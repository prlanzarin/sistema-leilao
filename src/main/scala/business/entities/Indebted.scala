package business.entities

import java.util.Date

class Indebted (val name: String,
                val birthDay: Date,
                val debt: Double,
                val cpf: String)
extends Serializable {
    private var properties: List[Property] = List.empty[Property]
    def addProperty(p: Property) = p :: this.properties
}
