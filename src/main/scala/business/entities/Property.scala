package business.entities

import PropertyKind._

case class Property( name: String,
                value: Double,
                kind: PropertyKind,
                boughtIn: Int) {

    override def toString: String =
        "Nome: " + name + "\nValor: " + Double + "\nTipo: " + kind.toString +
            "\nAno de Compra: " + boughtIn
}
