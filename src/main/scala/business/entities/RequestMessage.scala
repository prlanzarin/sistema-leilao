package business.entities

import java.util.Date

abstract class RequestMessage extends Serializable
case class LoginRequest(userName: String, password: String) extends RequestMessage
case class AddUserRequest(u: User) extends RequestMessage
case class AddIndebtedRequest(i: Indebted) extends RequestMessage
case class AddPropertyRequest(i: Indebted, p : Property) extends RequestMessage
case class AddAuctionRequest(p: Property, b: Date, e: Date) extends RequestMessage
case class QueryOpenedAuctionsRequest(name: Option[String], propertyKind:
Option[String]) extends RequestMessage
case class QueryClosedAuctionsRequest(name: Option[String], propertyKind: Option[String]) extends RequestMessage
case class QueryIndebtedPropertiesRequest(iCpf: String) extends RequestMessage
case class QueryIndebtedsRequest() extends RequestMessage
case class QueryPropertiesRequest(k: Option[String], i: Option[Boolean]) extends RequestMessage
case class QueryAuctionHistoryRequest(client: Client, propertyName:
Option[String], propertyKind: Option[String]) extends RequestMessage

