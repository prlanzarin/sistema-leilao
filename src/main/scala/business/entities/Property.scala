package business.entities

import PropertyKind._

class Property(val name: String,
               val value: Double,
               val kind: PropertyKind)
extends Serializable {

  private var _idKey: Int = 0 //TODO não sei se essa id está certa

  def idKey = this._idKey
}
