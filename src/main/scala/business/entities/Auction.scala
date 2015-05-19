package business.entities

import java.util.Date
import java.util.Calendar

class Auction(   val indebted: Indebted,
                 val property: Property,
                 val begin: Date,
                 val end: Date)
    extends Serializable {

    private var _highestBid: Bid = null
    private var _opened: Boolean = Calendar.getInstance().
        getTime().compareTo(begin) >= 0

    def highestBid = _highestBid

    def opened = _opened

    def opened_(b : Boolean) = _opened = b

    def highestbid_(bid : Bid) = _highestBid = bid

}
