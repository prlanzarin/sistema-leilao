package presentation.controller

import main.scala.presentation.controller.Connection
import presentation.ui.UIUtils
import business.entities.Indebted
import business.entities.Property
import business.entities.PropertyKind
import business.entities.PropertyKind._

abstract class Command {
    def execute
}

class CreateIndebtedCommand extends Command {
    override def execute {
        val indebted = readIndebted
        Connection.sendAddIndebtedRequest(indebted)
    }

    def readIndebted: Indebted = {
        println("Preencha com as informações do endividado:")
        val name = UIUtils.readString("Informe o nome:")
        val cpf = UIUtils.readCPF("Informe o CPF do endividado: (somente dígitos)")
        val bday = UIUtils.readDate(
            "Informe a data de nascimento: (" + UIUtils.dateFormat + ")")
        val debt = UIUtils.readDouble(
            "Informe o valor da dívida: (R$)", 0.0)
        return new Indebted(name, bday, debt, cpf)
    }

    override def toString: String = "Cadastrar endividado"
}

class CreatePropertyCommand(indebted: Indebted) extends Command {
    override def execute {
        val property = readProperty
        Connection.sendAddPropertyRequest(indebted, property)
    }

    def readProperty: Property = {
        println("Preencha com as informações do bem:")
        val name = UIUtils.readString("Informe o nome:")
        val value = UIUtils.readDouble("Informe o valor: (R$)", 0.0)
        val properties = "Imóvel" :: "Jóia" :: "Veículo" :: "Outro" :: List()
        val kind = UIUtils.select("Informe o tipo do bem:", properties)
        val boughtIn = 0 // TODO -> FILL
        new Property(name, value, PropertyKind.withName(kind), boughtIn)
    }
    
    override def toString: String = "Cadastrar Bem"
}

class SelectIndebtedCommand extends Command {
    override def execute {
        val loi: List[Indebted] = Connection.sendQueryIndebtedsRequest()
        if (loi == Nil)
            println("Não há endividados cadastrados.")
        else {
            val indebted = selectIndebted(loi)
            val subcommands = Map[String, Command](
                "C" -> new CreatePropertyCommand(indebted),
                "Q" -> new ExitCommand)
            println("\nDados do Endividado:")
            println(indebted)
            getCommand(subcommands)
        }
    }

    def selectIndebted(loi: List[Indebted]): Indebted = {
        println("Selecione um endividado:")
        println("Índice\tEndividado")
        for (i <- 0 until loi.size) {
            println(i + "\t" + loi(i).name)
        }
        val i = UIUtils.readInt("Informe o índice do endividado correspondente:")
        return loi(i)
    }

    def getCommand(commands: Map[String, Command]) {
        var command: Command = null
        while (command == null || !command.isInstanceOf[ExitCommand]) {
            command = chooseCommand(commands)
            command.execute
        }
    }

    def showCommands(commands: Map[String, Command]) {
        println("Opções:")
        commands.foreach(p => println(p._1 + " - " + p._2))
    }

    def chooseCommand(commands: Map[String, Command]): Command = {
        showCommands(commands)
        while (true) {
            val c = UIUtils.readString("Escolha uma opção:").toUpperCase()
            if (commands.contains(c))
                return commands.get(c).get
            else
                println("Comando desconhecido.")
        }
        return null
    }

    override def toString: String = "Selecionar endividado"
}

class ExitCommand extends Command {
    override def execute = Unit
    override def toString = "Sair"
}

class GenerateIndebtedsReportCommand extends Command {
    override def execute = {
        val loi: List[Indebted] = Connection.sendQueryIndebtedsRequest()
        if (loi == Nil)
            println("Não há endividados cadastrados.")
        else {
            val fn = UIUtils.readString("Nome do arquivo de relatório:")
            var data = loi.map(i => i.toString)
            val rg = new ReportGenerator
            rg.printToFile(fn, data)

        }
    }

    override def toString = "Gerar relatório de todos os endividados"
}