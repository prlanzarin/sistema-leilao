package business.entities;

import java.util.Date
import java.util.Calendar

class Auction(
  private var indebted: Indebted,
  private var property: Property,
  private var begin: Date,
  private var end: Date) {
  
  private var highestBid: Bid = null
  private var opened: Boolean = Calendar.getInstance().getTime().compareTo(begin) >= 0

}