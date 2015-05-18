package presentation.ui;

import presentation.controller._
import java.io._
import java.net.SocketException

class ManagerUI extends UI {
    val commands = Map[String, Command](
        "C" -> new CreateIndebtedCommand(),
        "S" -> new SelectIndebtedCommand(),
        "Q" -> new ExitCommand)

    def show {
        println("Menu do Gerente\n")
        getCommand
    }

    def getCommand {
        var command: Command = null
        while (command == null || !command.isInstanceOf[ExitCommand]) {
            command = chooseCommand
            command.execute
        }
    }

    def showCommands {
        println("Opções:")
        commands.foreach(p => println(p._1 + " - " + p._2))
    }

    def chooseCommand: Command = {
        showCommands
        while (true) {
            val c = UIUtils.readString("Escolha uma opção:").toUpperCase()
            if (commands.contains(c))
                return commands.get(c).get
            else
                println("Comando desconhecido.")
        }
        return null
    }
}

object ManagerSession {
    def main(args: Array[String]): Unit = {
        try {
            Connection.init
            val ui = new ManagerUI
            ui.show            
            Connection.end
        } catch {
            case e: SocketException =>
                println("Não foi possível conectar ao servidor.")
            case e: IOException => ()
        } finally {
        }
    }
}
