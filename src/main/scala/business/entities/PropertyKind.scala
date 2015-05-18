package business.entities

object PropertyKind extends Enumeration{
  type PropertyKind = Value
  val REALTY = Value("Imóvel")
  val JEWEL = Value("Jóia")
  val VEHICLE = Value("Veículo")
  val OTHER = Value("Outro")
}
