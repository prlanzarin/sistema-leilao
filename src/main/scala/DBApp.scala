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
            "12345678901", date, "12345600", "Rua do Papagaio",
            "maurilio@lapaz.com")
        val chimerito = new Client("chimerito", "123", "Chimerito Santiago",
            "12345678903",
            date, "12345602", "Rua do Mamute", "chimerito@lapaz.com")
        val divermio = new Client("divermio", "123", "Divermio Tiamatu",
            "19293840918", date, "33516666", "Garganta del Diavolo",
            "divermio@lapaz.com")

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
        //Database.queryUser("divermio", "123").foreach(man => println(man))
        //Database.queryUser("neicilua", "123").foreach(man => println(man))
        Database.queryUser("chimerito", "123").foreach(man => println(man))
        Database.queryUser(divermio).foreach(man => println(man))
        Database.queryUser(maurilio).foreach(clt => println(clt)) //yes
        Database.queryUser(new Client("maulirio", "123", "Maurilio Santiago",
            "12345678901", date, "12345600", "Rua do Papagaio",
            "maurilio@lapaz.com")).foreach(clt => println(clt)) //no
        Database.queryUser(new Client("maurilio", "124", "Maurilio Santiago",
            "12345678901", date, "12345600", "Rua do Papagaio",
            "maurilio@lapaz.com")).foreach(clt => println(clt)) //no

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
        Database.queryIndebtedProperties("06666666666").foreach(p => println(p))

        println("--GET TEST--")
        Database.getProperties.foreach(prop => println(prop))

        println("-----AUCTIONS TEST-----")
        println("--ADD AUCTIONS---")
        /*Database.addAuction(Auction(Database.queryIndebted("06666666666").get,
            Database.queryProperty("06666666666", "Apartamento Duplex Power Plus").get,
            date, date, None, false))*/
        println("--CLOSED AUCTIONS---")
        Database.getClosedAuctions foreach(ac => println(ac))
        println("--OPEN AUCTIONS---")
        Database.getOpenAuctions foreach(ac => println(ac))
        println("--GET AUCTIONS---")
        Database.getAuctions foreach(ac => println(ac))
        println("--CLIENT AUCTIONS--")
        Database.queryClientAuctions(chimerito).foreach(ac => println(ac))

        println("-----BIDS TEST-----")
        Database.queryAuctionBids(1L).foreach(ac => println(ac))
        Database.queryClientBids(chimerito).foreach(ac => println(ac))

        println("-----QUERY PROPERTIES WITH KIND AND AUCTION-----")
        println("--VEHICLE TRUE--")
        Database.queryProperties(Some(PropertyKind.VEHICLE.toString), Some
            (true)).foreach(p => println(p))
        println("--VEHICLE FALSE--")
        Database.queryProperties(Some(PropertyKind.VEHICLE.toString), Some
            (false)).foreach(p => println(p))
        println("--VEHICLE ALL--")
        Database.queryProperties(Some(PropertyKind.VEHICLE.toString), None)
            .foreach(p => println(p))
        println("--ALL TRUE--")
        Database.queryProperties(None, Some(true)).foreach(p => println(p))
        println("--ALL FALSE--")
        Database.queryProperties(None, Some(false)).foreach(p => println(p))
        println("--ALL, ALL--")
        Database.queryProperties(None,None).foreach(p => println(p))

        println("-----QUERY INDEBTED BY PROPERTY-----")
        Database.queryIndebted(Database.queryProperty(6L).get).foreach(p =>
            println(p))
        Database.queryIndebted(Database.queryProperty(7L).get).foreach(p =>
            println(p))

        println("-----QUERY INDEBTEDS BY PROPERTIES-----")
        Database.queryPropertyOwners(Database.getProperties).foreach(p =>
            println(p))
        Database.queryPropertyOwners(Nil).foreach(p => println(p))

        println("-----UPDATE AUCTION----")
        Database.updateAuction(Auction(Database.queryIndebted("01111111111")
            .get, Database.queryProperty("01111111111", "Onibus da Carris").get,
            date, date, None, false, Some(1L)))
        Database.queryAuction(1L).foreach(a => println(a))
        Database.updateAuction(Auction(Database.queryIndebted("01111111111")
            .get, Database.queryProperty("01111111111", "Onibus da Carris").get,
            date, date, None, true, Some(1L)))
        Database.queryAuction(1L).foreach(a => println(a))

        println("-----MAKE BID----")
        Database.queryAuction(1L).foreach(a => println(a))
        Database.queryHighestBid(1L).foreach(p => println(p))
        //Database.makeBid(Bid(1L, chimerito, 55000.00))
        //Database.queryHighestBid(1L).foreach(p => println(p))

    }
}
