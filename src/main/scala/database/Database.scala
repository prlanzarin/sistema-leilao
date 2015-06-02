package database

import java.util
import java.util.Date

import business.entities._

import org.scalaquery.meta.MTable
import org.scalaquery.session.Database.threadLocalSession
import org.scalaquery.ql._
import org.scalaquery.ql.TypeMapper._
import org.scalaquery.ql.extended.H2Driver.Implicit._
import org.scalaquery.ql.extended.{ExtendedTable => Table}

class DBUser extends Table[(String, String, String, Option[String],
    Option[java.util.Date], Option[String], Option[String],
    Option[String], Int)]("USERS") {
    implicit val JavaUtilDateTypeMapper =
        MappedTypeMapper.base[java.util.Date, Long](_.getTime,
            new java.util.Date(_))
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

class DBIndebted extends
Table[(String, String, java.util.Date, Double)]("INDEBTED") {
    implicit val JavaUtilDateTypeMapper =
        MappedTypeMapper.base[java.util.Date, Long](_.getTime,
            new java.util.Date(_))
    def cpf = column[String]("CPF", O.PrimaryKey, O.NotNull)
    def name = column[String]("NAME", O.NotNull)
    def bdate = column[java.util.Date]("BIRTHDATE", O.NotNull)
    def debt = column[Double]("DEBT", O.NotNull)

    def * = cpf ~ name ~ bdate ~ debt
}

class DBProperty(indebteds : DBIndebted) extends
Table[(Option[Long], String, Double, String, String, Int)]("PROPERTIES"){

    def id = column[Long]("PROP_ID", O.PrimaryKey, O.AutoInc, O.NotNull)
    def name = column[String]("NAME", O.NotNull)
    def value = column[Double]("VALUE", O.NotNull)
    def kind = column[String]("KIND", O.NotNull)
    def ownerID = column[String]("OWNER", O.NotNull)
    def boughtIn = column[Int]("BOUGHT_IN", O.NotNull)

    def ownerIDKey = foreignKey("OWNER_FK", ownerID, indebteds)(_.cpf)

    def * = id.? ~ name ~ value ~ kind ~ ownerID ~ boughtIn
}

class DBAuction(val indebteds : DBIndebted, val properties : DBProperty)
    extends Table[(Option[Long], java.util.Date, java.util.Date, String,
        Long)]("AUCTION") {
    implicit val JavaUtilDateTypeMapper =
        MappedTypeMapper.base[java.util.Date, Long](_.getTime,
            new java.util.Date(_))
    def auctionId = column[Long]("AUCT_ID", O.PrimaryKey, O.AutoInc, O
        .DBType("serial"))
    def begin = column[java.util.Date]("BEGIN", O.NotNull)
    def end = column[java.util.Date]("END", O.NotNull)
    def indebted = column[String]("INDEBTED", O.NotNull)
    def property = column[Long]("PROPERTY", O.NotNull)

    def indebtedKey = foreignKey("INDEBTED_FK", indebted, indebteds)(_.cpf)
    def propertyKey = foreignKey("PROP_FK", property, properties)(_.id)

    def * = auctionId.? ~ begin ~ end ~ indebted ~ property
}

class DBUserBids(val auctions : DBAuction, val users : DBUser) extends
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

object Database {

    implicit val JavaUtilDateTypeMapper =
        MappedTypeMapper.base[java.util.Date, Long](_.getTime,
            new java.util.Date(_))

    val db = org.scalaquery.session.Database.
        forURL("jdbc:h2:file:./db/auctiondb", driver = "org.h2.Driver")

    val users = new DBUser

    val indebteds = new DBIndebted

    val properties = new DBProperty(indebteds)

    val auctions = new DBAuction(indebteds, properties)

    val userBids = new DBUserBids(auctions, users)

    def initialize() = {

        db withSession {
            val a = new java.util.Date()
            val b = new java.util.Date(2015, 9, 25)

            MTable.getTables(users.tableName).firstOption match {
                case None =>
                    users.ddl.create
                    users.insertAll(
                        ("lpsilvestrin", "123", "Luis", None, None, None, None,
                            None, 0),
                        ("prlanzarin", "123", "Paulo", None, None, None, None,
                            None, 0),
                        ("mhbackes", "123", "Marcos", None, None, None, None,
                            None, 0),
                        ("rgherdt", "123", "Ricardo", None, None, None, None,
                            None, 0))

                    users.insertAll(
                        ("maurilio", "123", "Maurilio Santiago", Some
                            ("12345678901"), Some(a), Some("12345600"), Some
                            ("Rua do Papagaio"), Some("maurilio@lapaz.com"), 1),
                        ("mariella", "123", "Mariella Santiago", Some
                            ("12345678902"), Some(a), Some("12345601"), Some
                            ("Rua da Anta"), Some("mariella@lapaz.com"), 1),
                        ("chimerito", "123", "Chimerito Santiago", Some
                            ("12345678903"), Some(a), Some("12345602"), Some
                            ("Rua do Mamute"), Some("chimerito@lapaz.com"), 1))
                case _ =>
            }

            MTable.getTables(indebteds.tableName).firstOption match {
                case None =>
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
                        ("08888888888", "E Fabiano", a, 9999.333))
                case _ =>
            }

            MTable.getTables(properties.tableName).firstOption match {
                case None =>
                    properties.ddl.create
                    properties.insertAll(
                        (Some(1L), "Onibus da Carris", 35000.50,
                            PropertyKind.VEHICLE.toString, "01111111111", 2001),
                        (Some(2L), "Bicicleta Sundown", 20.00,
                            PropertyKind.VEHICLE.toString, "02222222222", 2002),
                        (Some(3L), "Oculos OAKLEY phoda", 10.50,
                            PropertyKind.OTHER.toString, "03333333333", 2003),
                        (Some(4L), "Skate motorizado", 50000.00,
                            PropertyKind.VEHICLE.toString, "04444444444", 2004),
                        (Some(5L), "TOURO NELORI BOA PINTA", 500.00,
                            PropertyKind.OTHER.toString, "05555555555", 2005),
                        (Some(6L), "Apartamento Duplex Power Plus",
                            300000.00, PropertyKind.REALTY.toString, "06666666666",
                            2006),
                        (Some(7L), "Apartamento Triplex Power Max",
                            500000.00, PropertyKind.REALTY.toString, "06666666666",
                            2007))
                case _ =>
            }

            MTable.getTables(auctions.tableName).firstOption match {
                case None =>
                    auctions.ddl.create
                    auctions.insertAll(
                        (None, a, b, "01111111111", 1L),
                        (None, a, b, "02222222222", 2L),
                        (None, a, b, "03333333333", 3L))
                case _ =>
            }

            MTable.getTables(userBids.tableName).firstOption match {
                case None =>
                    userBids.ddl.create
                    userBids.insertAll(
                        (None, 1L, "maurilio", 50000.00),
                        (None, 1L, "chimerito", 50001.00),
                        (None, 2L, "chimerito", 50.00),
                        (None, 3L, "mariella", 992.39))
                case _ =>
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
                        Some(client.email), 1)}
                ) orElse initialize()
        }
    }

    def addUser(manager: Manager) = {
        db withSession {
            MTable.getTables(users.tableName).firstOption foreach(
                MTable => {users.insert(manager.userName, manager.password,
                    manager.name, None, None, None, None, None, 0)}
                ) orElse initialize()
        }
    }

    def addIndebted(name : String, birthDay : java.util.Date,
                    debt : Double, cpf : String) = {
        db withSession {
            MTable.getTables(indebteds.tableName).firstOption foreach(
                MTable => {indebteds.insert(cpf, name, birthDay, debt)}
                ) orElse initialize()
        }
    }

    def addProperty(cpf : String, propertyName : String,
                    value : Double, kind : String, boughtIn : Int) = {
        lazy val dbQuery = for { i <- indebteds if i.cpf === cpf } yield i.cpf

        db withSession {
            MTable.getTables(auctions.tableName).firstOption  foreach(
                MTable => {properties.insert(None, propertyName, value, kind,
                    dbQuery.list.head, boughtIn)}
                ) orElse initialize()
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
                    auction.indebted.cpf, dbQuery.list.head)
                ) orElse initialize()
        }
    }

    def addBid(uid : String, aid : Long, value : Double) = {
        lazy val dbQuery = for {
            b <- userBids if b.auctionID === aid &&
            b.userID === uid
        } yield b.value

        db withSession {
            MTable.getTables(userBids.tableName).firstOption foreach(
                MTable => dbQuery.list.isEmpty match {
                    case false => dbQuery.update(value)
                    case true => userBids.insert(None, aid, uid, value)
                }) orElse initialize()
        }
    }

    def cancelBid(uid : String, aid : Long, value : Double) = {
        lazy val dbQuery = for {
            b <- userBids if b.auctionID === aid &&
            b.userID === uid && b.value === value
        } yield b

        db withSession {
            MTable.getTables(userBids.tableName).firstOption foreach(
                MTable => dbQuery.list.isEmpty match {
                    case false => dbQuery.mutate(b => b.delete())
                }) orElse initialize()
        }
    }

    def updateAuction(aid : Long) = {
        lazy val dbQuery =
            for {
                a <- auctions if a.auctionId === aid
            } yield a.end

        db withSession {
            MTable.getTables(auctions.tableName).firstOption foreach(
                MTable => dbQuery.update(new Date())
                ) orElse initialize()
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
                case None => initialize()
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
            p <- properties if a.property === p.id
        } yield p.*
        lazy val dbQueryF = for {
            a <- auctions
            p <- properties if !auctions.filter(a => a.property === p.id).exists
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

    def queryOpenAuctions(propertyName : Option[String], propertyKind :
    Option[String]): List[Auction] = {
        lazy val dbQuery = for {
            p <- properties if p.name === propertyName
            a <- auctions if a.property === p.id && a.begin <= new util.Date() &&
            a.end >= new util.Date()
        } yield a.*

        propertyName match {
            case Some(x) => queryAuctions(dbQuery).filter(a =>
                propertyKind.map(a.property.kind.toString == _).getOrElse(true))
            case None => getOpenAuctions.filter(a =>
                propertyKind.map(a.property.kind.toString == _).getOrElse(true))
        }
    }

    def queryClosedAuctions(propertyName : Option[String], propertyKind :
    Option[String]): List[Auction] = {
        lazy val dbQuery = for {
            p <- properties if p.name === propertyName
            a <- auctions if a.property === p.id && a.end < new util.Date()
        } yield a.*

        propertyName match {
            case Some(x) => queryAuctions(dbQuery).filter(a =>
                propertyKind.map(a.property.kind.toString == _).getOrElse(true))
            case None => getClosedAuctions.filter(a =>
                propertyKind.map(a.property.kind.toString == _).getOrElse(true))
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
            m <- users if m.userName === login && m.passWord === password
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
                ) orElse { initialize(); None }
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
                ) orElse { initialize(); None }
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
                ) orElse { initialize(); None }
        }
    }

    def queryUser(id : String) :  Option[Client] = {
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
                ) orElse { initialize(); None }
        }
    }

    def queryClientBidHistory(client : Client) : List[(Auction, Bid)] = {
        lazy val dbQuery = for {
            ub <- userBids if ub.userID === client.userName
        } yield ub.*
        db withSession {
            MTable.getTables(indebteds.tableName) firstOption match {
                case Some(x) => dbQuery.list map {
                    case (ubid, aID, uID, value) =>
                        (queryAuction(aID).get, Bid(aID, queryUser(uID).get,
                            value))
                }
                case None => initialize()
                    Nil
            }
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
                case None => initialize()
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
                        case (id, begin, end, indebted, property) =>
                            for {
                                dbi <- Database.queryIndebted(indebted)
                                dbp <- Database.queryProperty(property)
                            } yield Auction(dbi, dbp, begin, end,
                                queryHighestBid(id.get), id,
                                Some(countAuctionBids(id.get)))
                    }
                ) orElse { initialize(); None }
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
                case None => initialize()
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
            a <- auctions if a.begin <= new util.Date() && a.end >= new util
        .Date()
        } yield a.*

        queryAuctions(dbQuery)
    }

    def getClosedAuctions : List[Auction] = {
        lazy val dbQuery = for {
            a <- auctions if a.end < new util.Date()
        } yield a.*

        queryAuctions(dbQuery)

    }

    private def queryAuctions(dbQuery : Query[Projection5[Option[Long], java.util.Date,
        java.util.Date, String, Long], auctions.TableType]): List[Auction] = {

        db withSession {
            MTable.getTables(auctions.tableName) firstOption match {
                case Some(x) => dbQuery.list flatMap {
                    case (id, begin, end, indebted, property) =>
                        for {
                            dbi <- Database.queryIndebted(indebted)
                            dbp <- Database.queryProperty(property)
                        } yield Auction(dbi, dbp, begin, end,
                            queryHighestBid(id.get), id,
                            Some(countAuctionBids(id.get)))
                }
                case None => initialize()
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
                case None => initialize()
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
                case None => initialize()
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
                ) orElse { initialize(); None }
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
                ) orElse { initialize(); None }
        }
    }

    private def countAuctionBids(auctionID : Long) : Int = {
        lazy val dbQuery = for {
            ub <- userBids if ub.auctionID === auctionID
        } yield ub.userBidID.count

        db withSession {
            MTable.getTables(userBids.tableName).firstOption match {
                case Some(x) => dbQuery.list.head
                case None => initialize()
                    dbQuery.list.head
            }
        }
    }

    private def max(lob : List[Bid]) : Option[Bid] = lob match {
        case Nil => None
        case List(b: Bid) => Some(b)
        case h :: h1 :: t => max((if (h.value > h1.value) h else h1) :: t)
    }
}