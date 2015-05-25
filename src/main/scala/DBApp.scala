package main.scala

import _root_.business.entities._
import _root_.database.Database


/**
 * Created by prlanzarin on 21/05/2015.
 * DB Test App
 */
object DBApp {
    def main (args: Array[String]) {

        Database.populateDb()
        val date = new java.util.Date()
        val prlanza = new Manager("prlanzarin", "123", "Paulo")
        val neicilua = new Manager("neicilua", "123", "Neicilua")
        val maurilio = new Client("maurilio", "123", "Maurilio Santiago",
            "12345678901", date)
        val divermio = new Client("divermio", "123", "Divermio Tiamatu",
            "19293840918", date)

        println("-----MANAGERS TEST-----")
        println("--ADD TEST--")
        //Database.addUser(neicilua) //retire o comentario para adicionar a base
        // so adicione uma vez
        println("--QUERY TEST--")
        Database.queryUser(neicilua).foreach(man => println(man))
        Database.queryUser(prlanza).foreach(man => println(man)) //yes
        Database.queryUser(new Manager("prlanzarin", "124", "Paulo")).
            foreach(man => println(man)) //no
        Database.queryUser(new Manager("prlamzarin", "123", "Paulo")).
            foreach(man => println(man)) //no

        println("-----CLIENTS TEST-----")
        println("--ADD TEST--")
        //Database.addUser(divermio) //retire o comentario para adicionar a base
        // so adicione uma vez
        println("--QUERY TEST--")
        Database.queryUser("divermio", "123").foreach(man => println(man))
        Database.queryUser("neicilua", "123").foreach(man => println(man))
        Database.queryUser(divermio).foreach(man => println(man))
        Database.queryUser(maurilio).foreach(clt => println(clt)) //yes
        Database.queryUser(new Client("maulirio", "123", "Maurilio Santiago",
            "12345678901", date)).foreach(clt => println(clt)) //no
        Database.queryUser(new Client("maurilio", "124", "Maurilio Santiago",
            "12345678901", date)).foreach(clt => println(clt)) //no

        println("-----INDEBTEDS TEST-----")
        println("--QUERY TEST--")
        //yes
        Database.queryIndebted("01111111111").foreach(indebted => println(indebted))
        //no
        Database.queryIndebted("02193123892").foreach(indebted => println
            (indebted))
        println("--GET TEST--")
        Database.getIndebteds.foreach(indebted => println(indebted))

        println("-----PROPERTIES TEST-----")
        println("--QUERY TEST--")
        Database.queryProperty("01111111111", "Onibus da Carris").
            foreach(prop => println(prop))//yes
        Database.queryProperty("01111111111", "Omibus da Carris").
            foreach(prop => println(prop))//no
        Database.queryProperty("01111112434", "Onibus da Carris").
            foreach(prop => println(prop))//no
        println("--GET TEST--")
        Database.getProperties.foreach(prop => println(prop))

        println("-----AUCTIONS TEST-----")
        println("---OPEN AUCTIONS---")
        Database.getOpenAuctions.foreach(ac => println(ac))
        println("---CLOSED AUCTIONS---")
        Database.getClosedAuctions.foreach(ac => println(ac))
        println("---GET AUCTIONS---")
        Database.getAuctions.foreach(ac => println(ac))

    }
}
