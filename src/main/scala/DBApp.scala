package main.scala

import _root_.business.entities.{Indebted, Property}
import _root_.database.Database

/**
 * Created by USUARIO on 21/05/2015.
 */
object DBApp {
    def main (args: Array[String]) {
        Database.populateDb()
        val propq : List[Property] = Database.getProperties()
        for(p <- propq)
        {
            println(p.toString)
        }
    }
}
