package database

import business.entities._

import org.scalaquery.meta.MTable
import org.scalaquery.session._
import org.scalaquery.session.Database.threadLocalSession
import org.scalaquery.ql._
import org.scalaquery.ql.TypeMapper._
import org.scalaquery.ql.extended.H2Driver.Implicit._
import org.scalaquery.ql.extended.{ExtendedTable => Table}

object Database {
  implicit val JavaUtilDateTypeMapper =
    MappedTypeMapper.base[java.util.Date, Long](_.getTime,
                                                new java.util.Date(_))
  val db = org.scalaquery.session.Database.forURL("jdbc:h2:file:./db/TESTD02115123",
                            driver = "org.h2.Driver")
  var PropID : Int = 0

  val indebteds = new
      Table[(String, String, java.util.Date, Double)]("INDEBTEDS") {

    def cpf = column[String]("CPF", O.PrimaryKey)
    def name = column[String]("NAME")
    def bdate = column[java.util.Date]("BIRTHDATE")
    def debt = column[Double]("DEBT")

    def * = cpf ~ name ~ bdate ~ debt
  }

  val properties = new
      Table[(Int, String, Double, String, String)]("PROPERTIES") {
    def id = column[Int]("P_ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def value = column[Double]("VALUE")
    def kind = column[String]("KIND")
    def ownerID = column[String]("OWNER")
    def owner = foreignKey("OWNER_FK", ownerID, indebteds)(_.cpf)

    def * = id ~ name ~ value ~ kind ~ ownerID
  }

  def populateDb() = {

    db withSession {
      var a = new java.util.Date()

      if (!MTable.getTables.list.exists(_.name.name == indebteds.tableName)) {
        (indebteds.ddl ++ properties.ddl).create

        indebteds.insertAll(
          ("01111111111", "Gringo", a, 15000.50),
          ("02222222222", "Mortimer", a, 2800.70),
          ("03333333333", "Tuco", a, 550.30),
          ("04444444444", "Jerusa Valente", a, 19200.50),
          ("05555555555", "Douglas 10 Imortal Tricolor", a, 503810.38),
          ("06666666666", "Joao, o Pesquisador Usuario", a, 10.50)
        )

        properties.insert(PropID, "Onibus da Carris",
          35000.50, "Vehicle", "01111111111")
        PropID = PropID + 1
        properties.insert(PropID, "Bicicleta Sundown",
          20.00, "Vehicle", "02222222222")
        PropID = PropID + 1
        properties.insert(PropID, "Biju",
          10.50, "Jewel", "03333333333")
        PropID = PropID + 1
        properties.insert(PropID, "Skate motorizado",
          50000.00, "Vehicle", "04444444444")
        PropID = PropID + 1
        properties.insert(PropID, "Acordeao",
          500.00, "Other", "05555555555")
        PropID = PropID + 1
        properties.insert(PropID, "Apartamento Duplex Power Plus",
          300000.00, "Realty", "06666666666")
        PropID = PropID + 1
      }
    }
  }

  def queryIndebted(indebtedCPF : String) : Boolean = {
    var found: Boolean = false

    db withSession {
      if (!MTable.getTables.list.exists(_.name.name == indebteds.tableName))
        indebteds.ddl.create

      val query =
        for {
            i <- indebteds if i.cpf === indebtedCPF
        } yield i.cpf

      if (query.list.size > 0)
        found = true
    }
    return found
  }

  def addIndebted(name : String, birthDay : java.util.Date,
                  debt : Double, cpf : String) = {

    db withSession {

      if (!MTable.getTables.list.exists(_.name.name == indebteds.tableName)) {
        indebteds.ddl.create
        indebteds.insert(cpf, name, birthDay, debt)
      }
      else
        indebteds.insert(cpf, name, birthDay, debt)
    }
  }

  def addProperty(cpf : String, propertyName : String,
                  value : Double, kind : String) = {

    db withSession {
      if (!MTable.getTables.list.exists(_.name.name == properties.tableName))
        properties.ddl.create

      val query =
        for {
            i <- indebteds if i.cpf === cpf
        } yield i.cpf

      PropID = PropID + 1
      properties.insert(PropID, propertyName, value, kind , query.list.head)
    }
  }

  def queryProperty(indebtedCPF : String, propertyName : String): Boolean = {
    var found: Boolean = true

    db withSession {
      if (!MTable.getTables.list.exists(_.name.name == properties.tableName)) {
        properties.ddl.create
        return false
      }

      val query =
        for {
          i <- properties
          if i.ownerID === indebtedCPF && i.name === propertyName
        } yield i.id

      if (query.list.size == 0)
        found = false
    }

    found
  }

  def getIndebteds() : List[Indebted] = {
    var loIndebteds : List[Indebted] = Nil

    db withSession {
      if (!MTable.getTables.list.exists(_.name.name == indebteds.tableName)) {
        indebteds.ddl.create
        return Nil
      }
      Query(indebteds) foreach {
        case (cpf, name, bdate, debt) =>
          loIndebteds = new Indebted(name, bdate, debt, cpf) :: loIndebteds
      }
    }

    loIndebteds
  }

  def getProperties() : List[Property] = {
    var loProperties : List[Property] = Nil

    db withSession {

      if (!MTable.getTables.list.exists(_.name.name == properties.tableName)) {
        properties.ddl.create
        return Nil
      }

      Query(properties) foreach {
        case (id, name, value, kind, owner) =>
          loProperties = new
              Property(name, value, PropertyKind.withName(kind)) :: loProperties
      }
    }

    loProperties
  }

  def addAuction(auction: Auction) = {

  }

  def addUser(client: Client) = {

  }

  def addUser(manager: Manager) = {

  }

  def queryScheduledAuctions : List[Auction] = {
      Nil
  }

  def queryOpenAuctions : List[Auction] = {
      Nil
  }

  def queryClosedAuctions : List[Auction] = {
      Nil
  }
}