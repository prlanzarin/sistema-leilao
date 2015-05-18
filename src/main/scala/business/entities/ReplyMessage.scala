package business.entities;
import business.entities._


abstract class ReplyMessage extends Serializable;
case class AddIndebtedReply(msg: String) extends ReplyMessage
case class AddPropertyReply(msg: String) extends ReplyMessage
case class QueryIndebtedsReply(il : List[Indebted]) extends ReplyMessage
