/**
 * 	@author : Regina Wong

*/

import java.io.Serializable;

import big.data.*;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.HashMap;

/**
 * The database of open auctions will be stored in a hash table to provide constant time insertion and deletion.
 *, your class should include a function which will build an AuctionTable from a URL using the BigData library. 
 */
public class AuctionTable implements Serializable {
	Hashtable <String, Auction>auctions = new Hashtable<String, Auction>();
	
	/**
	 * Uses the BigData library to construct an AuctionTable from a remote data source.
	 * @param URL
	 * 		String representing the URL to the remote data source.
	 * @return
	 * 		The AuctionTable constructed from the remote data source.
	 * @throws IllegalArgumentException
	 * 		Thrown if the URL does not represent a valid datasource (can't connect or invalid syntax).
	 */
	public static AuctionTable buildFromURL(String URL) throws IllegalArgumentException
	{
		Hashtable <String, Auction>x = new Hashtable<String, Auction>();
		try
		{
			DataSource ds = DataSource.connect(URL).load();
			String idData [] = ds.fetchStringArray("listing/auction_info/id_num");
			String bidData [] = ds.fetchStringArray("listing/auction_info/current_bid");
			String sellerData[] = ds.fetchStringArray("listing/seller_info/seller_name");
			String buyerData [] = ds.fetchStringArray("listing/auction_info/high_bidder/bidder_name");
			String timeData [] = ds.fetchStringArray("listing/auction_info/time_left");
			String cpu[] = ds.fetchStringArray("listing/item_info/cpu");
			String memory[] = ds.fetchStringArray("listing/item_info/memory");
			String hardDrive[] = ds.fetchStringArray("listing/item_info/hard_drive");
			String infoData[] = new String[memory.length];
			for(int i = 0 ; i < memory.length ; i ++)
			{
				infoData[i] = cpu[i] + memory[i] + hardDrive[i];
			}
			int times [] = new int [timeData.length];
			for(int i = 0 ; i < timeData.length ; i ++)
			{
				int hours = 0;
				int indexOfDay = -5;
				if(timeData[i].contains("day"))
				{
					indexOfDay = timeData[i].indexOf("day");
					String temp = timeData[i].substring(0,indexOfDay).trim();
					hours += Integer.parseInt(temp) * 24;
				}
				if(timeData[i].contains("hour"))
				{
					String temp = timeData[i].substring(indexOfDay + 5 , timeData[i].indexOf("hour")).trim();
					hours += Integer.parseInt(temp);
				}
				if(timeData[i].contains("hrs"))
				{
					System.out.print((indexOfDay + 5) + "asfsd" + timeData[i].indexOf("hrs"));
					String temp = timeData[i].substring(indexOfDay + 5 , timeData[i].indexOf("hrs")).trim();
					hours += Integer.parseInt(temp);
				}
				times [i] =hours;
			}
			double[] currentBid = new double [bidData.length];
			for(int i = 0; i < bidData.length ; i ++)
			{
				String temp = bidData[i].substring(1,bidData[i].indexOf("."));
				while(temp.contains(","))
				{
					int psn = temp.indexOf(",");
					temp = temp.substring(0, psn) + temp.substring(psn+1);
				}
				double decimal = Integer.parseInt(bidData[i].substring(bidData[i].indexOf(".")+1));
				currentBid[i] = Integer.parseInt(temp) + (decimal/100);
			}
			for(int i = 0; i < idData.length ; i ++)
			{
				x.put(idData[i] , new Auction(idData[i],times[i],infoData[i],sellerData[i], buyerData[i], currentBid[i]));
			}
			AuctionTable ret = new AuctionTable();
			ret.auctions = x;
			return ret;
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("The URL is not valid.");
		}
	}
	
	/**
	 * Manually posts an auction, and add it into the table.
	 * @param auctionID
	 * 		the unique key for this object
	 * @param auction
	 * 		The auction to insert into the table with the corresponding auctionID
	 * @throws IllegalArgumentException
	 * 		If the given auctionID is already stored in the table.
	 */
	public void putAuction(String auctionID, Auction auction) throws IllegalArgumentException
	{
		if(auctions.containsKey(auctionID))
		{
			throw new IllegalArgumentException("The auction ID has been taken");
		}
		auctions.put(auctionID, auction);
	}
	
	/**
	 * Get the information of an Auction that contains the given ID as key
	 * @param auctionID
	 * 		the unique key for this object
	 * @return
	 * 		An Auction object with the given key, null otherwise.
	 */
	public Auction getAuction(String auctionID)
	{
		if(auctions.containsKey(auctionID))
		{
			return auctions.get(auctionID);
		}
		return null;
	}
	
	/**
	 * Simulates the passing of time. 
	 * Decrease the timeRemaining of all Auction objects by the amount specified. 
	 * The value cannot go below 0.
	 * @param numHours
	 * 		the number of hours to decrease the timeRemaining value by.
	 * @throws IllegalArgumentException
	 * 		If the given numHours is non positive
	 */
	public void letTimePass(int numHours) throws IllegalArgumentException
	{
		if(numHours < 0)
		{
			throw new IllegalArgumentException("The number of hours you enterd isn't valid");
		}
		Collection<Auction> temp = auctions.values();
		for(Auction x : temp)
		{
			x.decrementTimeRemaining(numHours);
		}	
	}
	
	/**
	 * Iterates over all Auction objects in the table and removes them if they are expired (timeRemaining == 0).
	 */
	public void removeExpiredAuctions()
	{
		for(Enumeration<Auction> temp = auctions.elements() ; temp.hasMoreElements();)
		{
			Auction x = temp.nextElement();
			if(x.getTimeRemaining() == 0)
			{
				auctions.remove(x.getAuctionID());
			}
		}	
	}
	
	/**
	 * Prints the AuctionTable in tabular form.
	 */
	public void printTable()
	{
		String temp = "";
		temp = String.format(" %3s| %10s | %25s | %25s| %-10s |%-6s", "Auction ID", "Bid", "Seller" ,"Buyer","Time", " Item Info");
		temp += "\n======================================================================================================================================";
		Collection<Auction> tempValues = auctions.values();
		for(Auction x : tempValues)
		{
			temp += "\n" + x.toString();
		}
		System.out.println(temp);
	}
}
