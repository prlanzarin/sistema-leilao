package business.entities

import PropertyKind._

class Property(val name: String,
               val value: Double,
               val kind: PropertyKind)
extends Serializable {

    override def toString: String =
        "Nome: " + name + "\nValor: " + Double + "\nTipo: " + kind.toString
}
