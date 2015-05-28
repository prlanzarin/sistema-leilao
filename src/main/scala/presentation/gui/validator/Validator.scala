package main.scala.presentation.gui.validator

import java.text._
import java.util.{Calendar, Date}

/**
 * Created by mhbackes on 25/05/15.
 */
object Validator {
  val dateFormat = "dd/MM/yyyy"
  val timeFormat = "hh:mm:ss"
  val dateFormatter = new SimpleDateFormat(dateFormat)
  val timeFormatter = new SimpleDateFormat(timeFormat)
  val passwordSize = 3

  def validateName(input: String): String = {
    if (input.isEmpty)
      throw new ValidationException("Nome inválido")
    input
  }

  def validateDate(input: String): Date = {
    try {
      dateFormatter parse input
    } catch {
      case e: ParseException => throw new ValidationException("Data inválida, formato: " + dateFormat)
    }
  }

  def validateCpf(input: String): String = {
    if (input.size != 11)
      throw new ValidationException("CPF inválido")
    if (input.foldRight(true)((i: Char, z: Boolean) => (z && Character.isDigit(i))))
      input
    else
      throw new ValidationException("CPF inválido, somente dígitos")
  }

  def validatePhone(input: String): String = {
    if (input.size != 10)
      throw new ValidationException("Telefone inválido")
    if (input.foldRight(true)((i: Char, z: Boolean) => (z && Character.isDigit(i))))
      input
    else
      throw new ValidationException("Telefone inválido")
  }

  def validateAddress(input: String): String = {
    if (input.isEmpty)
      throw new ValidationException("Email inválido")
    input
  }

  def validateEmail(input: String): String = {
    if (input.isEmpty)
      throw new ValidationException("Email inválido")
    input
  }

  def validateUsername(input: String): String = {
    if (input.isEmpty)
      throw new ValidationException("Usuário inválido")
    input
  }

  def validatePassword(rawInput: Array[Char]): String = {
    val input = new String(rawInput)
    if (input.size < passwordSize)
      throw new ValidationException("Senha inválida, a senha deve ter pelo menos " + passwordSize + " dígitos")
    input
  }

  def validateValue(input: String): Double = {
    try {
      val value = input.toDouble
      if(value < 0)
        throw new ValidationException("Valor inválido")
      value
    } catch {
      case e: NumberFormatException => throw new ValidationException("Valor inválido")
    }
  }

  def validateYear(input: String): Int = {
    if(input.size > 4)
      throw new ValidationException("Ano inválido")
    try {
      val year = input.toInt
      if(year > Calendar.getInstance.get(Calendar.YEAR))
        throw new ValidationException("Ano inválido")
      year
    } catch {
      case e: NumberFormatException => throw new ValidationException("Ano inválido")
    }
  }

  def validateTime(input: String): Date = {
    try {
      timeFormatter parse input
    } catch {
      case e: ParseException => throw new ValidationException("Horário inválido, formato: " + timeFormat)
    }
  }

  def validateDateTimeAfterNow(dateInput: String, timeInput: String): Date = {
    val now = Calendar.getInstance.getTime()
    val date = validateDate(dateInput)
    val time = validateTime(timeInput)
    val calTime = Calendar.getInstance
    calTime.setTime(time)
    val h = calTime.get(Calendar.HOUR_OF_DAY)
    val m = calTime.get(Calendar.MINUTE)
    val s = calTime.get(Calendar.SECOND)
    val calDate = Calendar.getInstance
    calDate.setTime(date)
    calDate.add(Calendar.HOUR_OF_DAY, h)
    calDate.add(Calendar.MINUTE, m)
    calDate.add(Calendar.SECOND, s)
    val dateTime = calDate.getTime()
    if(dateTime.before(now)) throw new ValidationException("A data deve ser depois do dia de hoje")
    dateTime
  }
}
