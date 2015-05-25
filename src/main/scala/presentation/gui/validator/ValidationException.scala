package main.scala.presentation.gui.validator

/**
 * Created by mhbackes on 25/05/15.
 */
class ValidationException(val msg: String) extends Exception {
  override def getMessage: String = msg
}
