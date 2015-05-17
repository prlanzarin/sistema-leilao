package main.scala.business.entities

object PropertyKind extends Enumeration{
  type PropertyKind = Value
  val REALTY = Value("Realty")
  val JEWEL = Value("Jewel")
  val VEHICLE = Value("Vehicle")
  val OTHER = Value("Other")
}