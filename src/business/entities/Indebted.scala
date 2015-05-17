package business.entities;

import java.util.Date

class Indebted(
  private var _name: String,
  private var _birthDay: Date,
  private var _debt: Double,
  private var _cpf: String) {
  
  private var _properties : List[Property] = List[Property]()
  
  def name = this._name
  def birthDay = this._birthDay
  def debt = this._debt
  def cpf = this._cpf
  def properties = this._properties
  
}
