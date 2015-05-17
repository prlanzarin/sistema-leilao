package business.entities

import PropertyKind._

class Property(
  private var _name: String,
  private var _value: Double,
  private var _kind: PropertyKind) {
  
  private var _idKey: Int = 0 //TODO não sei se essa id está certa

  def idKey = this._idKey
  def name = this._name
  def value = this._value
  def kind = this._kind
}
