package business.entities;
import business.entities._


abstract class ReplyMessage extends Serializable;
case class AddIndebtedReply(msg: String) extends ReplyMessage
case class AddPropertiesReply(msg: String) extends ReplyMessage
