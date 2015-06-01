package business.entities

import java.util.Date
import java.util.Calendar

import presentation.ui.UIUtils

case class Auction(indebted: Indebted,
                   property: Property,
                   begin: Date,
                   end: Date,
                   highestBid : Option[Bid] = None,
                   auctionID : Option[Long] = None,
                   numberOfBids : Option[Int] = None) {

    def highestBid_(bid : Option[Bid]) = Auction(indebted, property, begin,
        end, bid, auctionID)

    def auctionID_(newID : Option[Long]) = Auction(indebted, property,
        begin, end, highestBid, newID)

    def isOpen : Boolean = {
        lazy val now = Calendar.getInstance.getTime()
        begin.before(now) && end.after(now)
    }

    override def toString: String = "Endividado: " + indebted.name +
        "\nPropriedade: " + "" + property.name + "\nComeco (R$): " +
        UIUtils.dateFormatter.format(begin) +"\nFim: " + UIUtils.dateFormatter.
        format(end) + "\nIdentificador: " + auctionID.get + "\nAberto == " +
         isOpen + "\nLance mais alto -> " + highestBid.getOrElse(println
        ("Nenhum")) + "\nNumero de lances: " + numberOfBids.getOrElse(None)
}
