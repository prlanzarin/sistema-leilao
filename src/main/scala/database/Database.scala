package database

import java.util.Date
import business.entities._

class Database {
  
  def queryScheduledAuctions = {
    
  }

  def queryOpenAuctions = {
    
  }

  def queryClosedAuctions = {
    
  }

  def queryIndebteds() : List[Indebted]= sys.error("todo")
    
  def queryProperties() : List[Property] = sys.error("todo")

  def addIndebted(name : String, birthDay : Date, debt : Double, CPF : String) : Unit = {
    
  }

  def addProperty(cpf : String, propertyName : String, value : Double, kind : String) : Unit = {
    
  }

  def addAuction() = {
    
  }

  def addUser() = {
    
  }

  def queryProperty(indebtedCPF : String, propertyName : String) : Boolean = sys.error("todo")
    
  def queryIndebted(indebtedCPF : String) : Boolean = sys.error("todo")
    
}
