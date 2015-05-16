package business.entities;

import PropertyKind._

class Property(
  private var _name: String,
  private var _value: Double,
  private var _kind: PropertyKind) {

  def name = this._name
  def value = this._value
  def kind = this._kind
}