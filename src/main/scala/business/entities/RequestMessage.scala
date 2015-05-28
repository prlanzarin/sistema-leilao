package business.entities

abstract class RequestMessage extends Serializable
case class LoginRequest(userName: String, password: String) extends RequestMessage
case class AddUserRequest(u: User) extends RequestMessage
case class AddIndebtedRequest(i: Indebted) extends RequestMessage
case class AddPropertyRequest(i: Indebted, p : Property) extends RequestMessage
case class QueryIndebtedPropertiesRequest(iCpf: String) extends RequestMessage
case class QueryIndebtedsRequest() extends RequestMessage
case class QueryPropertiesRequest(k: Option[String], i: Option[Boolean]) extends RequestMessage

