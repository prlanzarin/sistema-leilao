package main.scala

import main.scala.presentation.gui.LoginFrame

import scala.swing.{Dialog, SimpleSwingApplication}
import java.net.SocketException

/**
 * Created by mhbackes on 25/05/15.
 */
object ClientGUI extends SimpleSwingApplication {
  override def top = {
    try {
      new LoginFrame
    } catch {
      case e: SocketException =>
        Dialog.showMessage(null, "Não foi possível conectar a o servidor", "Erro", Dialog.Message.Error)
        sys.exit(-1)
    }
  }
}
