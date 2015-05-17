package business.entities;
import business.entities._


abstract class RequestMessage extends Serializable
case class AddIndebtedRequest(indebted : Indebted) extends RequestMessage
case class AddPropertiesRequest(property : Property) extends RequestMessage

