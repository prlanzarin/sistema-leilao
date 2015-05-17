package main.scala.business.services

import java.util.Date

import main.scala.business.entities.PropertyKind.PropertyKind
import main.scala.business.entities.{Auction, Property, Indebted}
import main.scala.database.Database

class ManagerServices extends UserServices {
  val database = new Database

  def createIndebted(name: String, bday: Date, debt: Double, cpf: String): Boolean = {
    if (!validateIndebted(cpf))
      return false
    if (database.queryIndebted(cpf) != null)
      return false
    database.addIndebted(name, bday, debt, cpf)
    return true
  }

  def createAuction(beginDate: Date, endDate: Date, indebted: Indebted, property: Property) = {

  }

  def createProperty(indebted: Indebted, name: String, value: Double, kind: PropertyKind) = {

  }

  def endAuction(auction: Auction) = {

  }

  def getProperties = {

  }

  def getClosedAuctions = {

  }

  def getIndebted = {

  }

  def generateReport(indebteds: List[Indebted]) = {

  }

  def validateIndebted(cpf: String): Boolean = {
    if (cpf.length != 8)
      return false
    else
      return true
  }
}