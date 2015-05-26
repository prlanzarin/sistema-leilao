package business.entities

abstract class ReplyMessage extends Serializable
case class LoginReply(user: Option[User]) extends ReplyMessage
case class AddUserReply(msg: String) extends ReplyMessage
case class AddIndebtedReply(msg: String) extends ReplyMessage
case class AddPropertyReply(msg: String) extends ReplyMessage
case class QueryIndebtedPropertiesReply(il : List[Property]) extends ReplyMessage
case class QueryIndebtedsReply(il : Indebted) extends ReplyMessage
