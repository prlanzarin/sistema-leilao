package main.scala.presentation.controller

import java.io._
import java.net.{InetAddress, Socket, SocketException}
import java.util.Date

import business.entities._

object Connection {
  var ia: InetAddress = null
  var socket: Socket = null
  var in: ObjectInputStream = null
  var out: ObjectOutputStream = null

  @throws(classOf[IOException])
  @throws(classOf[SocketException])
  def init {
    ia = InetAddress.getByName("localhost")
    socket = new Socket(ia, 9999)
    out = new ObjectOutputStream(
      new DataOutputStream(socket.getOutputStream()))
    in = new ObjectInputStream(new DataInputStream(socket.getInputStream()))
  }

  def end {
    socket.close()
    in.close()
    out.close()
  }

  def sendAddUserRequest(user: User) {
    val msg: RequestMessage = AddUserRequest(user)
    out.writeObject(msg)
    out.flush()
    val r = in.readObject().asInstanceOf[ReplyMessage]
    r match {
      case AddUserReply(str) => println("\nSucesso.\n")
      case _ => ;
    }
  }

  def sendLoginRequest(userName: String, password: String): Option[User] = {
    val msg: RequestMessage = LoginRequest(userName, password)
    out.writeObject(msg)
    out.flush()
    val r = in.readObject().asInstanceOf[ReplyMessage]
    r match {
      case LoginReply(u) => u
      case _ => null
    }
  }

  def sendAddIndebtedRequest(indebted: Indebted) {
    val msg: RequestMessage = AddIndebtedRequest(indebted)
    out.writeObject(msg)
    out.flush()
    val r = in.readObject().asInstanceOf[ReplyMessage]
    r match {
      case AddIndebtedReply(str) => println("\nSucesso.\n")
      case _ => ;
    }
  }

  def sendAddPropertyRequest(indebted: Indebted, property: Property) {
    val msg: RequestMessage = AddPropertyRequest(indebted, property)
    out.writeObject(msg)
    out.flush()
    val r = in.readObject().asInstanceOf[ReplyMessage]
    r match {
      case AddPropertyReply(str) => println("\nSucesso.\n")
      case _ => ; // ignore wrong typed message
    }
  }

  def sendAddAuctionRequest(property: Property, begin: Date, end: Date): Unit = {
    val msg: RequestMessage = AddAuctionRequest(property, begin, end)
    out.writeObject(msg)
    out.flush()
    val r = in.readObject().asInstanceOf[ReplyMessage]
    r match {
      case AddAuctionReply(str) => println("\nSucesso.\n")
      case _ => ; // ignore wrong typed message
    }
  }

  def sendQueryAuctionHistoryRequest(client: Client, propertyName:
  Option[String], propertyKind: Option[String]): List[(Auction, Bid)] = {
    val msg: RequestMessage = QueryAuctionHistoryRequest(client, propertyName, propertyKind)
    out.writeObject(msg)
    out.flush()
    val r = in.readObject().asInstanceOf[ReplyMessage]
    r match {
      case QueryAuctionHistoryReply(lab) => lab
      case _ => Nil
    }
  }

  def sendQueryOpenedAuctionsRequest(name: Option[String], propertyKind: Option[String]): List[Auction] = {
    val msg: RequestMessage = QueryOpenedAuctionsRequest(name, propertyKind)
    out.writeObject(msg)
    out.flush()
    val r = in.readObject().asInstanceOf[ReplyMessage]
    r match {
      case QueryOpenedAuctionsReply(la) => la
      case _ => Nil
    }
  }

  def sendQueryClosedAuctionsRequest(name: Option[String], propertyKind: Option[String]): List[Auction] = {
    val msg: RequestMessage = QueryClosedAuctionsRequest(name, propertyKind)
    out.writeObject(msg)
    out.flush()
    val r = in.readObject().asInstanceOf[ReplyMessage]
    r match {
      case QueryClosedAuctionsReply(la) => la
      case _ => Nil
    }
  }

  def sendQueryIndebtedPropertiesRequest(indebtedCpf: String): List[Property] = {
    val msg: RequestMessage = QueryIndebtedPropertiesRequest(indebtedCpf)
    out.writeObject(msg)
    out.flush()
    val r = in.readObject().asInstanceOf[ReplyMessage]
    r match {
      case QueryIndebtedPropertiesReply(lp) => lp
      case _ => Nil
    }
  }

  def sendQueryIndebtedsRequest(): List[Indebted] = {
    val msg: RequestMessage = QueryIndebtedsRequest()
    out.writeObject(msg)
    out.flush()
    var loi = List[Indebted]()
    var r = in.readObject().asInstanceOf[ReplyMessage]
    while (r != QueryIndebtedsReply(null)) {
      r match {
        case QueryIndebtedsReply(i) => loi = loi.+:(i)
        case _ => List() // ignore wrong typed message
      }
      r = in.readObject().asInstanceOf[ReplyMessage]
    }
    loi
  }

  def sendQueryPropertiesRequest(kind: Option[String], inAuction: Option[Boolean]): List[Property] = {
    val msg: RequestMessage = QueryPropertiesRequest(kind, inAuction)
    out.writeObject(msg)
    out.flush()
    val r = in.readObject().asInstanceOf[ReplyMessage]
    r match {
      case QueryPropertiesReply(lp) => lp
      case _ => Nil
    }
  }

  @throws(classOf[ConnectionException])
  def sendAddBidRequest(uid: String, aid: Long, value: Double) ={
    val msg: RequestMessage = AddBidRequest(uid, aid, value)
    out.writeObject(msg)
    out.flush()
    val r = in.readObject().asInstanceOf[ReplyMessage]
    r match {
      case AddBidReply("Success") =>
      case AddBidReply("Failed: bid was topped") => throw new ConnectionException("Lance inválido: valor insuficiente")
      case AddBidReply("Failed: auction has ended") => throw new ConnectionException("Leilão encerrado")
      case _ => throw new ConnectionException("Erro inesperado no servidor")
    }
  }

  def sendEndAuctionRequest(aid: Long): Boolean = {
    val msg: RequestMessage = EndAuctionRequest(aid)
    out.writeObject(msg)
    out.flush()
    val r = in.readObject().asInstanceOf[ReplyMessage]
    r match {
      case EndAuctionReply(msg: String) => msg == "Success"
      case _ => false
    }
  }

  def sendCancelBidRequest(uid: String, aid: Long, value: Double): Boolean = {
    val msg: RequestMessage = CancelBidRequest(uid, aid, value)
    out.writeObject(msg)
    out.flush()
    val r = in.readObject().asInstanceOf[ReplyMessage]
    r match {
      case CancelBidReply(msg: String) => msg == "Success"
      case _ => false
    }
  }
}
