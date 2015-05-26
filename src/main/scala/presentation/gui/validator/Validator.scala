package main.scala.presentation.gui.validator

import java.text.{SimpleDateFormat, ParsePosition, FieldPosition, DateFormat}
import java.util.Date

/**
 * Created by mhbackes on 25/05/15.
 */
object Validator {
  val dateFormat = "dd/MM/yyyy"
  val dateFormatter = new SimpleDateFormat(dateFormat)
  val passwordSize = 3

  def validateName(input: String): String = {
    if(input.isEmpty)
      throw new ValidationException("Nome inválido")
    input
  }

  def validateDate(input: String): Date = {
    if(input.isEmpty)
      throw new ValidationException("Data inválida, formato: " + dateFormat)
    dateFormatter.parse(input)
  }

  def validateCpf(input: String): String = {
    if(input.size != 11)
      throw new ValidationException("CPF inválido")
    if (input.foldRight(true)((i: Char, z: Boolean) => (z && Character.isDigit(i))))
      input
    else
      throw new ValidationException("CPF inválido, somente dígitos")
  }

  def validatePhone(input: String): String = {
    if(input.size != 10)
      throw new ValidationException("Telefone inválido")
    if (input.foldRight(true)((i: Char, z: Boolean) => (z && Character.isDigit(i))))
      input
    else
      throw new ValidationException("Telefone inválido")
  }

  def validateAddress(input: String): String = {
    if(input.isEmpty)
      throw new ValidationException("Email inválido")
    input
  }

  def validateEmail(input: String): String = {
    if(input.isEmpty)
      throw new ValidationException("Email inválido")
    input
  }

  def validateUsername(input: String): String = {
    if(input.isEmpty)
      throw new ValidationException("Usuário inválido")
    input
  }

  def validatePassword(rawInput: Array[Char]) : String = {
    val input = new String(rawInput)
    if(input.size < passwordSize)
      throw new ValidationException("Senha inválida, a senha deve ter pelo menos " + passwordSize + " dígitos")
    input
  }
}
