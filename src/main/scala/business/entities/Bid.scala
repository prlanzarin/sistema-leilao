package business.entities

import presentation.ui.UIUtils

case class Bid( auction: Long,
                client: Client,
                value : Double)
{
    override def toString: String = "Cliente: " + client.name + "\nValor: " +
        value
}


/*
private var _value: Double = 0

    def value = _value

    def value_(nvalue : Double) = _value = nvalue

 */
