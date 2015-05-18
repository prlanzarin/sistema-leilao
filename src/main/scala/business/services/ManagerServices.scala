package business.services

import java.util.Date

import business.entities.PropertyKind.PropertyKind
import business.entities.{Auction, Property, Indebted}
import database.Database

class ManagerServices extends UserServices {
  val database = new Database

  def createIndebted(indebted : Indebted) : Boolean = {
    val name = indebted.name
    val bday = indebted.birthDay
    val debt = indebted.debt
    val cpf = indebted.cpf
    if (!validateIndebted(cpf))
      return false
    database.addIndebted(name, bday, debt, cpf)
    return true
  }

  def createAuction(beginDate: Date, endDate: Date, indebted: Indebted, property: Property) = {

  }

  def createProperty(indebted: Indebted, idKey: Int, name: String, value: Double, kind: PropertyKind): Boolean = {
    if (!validateIndebted(indebted.cpf))
      return false
    if (!validateProperty(indebted.cpf, name))
      return false
    val property = new Property(idKey, name, value, kind)
    database.addProperty(name, value, kind.toString)
    return true
  }

  def endAuction(auction: Auction) = {

  }

  def getProperties = {

  }

  def getClosedAuctions = {

  }

  def getIndebted: Unit = {
    val indebteds = database.queryIndebteds
    return indebteds
  }

  def generateReport(indebteds: List[Indebted]) = {

  }

  def validateIndebted(cpf: String): Boolean = {
    if (database.queryIndebted(cpf) != null)
      return false
    return true
  }

  def validateProperty(indebtedCpf: String, name: String): Boolean = {
    if (database.queryProperty(indebtedCpf, name) != null)
      return false
    return true
  }
}
