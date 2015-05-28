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

    val users = new Table[(String, String, String, Option[String],
        Option[java.util.Date], Option[String], Option[String],
        Option[String], Int)]("USERS") {

        def userName = column[String]("U_NAME", O.PrimaryKey, O.NotNull)
        def passWord = column[String]("PASSWORD", O.NotNull)
        def name = column[String]("R_NAME", O.NotNull)
        def cpf = column[String]("CPF", O.Nullable)
        def bdate = column[java.util.Date]("BIRTHDATE", O.Nullable)
        def telephone = column[String]("TELEPHONE", O.Nullable)
        def address = column[String]("ADDRESS", O.Nullable)
        def email = column[String]("EMAIL", O.Nullable)
        def usrLevel = column[Int]("U_LVL", O.NotNull)

        def * = userName ~ passWord ~ name ~
            cpf.? ~ bdate.? ~ telephone.? ~ address.? ~ email.? ~ usrLevel
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
            Table[(Option[Long], String, Double, String, String,
                Int)]("PROPERTIES"){

        def id = column[Long]("PROP_ID", O.PrimaryKey, O.AutoInc, O.NotNull)
        def name = column[String]("NAME", O.NotNull)
        def value = column[Double]("VALUE", O.NotNull)
        def kind = column[String]("KIND", O.NotNull)
        def ownerID = column[String]("OWNER", O.NotNull)
        def boughtIn = column[Int]("BOUGHT_IN", O.NotNull)

        def ownerIDKey = foreignKey("OWNER_FK", ownerID, indebteds)(_.cpf)

        def * = id.? ~ name ~ value ~ kind ~ ownerID ~ boughtIn
    }

    val auctions = new Table[(Option[Long], java.util.Date, java.util.Date,
        Boolean, String, Long)]("AUCTION") {

        def auctionId = column[Long]("AUCT_ID", O.PrimaryKey, O.AutoInc, O
            .DBType("serial"))
        def begin = column[java.util.Date]("BEGIN", O.NotNull)
        def end = column[java.util.Date]("END", O.NotNull)
        def open = column[Boolean]("OPEN", O.NotNull)
        def indebted = column[String]("INDEBTED", O.NotNull)
        def property = column[Long]("PROPERTY", O.NotNull)

        def indebtedKey = foreignKey("INDEBTED_FK", indebted, indebteds)(_.cpf)
        def propertyKey = foreignKey("PROP_FK", property, properties)(_.id)

        def * = auctionId.? ~ begin ~ end ~ open ~ indebted ~
            property
    }

    val userBids = new
            Table[(Option[Long], Long, String, Double)]("USER_AUCTIONS") {

        def userBidID = column[Long]("UA_ID", O.PrimaryKey, O.NotNull, O
            .AutoInc, O.DBType("serial"))
        def auctionID = column[Long]("AUCTION_ID", O.NotNull)
        def userID = column[String]("USER_ID", O.NotNull)
        def value = column[Double]("VALUE", O.NotNull)

        def auctionKey = foreignKey("AUCTION_FK", auctionID, auctions)(_.auctionId)
        def userKey = foreignKey("USER_FK", userID, users)(_.userName)

        def * = userBidID.? ~ auctionID ~ userID ~ value
    }

    def populateDb() = {

        db withSession {
            val a = new java.util.Date()
            val b = new java.util.Date(2015, 9, 25)

            if (!MTable.getTables.list.exists(_.name.name == indebteds
                .tableName) && (!MTable.getTables.list.exists(_.name.name ==
                properties.tableName)) && (!MTable.getTables.list.exists(_.name.
                name == auctions.tableName)) && (!MTable.getTables.list.exists
                (_.name.name == users.tableName)) && (!MTable.getTables.list
                .exists(_.name.name == userBids.tableName))) {

                users.ddl.create
                users.insertAll(
                    ("lpsilvestrin", "123", "Luis", None, None, None, None,
                        None, 0),
                    ("prlanzarin", "123", "Paulo", None, None, None, None,
                        None, 0),
                    ("mhbackes", "123", "Marcos", None, None, None, None,
                        None, 0),
                    ("rgherdt", "123", "Ricardo", None, None, None, None,
                        None, 0)
                )

                users.insertAll(
                    ("maurilio", "123", "Maurilio Santiago", Some("12345678901"),
                        Some(a), Some("12345600"), Some("Rua do Papagaio"),
                        Some("maurilio@lapaz.com"), 1),
                    ("mariella", "123", "Mariella Santiago", Some
                        ("12345678902"), Some(a), Some("12345601"),
                        Some("Rua da Anta"), Some("mariella@lapaz.com"), 1),
                    ("chimerito", "123", "Chimerito Santiago", Some("12345678903"),
                        Some(a), Some("12345602"), Some("Rua do Mamute"),
                        Some("chimerito@lapaz.com"), 1)
                )

                indebteds.ddl.create
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

                properties.ddl.create
                properties.insert(Some(1L), "Onibus da Carris", 35000.50,
                    PropertyKind.VEHICLE.toString, "01111111111", 2001)
                properties.insert(Some(2L), "Bicicleta Sundown", 20.00,
                    PropertyKind.VEHICLE.toString, "02222222222", 2002)
                properties.insert(Some(3L), "Oculos OAKLEY phoda", 10.50,
                    PropertyKind.OTHER.toString, "03333333333", 2003)
                properties.insert(Some(4L), "Skate motorizado", 50000.00,
                    PropertyKind.VEHICLE.toString, "04444444444", 2004)
                properties.insert(Some(5L), "TOURO NELORI BOA PINTA", 500.00,
                    PropertyKind.OTHER.toString, "05555555555", 2005)
                properties.insert(Some(6L), "Apartamento Duplex Power Plus",
                    300000.00, PropertyKind.REALTY.toString, "06666666666", 2006)
                properties.insert(Some(7L), "Apartamento Triplex Power Max",
                    500000.00, PropertyKind.REALTY.toString, "06666666666", 2007)

                auctions.ddl.create
                auctions.insertAll(
                    (None, a, b, true, "01111111111", 1L),
                    (None, a, b, false, "02222222222", 2L),
                    (None, a, b, true, "03333333333", 3L)
                )

                userBids.ddl.create
                userBids.insertAll(
                    (None, 1L, "maurilio", 50000.00),
                    (None, 1L, "chimerito", 50001.00),
                    (None, 2L, "chimerito", 50.00),
                    (None, 3L, "mariella", 992.39)
                )
            }
        }
    }

    def addUser(client: Client) = {
        db withSession {
            MTable.getTables(users.tableName).firstOption foreach(
                MTable => {
                    users.insert(client.userName, client.password, client.name,
                        Some(client.CPF), Some(client.birthDay),
                        Some(client.telephone), Some(client.address),
                        Some(client.email), 1)})
        }
    }

    def addUser(manager: Manager) = {
        db withSession {
            MTable.getTables(users.tableName).firstOption foreach(
                MTable => {users.insert(manager.userName, manager.password,
                    manager.name, None, None, None, None, None, 0)})
        }
    }

    def addIndebted(name : String, birthDay : java.util.Date,
                    debt : Double, cpf : String) = {
        db withSession {
            MTable.getTables(auctions.tableName).firstOption foreach(
                MTable => {indebteds.insert(cpf, name, birthDay, debt)})
        }
    }

    def addProperty(cpf : String, propertyName : String,
                    value : Double, kind : String, boughtIn : Int) = {
        lazy val dbQuery = for { i <- indebteds if i.cpf === cpf } yield i.cpf

        db withSession {
            MTable.getTables(auctions.tableName).firstOption  foreach(
                MTable => {properties.insert(None, propertyName, value, kind,
                    dbQuery.list.head, boughtIn)}
                )
        }
    }

    def addAuction(auction: Auction) = {
        lazy val dbQuery =
            for {
                i <- properties if i.name === auction.property.name &&
                i.ownerID === auction.indebted.cpf
            } yield i.id

        db withSession {
            MTable.getTables(auctions.tableName).firstOption foreach(
                MTable => auctions.insert(None, auction.begin, auction.end,
                    auction.open, auction.indebted.cpf, dbQuery.list.head)
                )
        }
    }

    def updateAuction(auction : Auction) = {
        lazy val dbQuery =
            for {
                a <- auctions if a.auctionId === auction.auctionID
            } yield a.open

        db withSession {
            MTable.getTables(auctions.tableName).firstOption foreach(
                MTable => dbQuery.update(auction.open)
                )
        }
    }

    def makeBid(bid : Bid) = {
        lazy val dbQuery = for {
            b <- userBids if b.auctionID === bid.auction &&
            b.userID === bid.client.userName
        } yield b.value

        db withSession {
            println(dbQuery.list().head)
            MTable.getTables(userBids.tableName).firstOption foreach(
                MTable => dbQuery.list.isEmpty match {
                    case false => dbQuery.update(bid.value)
                    case true => userBids.insert(None, bid.auction,
                        bid.client.userName, bid.value)
                }
                )
        }
    }

    def queryIndebted(indebtedCPF: String): Option[Indebted] = {
        lazy val dbQuery = for {
            i <- indebteds if i.cpf === indebtedCPF
        } yield i.*

        queryIndebted(dbQuery)
    }

    def queryIndebted(property : Property) : Option[Indebted] = {
        lazy val dbQuery = for {
            p <- properties
            i <- indebteds if p.id === property.propertyID && p.ownerID === i.cpf
        } yield i.*

        queryIndebted(dbQuery)
    }

    def queryPropertyOwners(loProperties : List[Property]) : List[(Property,
        Indebted)] = {
        lazy val dbQuery = loProperties map {
            elm => for {
                p <- properties if p.id === elm.propertyID
                i <- indebteds if p.ownerID === i.cpf
            } yield (i.*, p.*)
        }
        db withSession {
            MTable.getTables(indebteds.tableName) firstOption match {
                case Some(x) => dbQuery map {
                    dbqe => dbqe.first match {
                        case((cpf, iname, bdate, debt), (id, pname, value,
                        kind, owner, boughtIn)) => (Property(pname, value,
                            PropertyKind.withName(kind), boughtIn, id),
                            Indebted(iname, bdate, debt, cpf))
                    }
                }
                case None =>
                    indebteds.ddl.create
                    Nil
            }
        }
    }

    def queryProperty(indebtedCPF : String, propertyName : String):
    Option[Property] = {
        lazy val dbQuery = for {
            p <- properties if p.ownerID === indebtedCPF && p.name ===
            propertyName
        } yield p.*

        queryProperty(dbQuery)
    }

    def queryProperty(propertyID : Long): Option[Property] = {
        lazy val dbQuery = for {
            p <- properties if p.id === propertyID
        } yield p.*

        queryProperty(dbQuery)
    }

    def queryProperties(propertyKind : Option[String], auctionStatus :
    Option[Boolean]): List[Property] = {
        lazy val dbQueryT = for {
            a <- auctions
            p <- properties if a.property === p.id && a.open === true
        } yield p.*
        lazy val dbQueryF = for {
            a <- auctions
            p <- properties if a.property === p.id && a.open === false
        } yield p.*

        auctionStatus match {
            case Some(x) => x match {
                case true => queryProperties(dbQueryT).filter(q =>
                    propertyKind.map(q.kind.toString == _).getOrElse(true))
                case false => queryProperties(dbQueryF).filter(q =>
                    propertyKind.map(q.kind.toString == _).getOrElse(true))
            }
            case None => getProperties.filter(q =>
                propertyKind.map(q.kind.toString == _).getOrElse(true))
        }
    }

    def queryIndebtedProperties(indebtedCPF : String) : List[Property] = {
        lazy val dbQuery = for {
            p <- properties if p.ownerID === indebtedCPF
        } yield p.*

        queryProperties(dbQuery)
    }

    def queryUser(login : String ,password : String) : Option[User] = {
        lazy val dbQuery = for {
            m <- users if m.userName === login && m.passWord ===
            password
        } yield m.*

        db withSession {
            MTable.getTables(users.tableName).firstOption flatMap(
                MTable =>
                    dbQuery.firstOption map {
                        case (userName, passWord, name, cpf, bdate,
                        telephone, address, email, usrLevel) =>
                            usrLevel match {
                                case 0 => new Manager(userName, passWord, name)
                                case 1 => new Client(userName, passWord, name,
                                    cpf.get, bdate.get, telephone.get,
                                    address.get, email.get)
                            }
                    }
                )
        }
    }

    def queryUser(manager: Manager) : Option[Manager] = {
        lazy val dbQuery = for {
            m <- users if m.userName === manager.userName && m.passWord ===
            manager.password && m.usrLevel === 0
        } yield m.*

        db withSession {
            MTable.getTables(users.tableName).firstOption flatMap(
                MTable =>
                    dbQuery.firstOption map {
                        case (userName, passWord, name, cpf, bdate,
                        telephone, address, email, usrLevel) =>
                            new Manager(userName, passWord, name)
                    }
                )
        }
    }

    def queryUser(client: Client) : Option[Client] = {
        lazy val dbQuery = for {
            c <- users if c.userName === client.userName && c.passWord ===
            client.password && c.usrLevel === 1
        } yield c.*

        db withSession {
            MTable.getTables(users.tableName).firstOption flatMap(
                MTable =>
                    dbQuery.firstOption map {
                        case (userName, passWord, name, cpf, bdate,
                        telephone, address, email, usrLevel) =>
                            new Client(userName, passWord, name, cpf.get,
                                bdate.get, telephone.get, address.get, email.get)
                    }
                )
        }
    }

    def queryClientAuctions(client : Client) : List[Auction] = {
        lazy val dbQuery = for {
            ub <- userBids if ub.userID === client.userName
        } yield ub.*

        db withSession {
            MTable.getTables(userBids.tableName) firstOption match {
                case Some(x) => dbQuery.list map {
                    case (ubID, auctionID, userID, value) =>
                        queryAuction(auctionID).get
                }
                case None =>
                    (users.ddl ++ auctions.ddl ++ userBids.ddl).create
                    Nil
            }
        }
    }

    def queryClientBids(client : Client) : List[Bid] = {
        lazy val dbQuery = for {
            ub <- userBids if ub.userID === client.userName
        } yield ub.*

        queryBids(dbQuery)
    }

    def queryAuctionBids(auctID : Long) : List[Bid] = {
        lazy val dbQuery = for {
            ub <- userBids if ub.auctionID === auctID
        } yield ub.*

        queryBids(dbQuery)
    }

    def queryAuction(auctID : Long) : Option[Auction] = {
        lazy val dbQuery = for {
            a <- auctions if a.auctionId === auctID
        } yield a.*

        db withSession {
            MTable.getTables(users.tableName).firstOption flatMap(
                MTable =>
                    dbQuery.firstOption flatMap {
                        case (id, begin, end, open, indebted, property) =>
                            for {
                                dbi <- Database.queryIndebted(indebted)
                                dbp <- Database.queryProperty(property)
                            } yield Auction(dbi, dbp, begin, end,
                                queryHighestBid(id.get), open, id)
                    }
                )
        }
    }

    def queryHighestBid(auctionID : Long) : Option[Bid] = {

        db withSession {
            max(queryAuctionBids(auctionID))
        }
    }

    def getIndebteds : List[Indebted] = {
        lazy val dbQuery = Query(indebteds).list
        db withSession {
            MTable.getTables(indebteds.tableName) firstOption match {
                case Some(x) => dbQuery map {
                    case(cpf, name, bdate, debt) => Indebted(name, bdate,
                        debt, cpf)
                }
                case None =>
                    indebteds.ddl.create
                    Nil
            }
        }
    }

    def getProperties : List[Property] = {
        lazy val dbQuery = for {
            p <- properties
        } yield p.*

        queryProperties(dbQuery)
    }

    def getAuctions : List[Auction] = {
        lazy val dbQuery = for {
            a <- auctions
        } yield a.*

        queryAuctions(dbQuery)
    }


    def getOpenAuctions : List[Auction] = {
        lazy val dbQuery = for {
            a <- auctions if a.open === true
        } yield a.*

        queryAuctions(dbQuery)
    }

    def getClosedAuctions : List[Auction] = {
        lazy val dbQuery = for {
            a <- auctions if a.open === false
        } yield a.*

        queryAuctions(dbQuery)

    }

    private def queryUser(id : String) :  Option[Client] = {
        lazy val dbQuery = for {
            c <- users if c.userName === id
        } yield c.*

        db withSession {
            MTable.getTables(users.tableName).firstOption flatMap(
                MTable =>
                    dbQuery.firstOption map {
                        case (userName, passWord, name, cpf, bdate,
                        telephone, address, email, usrLevel) =>
                            new Client(userName, passWord, name, cpf.get,
                                bdate.get, telephone.get, address.get, email.get)
                    }
                )
        }
    }

    private def queryAuctions(dbQuery : Query[Projection6[Option[Long], java.util.Date,
        java.util.Date, Boolean, String, Long], auctions.TableType]): List[Auction] = {

        db withSession {
            MTable.getTables(auctions.tableName) firstOption match {
                case Some(x) => dbQuery.list flatMap {
                    case (id, begin, end, open, indebted, property) =>
                        for {
                            dbi <- Database.queryIndebted(indebted)
                            dbp <- Database.queryProperty(property)
                        } yield Auction(dbi, dbp, begin, end,
                            queryHighestBid(id.get), open, id)
                }
                case None =>
                    (indebteds.ddl ++ properties.ddl ++ auctions.ddl).create
                    Nil
            }
        }
    }

    private def queryBids(dbQuery : Query[Projection4[Option[Long], Long,
        String, Double], userBids.TableType]): List[Bid] = {

        db withSession {
            MTable.getTables(userBids.tableName) firstOption match {
                case Some(x) => dbQuery.list map {
                    case (userBidID, auctionID, userID, value) =>
                        Bid(auctionID, queryUser(userID).get, value)
                }
                case None =>
                    userBids.ddl.create
                    Nil
            }
        }
    }

    private def queryProperties(dbQuery : Query[Projection6[Option[Long],
        String, Double, String, String, Int], properties.TableType])
    : List[Property] = {

        db withSession {
            MTable.getTables(properties.tableName) firstOption match {
                case Some(x) => dbQuery.list map {
                    case (id, name, value, kind, owner, boughtIn) =>
                        Property (name, value, PropertyKind.withName(kind),
                            boughtIn, id)
                }
                case None =>
                    (indebteds.ddl ++ properties.ddl).create
                    Nil
            }
        }
    }

    private def queryIndebted(dbQuery : Query[Projection4[String, String,
        java.util.Date, Double], indebteds.TableType]) : Option[Indebted] = {
        db withSession {
            MTable.getTables(indebteds.tableName).firstOption flatMap(
                MTable =>
                    dbQuery.firstOption map {
                        case (cpf, name, bdate, debt) =>
                            Indebted(name, bdate, debt, cpf,
                                queryIndebtedProperties(cpf))
                    }
                )
        }
    }

    private def queryProperty(dbQuery : Query[Projection6[Option[Long],
        String, Double, String, String, Int], properties.TableType])
    : Option[Property] = {
        db withSession {
            MTable.getTables(properties.tableName).firstOption flatMap(
                MTable =>
                    dbQuery.firstOption map {
                        case (id, name, value, kind, ownerID, boughtIn) =>
                            Property(name, value, PropertyKind.withName(kind),
                                boughtIn, id)
                    }
                )
        }
    }

    private def max(lob : List[Bid]) : Option[Bid] = lob match {
        case Nil => None
        case List(b: Bid) => Some(b)
        case h :: h1 :: t => max((if (h.value > h1.value) h else h1) :: t)
    }
}