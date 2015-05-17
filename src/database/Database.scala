package database

import business.entities.PropertyKind
import scala.slick.driver.SQLiteDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.Date
import slick.driver.SQLiteDriver
import java.sql.Date
/*
class SQLite(name: String) {
def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def nom = column[String]("NOM", O.NotNull)
  def prénom = column[String]("PRENOM")
  def sexe = column[Int]("SEXE")
  def télPortable = column[String]("TELPOR")
  def télBureau = column[String]("TELBUR")
  def télPrivé = column[String]("TELPRI")
  def siteRDV = column[String]("SITE")
  def typeRDV = column[String]("TYPE")
  def libelléRDV = column[String]("LIBELLE")
  def numRDV = column[String]("NUMRDV")
  def étape = column[String]("ETAPE")
  def dateRDV = column[Date]("DATE")
  def heureRDVString = column[String]("HEURE")
  def statut = column[String]("STATUT")
  def orderId = column[String]("ORDERID")
    }
  }
} */
class Database(name : String) {
  
  val db = Database.forURL("jdbc:sqlite:testdb.db" format name, driver = "org.sqlite.JDBC")
  
 implicit val JavaUtilDateMapper =
    MappedColumnType .base[java.util.Date, java.sql.Timestamp] (
      d => new java.sql.Timestamp(d.getTime),
      d => new java.util.Date(d.getTime))
      
  class PropertiesTable(tag : Tag) extends Table[(Int, String, Double, String, Int)](tag, "USERS") {
    def id = column[Int]("P_ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def value = column[Double]("VALUE")
    def kind = column[String]("KIND")
    def ownerID = column[Int]("OWNER")
    def owner = foreignKey("OWNER_FK", ownerID, TableQuery[IndebtedsTable])(_.id)
    
    def * = (id, name, value, kind, ownerID)

  }
  
  class IndebtedsTable(tag : Tag) extends Table[(Int, String, java.util.Date , String, Double)](tag, "USERS") {
    def id = column[Int]("I_ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def bdate = column[java.util.Date]("BIRTHDATE")
    def cpf = column[String]("CPF")
    def debt = column[Double]("DEBT")
    
    def * = (id, name, bdate, cpf, debt)
  }
  
  /* 
   * TODO
   * type String of CPF is a placeholder
   */
  def queryProperty(indebtedCPF : String, propertyName : String) = {
    
  }
  /*
   * TODO
   */
  def queryIndebted(indebtedCPF : String) = {
    
  }
  /*
   * TODO
   */
  def addIndebted(name : String, birthDay : java.util.Date, debt : Double, CPF : String) = {
    
  }
  /*
   * TODO
   */
  def addProperty(propertyName : String, value : Double, kind : String) = {
    
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
}