package business.entities;

import java.util.Date

class Client(
  username: String,
  password: String,
  name: String,
  private var CPF: String,
  private var birthDay: Date,
  private var telephone: String,
  private var address: String,
  private var email: String)
  extends User(username, password, name) {
  private var auctions: List[Auction] = List[Auction]()

}