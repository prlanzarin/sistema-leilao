package business.entities

import java.util.Date

import presentation.ui.UIUtils

class Client(
                username : String,
                password : String,
                name : String,
                val CPF: String,
                val birthDay: Date,
                val telephone : String,
                val address : String,
                val email : String,
                val auctions: List[Auction] = List[Auction]())
    extends User(username, password, name) {

    override def toString: String = "Client username: " + username +
        "\nPassword: " + password + "\nNome: " + name +("\nData de " +
        "nascimento: " + UIUtils.dateFormatter.format(birthDay) +
        "\nTelefone: " + telephone + "\nEndereco: " + address + "\nE-Mail: "
        + email)

}

/*
 private var _auctions: List[Auction] = List[Auction]()

    def auctions : List[Auction] = _auctions

    def auctions_(nauctions : List[Auction]) = _auctions = nauctions
 */
