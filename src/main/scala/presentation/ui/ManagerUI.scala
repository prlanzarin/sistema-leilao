package main.scala.presentation.ui;

import main.scala.presentation.controller.Command
import java.util.HashMap
import main.scala.presentation.controller.CreateIndebtedCommand
import main.scala.presentation.controller.SelectIndebtedCommand
import main.scala.presentation.controller.ExitCommand
import main.scala.presentation.controller.ExitCommand

object ManagerUI extends UI {
  val commands = Map[String, Command](
    "C" -> new CreateIndebtedCommand(),
    "S" -> new SelectIndebtedCommand(),
    "Q" -> new ExitCommand)

  def show {
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
    commands.foreach(p => println(p._1 + " - " + p._2))
  }

  def chooseCommand: Command = {
    showCommands
    while (true) {
      val c = UIUtils.readString("Selecione o comando:").toUpperCase()
      if (commands.contains(c))
        return commands.get(c).get
      else
        println("Comando desconhecido.")
    }
    return null
  }

  def main(args: Array[String]): Unit = {
    show
  }
}