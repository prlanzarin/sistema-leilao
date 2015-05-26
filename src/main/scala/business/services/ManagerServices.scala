package business.services

import java.util.Date

import business.entities.{Auction, Indebted, Property}
import database.Database

class ManagerServices extends UserServices {

    def insertIndebted(indebted : Indebted) : Boolean = {
        val name = indebted.name
        val bday = indebted.birthDay
        val debt = indebted.debt
        val cpf = indebted.cpf

        Database.queryIndebted(cpf) match {
            case Some(x) => false
            case None => Database.addIndebted(name, bday, debt, cpf)
                true
        }
    }

    def insertAuction(beginDate: Date, endDate: Date, indebted: Indebted, property: Property) = {

    }

    def insertProperty(i: Indebted, p: Property) : Boolean = {
        val cpf = i.cpf
        val name = p.name
        val value = p.value
        val kind = p.kind
        val boughtIn = p.boughtIn

        Database.queryProperty(cpf, name) match {
            case Some(x) => false
            case None => Database.addProperty(cpf, name, value, kind.
                toString, boughtIn)
                true
        }
    }

    def endAuction(auction: Auction) = {

    }

    def getProperties : List[Property] = {
        Database.getProperties
    }

    def getAuctions : List[Auction] = {
        Database.getAuctions
    }

    def getOpenAuction : List[Auction] = {
        Database.getOpenAuctions
    }

    def getClosedAuctions : List[Auction] = {
        Database.getClosedAuctions
    }

    def getIndebteds : List[Indebted] = {
        Database.getIndebteds
    }

    def generateReport(indebteds: List[Indebted]) = {

    }
}
