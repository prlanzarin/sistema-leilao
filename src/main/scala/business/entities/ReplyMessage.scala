package business.entities

abstract class ReplyMessage extends Serializable
case class LoginReply(user: Option[User]) extends ReplyMessage
case class AddUserReply(msg: String) extends ReplyMessage
case class AddIndebtedReply(msg: String) extends ReplyMessage
case class AddPropertyReply(msg: String) extends ReplyMessage
case class AddAuctionReply(msg: String) extends ReplyMessage
case class AddBidReply(msg: String) extends ReplyMessage
case class CancelBidReply(msg: String) extends ReplyMessage
case class EndAuctionReply(msg : String) extends ReplyMessage
case class QueryOpenedAuctionsReply(la: List[Auction]) extends ReplyMessage
case class QueryClosedAuctionsReply(la: List[Auction]) extends ReplyMessage
case class QueryIndebtedPropertiesReply(il : List[Property]) extends ReplyMessage
case class QueryIndebtedsReply(il : Indebted) extends ReplyMessage
case class QueryPropertiesReply(pl: List[Property]) extends ReplyMessage
case class QueryAuctionHistoryReply(lab: List[(Auction, Bid)]) extends ReplyMessage
