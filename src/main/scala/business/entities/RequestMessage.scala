package business.entities

abstract class RequestMessage extends Serializable
case class AddUserRequest(u: User) extends RequestMessage
case class AddIndebtedRequest(i: Indebted) extends RequestMessage
case class AddPropertyRequest(i: Indebted, p : Property) extends RequestMessage
case class QueryIndebtedsRequest() extends RequestMessage

