package business.entities;

import java.util.Date

class Indebted(name: String,
               birthDay: Date,
               dept: Double,
               cpd: String) {
    private var properties: List[Property] = List.empty[Property]
    def addProperty(p: Property) = p :: this.properties
}
