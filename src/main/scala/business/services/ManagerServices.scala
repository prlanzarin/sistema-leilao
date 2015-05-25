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

        Database.queryProperty(cpf, name) match {
            case Some(x) => false
            case None => Database.addProperty(cpf, name, value, kind.toString)
                true
        }
    }

    def endAuction(auction: Auction) = {

    }

    def getProperties = {

    }

    def getClosedAuctions = {

    }
/*
    def getIndebteds(): List[Indebted] = {
        val indebteds = Database.getIndebteds
        return indebteds
    }
*/
    def generateReport(indebteds: List[Indebted]) = {

    }
}
