package business.entities;

import java.util.Date

class Indebted(
  private var name: String,
  private var birthDay: Date,
  private var debt: Double) {
  private var properties : List[Property] = List[Property]()
}
