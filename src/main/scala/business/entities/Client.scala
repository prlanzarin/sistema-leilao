package business.entities

import java.util.Date

import presentation.ui.UIUtils

class Client(
                username: String,
                password: String,
                name: String,
                val CPF: String,
                val birthDay: Date)
    extends User(username, password, name) {

    private var _telephone : String = "" //TODO: choose default value
    private var _address : String = ""
    private var _email : String = ""
    private var _auctions: List[Auction] = List[Auction]()

    def telephone : String = _telephone
    def adress : String = _address
    def email : String = _email
    def auctions : List[Auction] = _auctions

    def telephone_(ntelephone : String) = _telephone = ntelephone
    def address_(naddress : String) = _address = naddress
    def email_(nemail : String) = _email = nemail
    def auctions_(nauctions : List[Auction]) = _auctions = nauctions

    override def toString: String = "Client username: " + username +
        "\nPassword: " + password + "\nNome: " + name +("\nData de " +
        "nascimento: " + UIUtils.dateFormatter.format(birthDay))

}
