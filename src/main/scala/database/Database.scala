package database

import business.entities._

import org.scalaquery.meta.MTable
import org.scalaquery.session.Database.threadLocalSession
import org.scalaquery.ql._
import org.scalaquery.ql.TypeMapper._
import org.scalaquery.ql.extended.H2Driver.Implicit._
import org.scalaquery.ql.extended.{ExtendedTable => Table}

object Database {

    implicit val JavaUtilDateTypeMapper =
        MappedTypeMapper.base[java.util.Date, Long](_.getTime,
            new java.util.Date(_))

    val db = org.scalaquery.session.Database.
        forURL("jdbc:h2:file:./db/auctiondb", driver = "org.h2.Driver")

    val auctions = new Table[(java.util.Date,
        java.util.Date, Double,
        Boolean, String, Long)]("AUCTION") {

        def auctionId = column[Long]("AUCT_ID", O.PrimaryKey, O.AutoInc, O
            .DBType("serial"))
        def begin = column[java.util.Date]("BEGIN", O.NotNull)
        def end = column[java.util.Date]("END", O.NotNull)
        def highestBid = column[Double]("H_BID", O.Nullable)
        def open = column[Boolean]("OPEN", O.NotNull)
        def indebted = column[String]("INDEBTED", O.NotNull)
        def property = column[Long]("PROP_ID", O.NotNull)

        def indebtedKey = foreignKey("INDEBTED_FK", indebted, indebteds)(_.cpf)
        def propertyKey = foreignKey("PROP_FK", property, properties)(_.id)

        def * = begin ~ end ~
            highestBid ~ open ~ indebted ~ property
    }

    val managers = new
            Table[(Long, String, String, java.util.Date)]("MANAGER") {

        def managerID = column[Long]("MAN_ID", O.PrimaryKey, O.AutoInc, O
            .NotNull)
        def userName = column[String]("NAME", O.NotNull)
        def passWord = column[String]("PASSWORD", O.NotNull)
        def name = column[java.util.Date]("NAME", O.NotNull)

        def * = managerID ~ userName ~ passWord ~ name
    }

    val clients = new Table[(Long, String, String, java.util.Date,
        String, java.util.Date, String, String,
        String)]("CLIENT") {

        def clientID = column[Long]("CLT_ID", O.PrimaryKey, O.AutoInc, O
            .NotNull)
        def userName = column[String]("NAME", O.NotNull)
        def passWord = column[String]("PASSWORD", O.NotNull)
        def name = column[java.util.Date]("NAME", O.NotNull)
        def cpf = column[String]("CPF", O.PrimaryKey, O.NotNull)
        def bdate = column[java.util.Date]("BIRTHDATE", O.NotNull)
        def telephone = column[String]("TELEPHONE", O.NotNull)
        def address = column[String]("ADDRESS", O.NotNull)
        def email = column[String]("EMAIL", O.NotNull)

        def * = clientID ~ userName ~ passWord ~ name ~
            cpf ~ bdate ~ telephone ~ address ~ email //~ cltAuctions
    }

    val userAuctions = new
            Table[(Long, Long)]("USER_AUCTIONS") {

        def userAuctionsID = column[Long]("UA_ID", O.PrimaryKey, O.NotNull, O
            .AutoInc, O.DBType("serial"))
        def auctionID = column[Long]("AUCTION_ID", O.NotNull)
        def userID = column[Long]("USER_ID", O.NotNull)

        def auctionKey = foreignKey("AUCTION_FK", auctionID, auctions)(_
            .auctionId)
        def userKey = foreignKey("USER_FK", userID, clients)(_.clientID)

        def * = auctionID ~ userID
    }

    val indebteds = new
            Table[(String, String, java.util.Date, Double)]("INDEBTED") {

        def cpf = column[String]("CPF", O.PrimaryKey, O.NotNull)
        def name = column[String]("NAME", O.NotNull)
        def bdate = column[java.util.Date]("BIRTHDATE", O.NotNull)
        def debt = column[Double]("DEBT", O.NotNull)

        def * = cpf ~ name ~ bdate ~ debt
    }

    val properties = new
            Table[(String, Double, String, String)]("PROPERTIES") {

        def id = column[Long]("PROP_ID", O.PrimaryKey, O.AutoInc, O.NotNull, O
            .DBType("serial"))
        def name = column[String]("NAME", O.NotNull)
        def value = column[Double]("VALUE", O.NotNull)
        def kind = column[String]("KIND", O.NotNull)
        def ownerID = column[String]("OWNER", O.NotNull)

        def ownerIDKey = foreignKey("OWNER_FK", ownerID, indebteds)(_.cpf)

        def * = name ~ value ~ kind ~ ownerID
    }

    private def hasTable(table : Table[Any]) : Boolean =
        MTable.getTables.list.exists(_.name.name == table.tableName)

    def populateDb() = {

        db withSession {
            val a = new java.util.Date()

            if (!MTable.getTables.list.exists(_.name.name == indebteds
                .tableName) && (!MTable.getTables.list.exists(_.name.name ==
                properties.tableName))) {

                (indebteds.ddl ++ properties.ddl).create
                indebteds.insertAll(
                    ("01111111111", "Siburgo Boapinta", a, 15000.50),
                    ("02222222222", "Coronel Mortelenta", a, 2800.70),
                    ("03333333333", "MC Carmen Furacao", a, 550.30),
                    ("04444444444", "Princesa Jujubas", a, 19200.50),
                    ("06666666661", "JOAO, o Devedor Usuario", a, 10.50),
                    ("05555555555", "Douglas 10 Imortal Tricolor", a, 503810.38),
                    ("06666666666", "Ze do Caixao", a, 1550.00),
                    ("09999999999", "Pe. Fabio de Mormon", a, 300.00),
                    ("07777777777", "Cesar Menotti", a, 10000.666),
                    ("08888888888", "E Fabiano", a, 9999.333)
                )

                properties.insert("Onibus da Carris",
                    35000.50, PropertyKind.VEHICLE.toString, "01111111111")
                properties.insert("Bicicleta Sundown",
                    20.00, PropertyKind.VEHICLE.toString, "02222222222")
                properties.insert("Oculos OAKLEY phoda",
                    10.50, PropertyKind.OTHER.toString, "03333333333")
                properties.insert("Skate motorizado",
                    50000.00, PropertyKind.VEHICLE.toString, "04444444444")
                properties.insert("TOURO NELORI BOA PINTA",
                    500.00, PropertyKind.OTHER.toString, "05555555555")
                properties.insert("Apartamento Duplex Power Plus",
                    300000.00, PropertyKind.REALTY.toString, "06666666666")
            }
        }
    }

    def addIndebted(name : String, birthDay : java.util.Date,
                    debt : Double, cpf : String) = {

        db withSession {
            MTable.getTables(auctions.tableName).firstOption.foreach(MTable =>
                indebteds.ddl.create)
            indebteds.insert(cpf, name, birthDay, debt)
        }
    }

    def addProperty(cpf : String, propertyName : String,
                    value : Double, kind : String) = {
        lazy val query =
            for { i <- indebteds if i.cpf === cpf } yield i.cpf

        db withSession {
            MTable.getTables(auctions.tableName).firstOption.foreach(
                MTable => {
                    indebteds.ddl.create
                    properties.ddl.create
                }
            )
            properties.insert(propertyName, value, kind , query.list.head)
        }
    }

    def addAuction(auction: Auction) = {
        lazy val query =
            for {
                i <- properties if i.name === auction.property.name &&
                i.ownerID === auction.indebted.cpf
            } yield i.id

        db withSession {
            MTable.getTables(auctions.tableName).firstOption.foreach(
                MTable => {
                    auctions.ddl.create
                    indebteds.ddl.create
                    properties.ddl.create
                }
            )
            auctions.insert(auction.begin, auction.end, auction
                .highestBid.value, auction.open, auction.indebted.cpf,
                query.list.head)
        }
    }

    def queryIndebted(indebtedCPF: String): Option[Indebted] = {
        lazy val dbQuery = for {
            i <- indebteds if i.cpf === indebtedCPF
        } yield i.*

        db withSession {
            MTable.getTables(indebteds.tableName).firstOption.flatMap(
                MTable =>
                    dbQuery.firstOption map {
                        case (cpf, name, bdate, debt) =>
                            new Indebted(name, bdate, debt, cpf)
                    }
            )
        }
    }


    def queryProperty(indebtedCPF : String, propertyName : String):
    Option[Property] = {
        lazy val dbQuery = for {
            p <- properties if p.ownerID === indebtedCPF && p.name ===
            propertyName
        } yield p.*

        db withSession {
            MTable.getTables(properties.tableName).firstOption.flatMap(
                MTable =>
                    dbQuery.firstOption map {
                        case (name, value, kind, ownerID) =>
                            new Property(name, value, PropertyKind.withName(kind))
                    }
            )
        }
    }


    def getIndebteds : List[Indebted] = {
        var loIndebteds : List[Indebted] = Nil

        db withSession {
            if (!MTable.getTables.list.exists(_.name.name == indebteds
                .tableName)) {
                indebteds.ddl.create
                return Nil
            }
            Query(indebteds) foreach {
                case (cpf, name, bdate, debt) =>
                    loIndebteds =
                        new Indebted(name, bdate, debt, cpf) :: loIndebteds
            }
        }

        loIndebteds
    }

    def getProperties : List[Property] = {
        var loProperties : List[Property] = Nil

        db withSession {

            if (!MTable.getTables.list.exists(_.name.name == properties
                .tableName)) {
                indebteds.ddl.create
                properties.ddl.create
                return Nil
            }

            Query(properties) foreach {
                case (name, value, kind, owner) =>
                    loProperties =
                        new Property(name,
                            value,
                            PropertyKind.withName(kind)) :: loProperties
            }
        }
        loProperties
    }
/*
    def getAuctions : List[Auction] = {
        var loAuctions : List[Auction] = Nil

        db withSession {

            if (!MTable.getTables.list.exists(_.name.name == auctions
                .tableName)) {
                auctions.ddl.create
                indebteds.ddl.create
                properties.ddl.create
                return Nil
            }

            Query(auctions) foreach {
                case (begin, end, highestBid, open, indebted, property) =>
                    loAuctions =
                        new Auction(indebted, begin, end) :: loAuctions
            }
        }
        loAuctions
    }
*/
    def getOpenAuctions : List[Auction] = {
        Nil
    }

    def getClosedAuctions : List[Auction] = {
        Nil
    }

    def addUser(client: Client) = {

    }

    def addUser(manager: Manager) = {

    }
}