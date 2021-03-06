package business.entities

class Manager(   userName: String,
                 password: String,
                 name: String)
    extends User(userName, password, name) {

    override def toString: String = "Manager username: " + userName +
        "\nPassword: " + password + "\nNome: " + name
}
