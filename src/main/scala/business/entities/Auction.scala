package business.entities

import java.util.Date
import java.util.Calendar

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

}
