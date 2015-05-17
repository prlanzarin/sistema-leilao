package main.scala.business.services

import java.util.Date

import main.scala.business.entities.PropertyKind.PropertyKind
import main.scala.business.entities.{Auction, Property, Indebted}
import main.scala.database.Database

class ManagerServices extends UserServices {
  val database = new Database

  def createIndebted(indebted : Indebted) : Boolean = {
    val name = indebted.name
    val bday = indebted.birthDay
    val debt = indebted.debt
    val cpf = indebted.cpf
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
