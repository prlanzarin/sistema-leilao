package business.services

import java.util.{Calendar, Date}

import business.entities.PropertyKind.PropertyKind
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

    def insertAuction(property: Property, beginDate: Date, endDate: Date): Boolean = {
        val indebted = Database.queryIndebted(property)
        val nowTime = Calendar.getInstance.getTime()
        //TODO schedule auction if not opened
        Database.addAuction(Auction(indebted.get, property, beginDate, endDate))
        true
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

    def endAuction(aid : Long) : Boolean = {
        Database.queryAuction(aid) match {
            case Some(x) => Database.updateAuction(aid)
                true
            case None => false
        }
    }

    def getIndebtedProperties(indebtedCpf: String): List[Property] ={
        Database.queryIndebtedProperties(indebtedCpf)
    }

    def getProperties : List[Property] = {
        Database.getProperties
    }

    def getProperties(kind: Option[String], inAuction: Option[Boolean]): List[Property] ={
        Database.queryProperties(kind, inAuction)
    }

    def getAuctions : List[Auction] = {
        Database.getAuctions
    }

    def getOpenAuction : List[Auction] = {
        Database.getOpenAuctions
    }

    def getOpenAuctions(property : Option[String], propertyKind :
    Option[String]) : List[Auction] = {
        Database.queryOpenAuctions(property, propertyKind)
    }

    def getClosedAuctions(property : Option[String], propertyKind :
    Option[String]) : List[Auction] = {
        Database.queryClosedAuctions(property, propertyKind)
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
