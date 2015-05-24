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

        Database.queryIndebted("01111111111").foreach(indebted => println
            (indebted))
        Database.queryIndebted("01111143111").foreach(indebted => println
            (indebted))

    }
}
