package business.services

import java.util.Date

import business.entities.PropertyKind.PropertyKind
import business.entities._ //{Auction, Property, Indebted}
import database.Database

class ManagerServices extends UserServices {
  val database = new Database

  def insertIndebted(indebted : Indebted) : Boolean = {
    val name = indebted.name
    val bday = indebted.birthDay
    val debt = indebted.debt
    val cpf = indebted.cpf
    if (database.queryIndebted(cpf) == true)
      return false

    database.addIndebted(name, bday, debt, cpf)
    return true
  }

  def insertAuction(beginDate: Date, endDate: Date, indebted: Indebted, property: Property) = {

  }

  def insertProperty(i: Indebted, p: Property) : Boolean = {
    val cpf = i.cpf
    val name = p.name
    val value = p.value
    val kind = p.kind

    if (!(database.queryIndebted(cpf)))
      return false

    database.addProperty(cpf, name, value, kind.toString)

    return true
  }

  def endAuction(auction: Auction) = {

  }

  def getProperties = {

  }

  def getClosedAuctions = {

  }

  def getIndebteds(): List[Indebted] = {
    val indebteds = database.getIndebteds
    return indebteds
  }

  def generateReport(indebteds: List[Indebted]) = {

  }
}
