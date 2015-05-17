package main.scala.business.entities

class Manager(
  userName: String,
  password: String,
  name: String)
  extends User(userName, password, name) {

}