package business.services

import business.entities.PropertyKind.PropertyKind
import business.entities.{Auction, Client, Bid}
import database.Database

class ClientServices extends UserServices {

    def createBid(uid : String, aid : Long, value : Double) : Boolean = {
        Database.addBid(uid, aid, value)
        true
        //TODO CATCH DB EXCEPTIONS
    }

    def cancelBid(uid : String, aid : Long, value : Double) : Boolean = {
        Database.cancelBid(uid, aid, value)
        true
        //TODO CATCH DB EXCEPTIONS
    }

    def queryOpenAuctions(property : Option[String], propertyKind :
    Option[String]) : List[Auction] = {
        Database.queryOpenAuctions(property, propertyKind)
    }

    def queryAuctionHistory(client : Client, propertyName : Option[String],
                      propertyKind : Option[String]) : List[(Auction, Bid)] = {
        Database.queryClientBidHistory(client).filter(a =>
            propertyKind.map(a._1.property.kind.toString == _ && a._1.property
                .name == propertyName.getOrElse(true)).getOrElse(true))
    }
}
