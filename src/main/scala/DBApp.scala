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

        //Database.queryIndebted("01111111111").foreach(indebted => println
        //    (indebted))
        val loIndb = Database.getIndebteds
        loIndb foreach {
            i => println(i)
        }
        val loP = Database.queryProperty("01111111111", "Onibus da Carris")
        val loP2 = Database.queryProperty("02222222222", "Bicicleta Sundown")

        //Database.addIndebted("Paulel", new java.util.Date, 500.00,
        //    "12384784951")
        val sib = Database.getAuctions
        println(sib)

    }
}
