package database

import business.entities.PropertyKind
import java.util.Date
import java.sql.Date

// Import the session management, including the implicit threadLocalSession
import org.scalaquery.session._
import org.scalaquery.session.Database.threadLocalSession

import org.scalaquery.ql._
import org.scalaquery.ql.TypeMapper._

import org.scalaquery.ql.extended.H2Driver.Implicit._
import org.scalaquery.ql.extended.{ExtendedTable => Table}

object DatabaseApp {
  def main(){
    val db = new Database("data1")
    db.newDbSession()
  }
}

class Database(name : String) {
  val db = Database.forURL("jdbc:h2:file:data/db" format name, driver = "org.h2.Driver")

  val indebteds = new Table[(Int, String, Int , String, Double)]("USERS") {
    def id = column[Int]("I_ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def bdate = column[Int]("BIRTHDATE")
    def cpf = column[String]("CPF")
    def debt = column[Double]("DEBT")

    def * = id ~ name ~ bdate ~ cpf ~ debt
  }

  val properties = new  Table[(Int, String, Double, String, Int)]("USERS") {
    def id = column[Int]("P_ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def value = column[Double]("VALUE")
    def kind = column[String]("KIND")
    def ownerID = column[Int]("OWNER")
    def owner = foreignKey("OWNER_FK", ownerID, indebteds)(_.id)

    def * = id ~ name ~ value ~ kind ~ ownerID
  }

  def newDbSession():Unit = {
    db withSession {
      (indebteds.ddl ++ properties.ddl).create
      indebteds.insert(0, "Joao", 101010, "01234567891", 150.82)
      properties.insert(0, "S10", 35000.50, "Car", 0)
    }

    println("Coffees:")
    Query(indebteds) foreach { case (id, name, bdate, cpf, debt) =>
      println("  " + name + "\t" + name + "\t" + bdate + "\t" + cpf + "\t" + debt)
    }
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