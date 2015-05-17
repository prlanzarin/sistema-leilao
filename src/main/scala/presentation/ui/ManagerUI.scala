package presentation.ui;

import presentation.controller.Command
import java.util.HashMap
import presentation.controller.CreateIndebtedCommand
import presentation.controller.SelectIndebtedCommand
import presentation.controller.ExitCommand
import presentation.controller.ExitCommand

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

object ManagerTest {
  def main(args: Array[String]): Unit = {
    val ui = new ManagerUI
    ui.show
  }
}
