package business.services

import java.util.Date

import business.entities.PropertyKind.PropertyKind
import business.entities.{Auction, Property, Indebted}
import database.Database

class ManagerServices extends UserServices {
  val database = new Database

  def insertIndebted(indebted : Indebted) : Boolean = {
    val name = indebted.name
    val bday = indebted.birthDay
    val debt = indebted.debt
    val cpf = indebted.cpf
    if (!validateIndebted(cpf))
      return false
    if (database.queryIndebted(cpf))
      return false
    database.addIndebted(name, bday, debt, cpf)
    return true
  }

  def insertAuction(beginDate: Date, endDate: Date, indebted: Indebted, property: Property) = {

  }

  def insertProperty(i: Indebted, p: Property) : Boolean = {
    val cpf = i.cpf
    
    if (database.queryIndebted(cpf)) {
        database.addProperty(cpf, p.name, p.value, "") // TODO arrumar kind
        return true
    } else {
        return false
    }
  }

  def endAuction(auction: Auction) = {

  }

  def getProperties = {

  }

  def getClosedAuctions = {

  }

  def getIndebteds() : List[Indebted] = sys.error("todo")


  def generateReport(indebteds: List[Indebted]) = {

  }

  def validateIndebted(cpf: String): Boolean = {
    if (cpf.length != 8)
      return false
    else
      return true
  }
}
