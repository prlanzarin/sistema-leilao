package business.entities

abstract class RequestMessage extends Serializable
case class AddIndebtedRequest(indebted : Indebted) extends RequestMessage
case class AddPropertyRequest(property : Property) extends RequestMessage

