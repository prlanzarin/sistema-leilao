package business.services

import business.entities.PropertyKind.PropertyKind
import business.entities.{Auction, Client, Bid}
import database.Database

class ClientServices extends UserServices {

    def createBid(uid : String, aid : Long, value : Double) : Option[Boolean] = {
        Database.queryAuction(aid) match {
            case Some(x) => value <= Database.queryHighestBid(aid).get.value
            match {
                case false => Database.queryUser(uid) map
                    (u => Database.addBid(uid, aid, value))
                    Some(true)
                case true => Some(false)
            }
            case None => None
        }
    }

    def cancelBid(uid : String, aid : Long, value : Double) : Boolean = {
        Database.queryAuction(aid) match {
            case Some(x) => Database.queryUser(uid) foreach(
                u => Database.cancelBid(uid, aid, value))
                true
            case None => false
        }
    }

    def queryOpenAuctions(property : Option[String], propertyKind :
    Option[String]) : List[Auction] = {
        Database.queryOpenAuctions(property, propertyKind)
    }

    def queryAuctionHistory(client : Client, propertyName : Option[String],
                      propertyKind : Option[String]) : List[(Auction, Bid)] = {
        Database.queryClientBidHistory(client).filter(a =>
            propertyKind.map(a._1.property.kind.toString == _).getOrElse
                (true) && propertyName.map(a._1.property.name == _).getOrElse(true))
    }
}
