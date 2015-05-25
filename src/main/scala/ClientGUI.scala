package main.scala

import main.scala.presentation.gui.LoginFrame

import scala.swing.SimpleSwingApplication
import scala.swing.Dialog
import java.net.SocketException

/**
 * Created by mhbackes on 25/05/15.
 */
object ClientGUI extends SimpleSwingApplication {
  override def top = {
    try {
      new LoginFrame
    } catch {
      case e: SocketException => println("Não foi possível conectar ao servidor.")
        sys.exit(-1)
    }
  }
}
