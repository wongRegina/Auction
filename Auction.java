import java.io.Serializable;

/**
 * 	@author : Regina Wong 
*/

/**
 * Write a fully-documented class named Auction which represents an active auction 
 * 		currently in the database. The Auction class should contain member variables 
 * 		for the seller's name, the current bid, the time remaining (in hours), 
 * 		current bidder's name, information about the item, and the unique ID for the auction. 
 * 	In addition, the class should implement a toString() method, 
 * 		which should print all of the data members in a neat tabular form.
 * The Auction class can only be altered by a single member method called newBid(), 
 * 		which takes in the name of a bidder and their bid value. 
 * This method checks to see if the bid value is greater than the current bid, 
 * 		and if it is, replaces the current bid and buyer name. 
 * There should be getter methods for each member variable, however, no setters should be included.
 *
 */
public class Auction implements Serializable{
	private int timeRemaining;
	private double currentBid;
	private String auctionID, sellerName, buyerName, itemInfo;
	
	/**
	 * Makes a Auction object that has no current bid
	 * @param id
	 * 		ID of the auction
	 * @param time
	 * 		time of the auction
	 * @param info
	 * 		information about the auction
	 * @param seller
	 * 		the seller of the object being auction
	 */
	public Auction(String id, int time, String info, String seller)
	{
		auctionID = id;
		timeRemaining = time;
		itemInfo = info;	
		currentBid = -1;
		sellerName = seller;
	}
	
	/**
	 * Makes a Auction object that has a current bid
	 * @param id
	 * 		ID of the auction
	 * @param time
	 * 		time of the auction
	 * @param info
	 * 		information about the auction
	 * @param seller
	 * 		the seller of the object being auction
	 * @param buyer
	 * 		the bidder
	 * @param bid
	 * 		the amount the bidder bid
	 */
	public Auction(String id, int time, String info, String seller, String buyer, double bid)
	{
		auctionID = id;
		timeRemaining = time;
		itemInfo = info;
		sellerName = seller;
		buyerName = buyer;
		currentBid = bid;
	}
	
	/**
	 * Decreases the time remaining for this auction by the specified amount. 
	 * If time is greater than the current remaining time for the auction, 
	 * 		then the time remaining is set to 0 (i.e. no negative times).
	 * @param time
	 * 		the amount of hours that should be decreased from the auction
	 * <dt><b>Postconditions:</b><dd>
	 * 		TimeRemaining has been decremented by the indicated amount and is greater than or equal to 0.
	 */
	public void decrementTimeRemaining(int time)
	{
		timeRemaining -= time;
		if(timeRemaining < 0)
		{
			timeRemaining = 0;
		}
	}
	
	/**
	 * Makes a new bid on this auction. 
	 * If bidAmt is larger than currentBid, then the value of currentBid is replaced by 
	 * 		bidAmt and buyerName is is replaced by bidderName.
	 * @param bidderName
	 * 		the bidder's name
	 * @param bidAmt
	 * 		the amount the bidder bid
	 * @throws ClosedAuctionException
	 * 		 Thrown if the auction is closed and no more bids can be placed (i.e. timeRemaining == 0). 
	 */
	public void newBid(String bidderName, double bidAmt) throws ClosedAuctionException
	{
		if(timeRemaining == 0)
		{
			throw new ClosedAuctionException("You can no longer bid on this item.");
		}
		else
		{
			if(bidAmt > currentBid)
			{
				currentBid = bidAmt;
				buyerName = bidderName;
			}
		}
	}
	
	/**
	 * returns string of data members in tabular form.
	 */
	public String toString() 
	{
		String temp = "";
		String x = "";
		if(currentBid != -1)
		{
			x = "$ " + String.format("%.2f",  currentBid);
		}
		String buyer = "";
		if(buyerName != null)
		{
			buyer = buyerName;
		}
		temp = String.format(" %3s | %10s | %25s | %25s| %-10s ", auctionID,x, sellerName, buyer,timeRemaining + " hours");
		String y = itemInfo;
		if(y.length() > 40)
		{
			y = itemInfo.substring(0,40);
		}
		temp += "| " + y;
		return temp;
	}
	
	/**
	 * gets the information of the auction
	 * @return
	 * 		the information of the auction
	 */
	public String getItemInfo()
	{
		return itemInfo;
	}
	
	/**
	 * gets the buyer's name
	 * @return
	 * 		the name of buyer of the auction
	 */
	public String getBuyerName()
	{
		return buyerName;
	}
	
	/**
	 * gets the seller's name
	 * @return
	 * 		the name of the seller of the auction
	 */
	public String getSellerName()
	{
		return sellerName;
	}
	
	/**
	 * get the auction ID of the auction
	 * @return
	 * 		the auction ID of the auction
	 */
	public String getAuctionID()
	{
		return auctionID;
	}
	
	/**
	 * the time remaining of the auction
	 * @return
	 * 		the time remaining of the auction
	 */
	public int getTimeRemaining()
	{
		return timeRemaining;
	}
	
	/**
	 * get the current bid 
	 * @return
	 * 		the current bid
	 */
	public double getCurrentBid()
	{
		return currentBid;
	}
}

