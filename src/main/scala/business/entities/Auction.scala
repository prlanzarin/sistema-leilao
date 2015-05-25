package business.entities

import java.util.Date
import java.util.Calendar

import presentation.ui.UIUtils

class Auction(   val indebted: Indebted,
                 val property: Property,
                 val begin: Date,
                 val end: Date)
  extends Serializable {

  private var _highestBid: Bid = null
  private var _open: Boolean = Calendar.getInstance().
    getTime().compareTo(begin) >= 0

  def highestBid = _highestBid

  def open = _open

  def open_(b : Boolean) = _open = b

  def highestbid_(bid : Bid) = _highestBid = bid

  override def toString: String = "Endividado: " + indebted.name +
    "\nPropriedade: " + "" + property.name + "\nComeco (R$): " +
    UIUtils.dateFormatter.format(begin) +("\nFim: " + UIUtils.dateFormatter.format(end))

}
