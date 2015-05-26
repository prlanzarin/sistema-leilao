package business.entities

import presentation.ui.UIUtils

case class Bid( auction: Long,
                client: Client,
                value : Double) {

    override def toString: String = "Cliente: " + client.name + "\nValor: " +
        value
}
