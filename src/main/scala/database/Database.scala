package database

import java.util.Date
import business.entities._

class Database {
  
  /*
   * TODO / 2nd iteration
   */
  def queryScheduledAuctions = {
    
  }
  /*
   * TODO / 2nd iteration
   */
  def queryOpenAuctions = {
    
  }
  /*
   * TODO / 2nd iteration
   */
  def queryClosedAuctions = {
    
  }
  /*
   * TODO
   */
  def queryIndebteds() : List[Indebted]= sys.error("todo")
    
  /*
   * TODO
   */
  def queryProperties() : List[Property] = sys.error("todo")
  /*
   * TODO
   */
  def addIndebted(name : String, birthDay : Date, debt : Double, CPF : String) : Unit = {
    
  }
  /*
   * TODO
   */
  def addProperty(cpf : String, propertyName : String, value : Double, kind : String) : Unit = {
    
  }
  /*
   * TODO / 2nd iteration
   */
  def addAuction() = {
    
  }
  /*
   * TODO / 2nd iteration
   */
  def addUser() = {
    
  }
  /* 
   * TODO
   * type String of CPF is a placeholder
   */
  def queryProperty(indebtedCPF : String, propertyName : String) : Boolean = sys.error("todo")
    
  /*
   * TODO
   */
  def queryIndebted(indebtedCPF : String) : Boolean = sys.error("todo")
    
}
