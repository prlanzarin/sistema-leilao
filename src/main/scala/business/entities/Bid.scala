package business.entities

class Bid(   val auction: Auction,
             val client: Client)
    extends Serializable {

    private var _value: Double = 0

    def value = _value

    def value_(nvalue : Double) = _value = nvalue

}
