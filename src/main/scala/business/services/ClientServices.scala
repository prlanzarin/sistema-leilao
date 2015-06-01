package business.services

import business.entities.PropertyKind.PropertyKind
import business.entities.{Auction, Client, Bid}
import database.Database

class ClientServices extends UserServices {

    def createBid(bid : Bid) = {
        Database.addBid(bid)
    }

    def cancelBid(bid : Bid) = {
        Database.cancelBid(bid)
    }

    def queryOpenAuctions(property : Option[String], propertyKind :
    Option[String]) : List[Auction] = {
        Database.queryOpenAuctions(property, propertyKind)
    }

    def queryAuctionHistory(client : Client, propertyName : Option[String],
                      propertyKind : Option[String]) : List[(Auction, Bid)] = {
        val ca = Database.queryClientAuctions(client).filter(a =>
            propertyKind.map(a.property.kind.toString == _ && a.property.name
                == propertyName.getOrElse(true)).getOrElse(true))
        Nil
    }
}
