package main.scala.business.entities

import java.util.Date
import java.util.Calendar

class Auction(
  private var _indebted: Indebted,
  private var _property: Property,
  private var _begin: Date,
  private var _end: Date) {
  
  private var _highestBid: Bid = null
  private var _opened: Boolean = Calendar.getInstance().getTime().compareTo(_begin) >= 0
  
  def indebted = this._indebted
  def property = this._property
  def begin = this._begin
  def end = this._end
  def highestBid = this._highestBid
  def opened = this._opened

}