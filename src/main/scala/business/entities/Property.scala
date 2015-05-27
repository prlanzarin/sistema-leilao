package business.entities

import PropertyKind._

case class Property( name: String,
                     value: Double,
                     kind: PropertyKind,
                     boughtIn: Int,
                     propertyID : Option[Long] = None) {

    def propertyID_(newID : Option[Long]) = Property(name, value, kind,
        boughtIn, newID)

    override def toString: String =
        "Nome: " + name + "\nValor: " + Double + "\nTipo: " + kind.toString +
            "\nAno de Compra: " + boughtIn
}
