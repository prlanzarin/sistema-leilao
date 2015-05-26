package business.entities

import java.util.Date
import java.util.Calendar

import presentation.ui.UIUtils

case class Auction(indebted: Indebted,
                 property: Property,
                 begin: Date,
                 end: Date,
                 highestBid : Option[Bid] = None,
                 open : Boolean,
                 auctionID : Option[Long] = None)
    {
        override def toString: String = "Endividado: " + indebted.name +
        "\nPropriedade: " + "" + property.name + "\nComeco (R$): " +
        UIUtils.dateFormatter.format(begin) +"\nFim: " + UIUtils.dateFormatter.
        format(end) + "\nIdentificador: " + auctionID.get + "\nAberto == " +
            open + "\nLance mais alto -> " + highestBid.getOrElse(println
            ("Nenhum"))
    }
