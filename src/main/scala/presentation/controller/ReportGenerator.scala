package presentation.controller

import java.io._

class ReportGenerator {
  def printToFile(fileName : String, data : List[String]) = {
    val f = new File(fileName)
    val p = new java.io.PrintWriter(f)
    try {
      data.foreach(p.println)
    } finally {
      p.close()
    }
  }
/*
  def print(fileName : String, data : List[String]) = {
    printToFile(new File(fileName))(p => data.foreach(p.println))
  }
 */
}
