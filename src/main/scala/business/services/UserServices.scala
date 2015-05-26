package business.services

import business.entities.{Manager, Client, Auction, User}
import database.Database

class UserServices {
  def getOpenAuctions : List[Auction] = {
      Database.getOpenAuctions
  }
  def getUser(login : String ,password : String) : Option[User] = {
      Database.queryUser(login, password)
  }
  def addUser(user: User) = {
      user match {
      case client: Client => Database.addUser(client)
      case manager: Manager => Database.addUser(manager)
    }
  }
}
