package presentation.ui;

import java.io.BufferedReader
import java.io.InputStreamReader

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Date
import java.util.Calendar
import java.text.SimpleDateFormat
import java.text.ParseException

class UI {
  val reader = new BufferedReader(new InputStreamReader(System.in))
  val dateFormatter = new SimpleDateFormat("dd/MM/yyyy")

  def readInt(message: String): Integer = {
    println(message)
    val input: Integer = reader.readLine.toInt
    return input
  }

  def readInt(message: String, lo: Integer, hi: Integer): Integer = {
    while (true) {
      println(message)
      val input: Integer = reader.readLine.toInt
      if (lo <= input && input <= hi)
        return input
      else
        println("Inteiro inválido.")
    }
    return null
  }

  def readDouble(message: String, lo: Double): Double = {
    while (true) {
      println(message)
      val input: Double = reader.readLine.toDouble
      if (lo <= input)
        return input
      else
        println("Inteiro inválido.")
    }
    return 0.0
  }

  def readString(message: String): String = {
    println(message)
    val input: String = reader.readLine
    return input;
  }

  def readString(message: String, size: Integer): String = {
    while (true) {
      println(message)
      val input: String = reader.readLine
      if (input.size == size)
        return input
      else
        println("Entrada inválida.")
    }
    return null
  }

  def readDate(message: String): Date = {
    println(message)
    var date: Date = null
    while (date == null) {
      try {
        val input: String = reader.readLine
        date = dateFormatter.parse(input)
      } catch {
        case e: Exception => println("Formato inválido.")
      }
    }
    return date
  }

  def readCPF(message: String): String = {
    while (true) {
      val input: String = readString(message, 11)
      if (input.foldRight(true)((i: Char, z: Boolean) => (z && Character.isDigit(i))))
        return input
    }
    return null
  }
}