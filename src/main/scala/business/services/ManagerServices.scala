package business.services

import java.util.Date

import business.entities.PropertyKind.PropertyKind
import business.entities.{Auction, Property, Indebted}
import database.Database

class ManagerServices extends UserServices {
  val database = new Database

  def createIndebted(name: String, bday: Date, debt: Double, cpf: String): Boolean = {
    if (!validateIndebted(cpf))
      return false
    database.addIndebted(name, bday, debt, cpf)
    return true
  }

  def createAuction(beginDate: Date, endDate: Date, indebted: Indebted, property: Property) = {

  }

  def createProperty(indebted: Indebted, idKey: Integer, name: String, value: Double, kind: PropertyKind): Boolean = {
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

  def getIndebted: List[Indebted] = {
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