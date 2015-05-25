package business.services

import business.entities.{Auction, User}
import database.Database

class UserServices {
  def getOpenAuctions : List[Auction] = {
      Database.getOpenAuctions
  }
  def getUser(login : String ,password : String) : Option[User] = {
      Database.queryUser(login, password)
  }
}
