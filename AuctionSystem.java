/**
 * 	@author : Regina Wong
 
*/

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Scanner;

/**
 * This class will allow the user to interact with the database by listing open auctions, 
 * 		make bids on open auctions, and create new auctions for different items. 
 * In addition, the class should provide the functionality to load a saved (
 * 		serialized) AuctionTable or create a new one if a saved table does not exist.
 *
 */
public class AuctionSystem {
	
	public static String username;

	/**
	 * The method should first prompt the user for a username. 
	 * This should be stored in username The rest of the program will be executed on behalf of this user.
	 */
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		String menu = "Menu: \n\t(D) - Import Data from URL \n\t(A) - Create a New Auction"
				+ "\n\t(B) - Bid on an Item \n\t(I) - Get Info on Auction \n\t(P) - "
				+ "Print All Auctions \n\t(R) - Remove Expired Auctions \n\t(T) - "
				+ "Let Time Pass \n\t(Q) - Quit";
		System.out.println("Starting...");
		AuctionTable table = new AuctionTable();
		ObjectOutputStream outStream = null;
		try
		{
			FileInputStream fileIn = new FileInputStream("auction.obj");
			ObjectInputStream inStream = new ObjectInputStream(fileIn);
			System.out.println("Loading previous Auction Table...");
			table = (AuctionTable) inStream.readObject();
		}
		catch(Exception e)
		{
			System.out.println("No previous auction table detected.");
			System.out.println("Creating new table...\n");
			table = new AuctionTable();
		}
		
		System.out.print("Please select a username: ");
		username = input.nextLine();
		System.out.println();
		System.out.println(menu);
		System.out.print("Please select an option: ");
		String choice = input.nextLine().toLowerCase();
		while(!choice.equalsIgnoreCase("q"))
		{
			boolean valid = true;
			switch(choice)
			{
				case "d":
					System.out.print("Please enter a URL: ");
					String url = input.nextLine();
					System.out.println("\nLoading...");
					try
					{
						table = AuctionTable.buildFromURL(url);
						System.out.println("Auction data loaded successfully!\n");
					}
					catch(IllegalArgumentException e)
					{
						System.out.println(e.getMessage() + "\n");
					}
					break;
				case "a":
					System.out.println("Creating new Auction as " + username);
					System.out.print("Please enter an Auction ID: ");
					String id = input.next();
					System.out.print("Please enter an Auction time (hours): ");
					int auctionTime = -1;
					boolean work = false;
					do
					{
						int temp = -1;
						if(input.hasNextInt())
						{
							temp = input.nextInt();
							if(temp <= 0)
							{
								System.out.print("Enter a positve int for the auction time: ");
								input.nextLine();
								work = false;
							}
							else
							{
								work = true;
							}
						}
						else
						{
							System.out.print("Enter a positve int for the auction time: ");
							input.nextLine();
							if(input.hasNextInt())
							{
								temp = input.nextInt();
								if(temp <= 0)
								{
									System.out.print("Enter a positve int for the auction time: ");
									input.nextLine();
									work = false;
								}
								else
								{
									work = true;
								}
							}
							
						}
						if(work == true)
						{
							auctionTime = temp;
						}
					}while(work != true);
					input.nextLine();
					System.out.print("Please enter some Item Info: ");
					String info = input.nextLine();
					Auction add = new Auction(id, auctionTime, info, username);
					table.putAuction(id, add);
					System.out.println("\nAuction " + id +" inserted into table.\n");
					break;
				case "b":
					System.out.print("Please enter an Auction ID: " );
					String auctionID = input.nextLine();
					Auction a = table.getAuction(auctionID);
					if(a.getSellerName().equals(username))
					{
						System.out.println("You cannot bid on you own item");
						break;
					}
					if(a != null && a.getTimeRemaining() != 0)
					{
						System.out.println("Auction " + a.getAuctionID() + " is OPEN");
						double current = a.getCurrentBid();
						if(a.getCurrentBid() == -1)
						{
							System.out.println("\tCurrent Bid: None");
						}
						else
						{
							System.out.println("\tCurrent Bid: $" + String.format("%.2f",a.getCurrentBid()) + "\n");
						}
						System.out.print("What would you like to bid?: ");
						double bid = -1;
						work = false;
						do
						{
							double temp = -1;
							if(input.hasNextDouble())
							{
								temp = input.nextDouble();
								if(temp <= 0)
								{
									System.out.print("Enter a positve double for the bid: ");
									input.nextLine();
									work = false;
								}
								else
								{
									work = true;
								}
							}
							else
							{
								System.out.print("Enter a positve double for the bid: ");
								input.nextDouble();
								if(input.hasNextDouble())
								{
									temp = input.nextDouble();
									if(temp <= 0)
									{
										System.out.print("Enter a positve double for the bid: ");
										input.nextLine();
										work = false;
									}
									else
									{
										work = true;
									}
								}
							}
							if(work == true)
							{
								bid = temp;
							}
						}while(work != true);
						try 
						{
							table.getAuction(auctionID).newBid(username, bid);
						} 
						catch (ClosedAuctionException e) 
						{
							
						}
						if(current == table.getAuction(auctionID).getCurrentBid())
						{
							System.out.println("Bid denied: The bid you enter isn't valid. It should be greater than current bid.");
						}
						else
						{
							System.out.println("Bid accepted.\n");
							input.nextLine();
						}
					}
					else if(a != null && a.getTimeRemaining() == 0)
					{
						System.out.println("Auction " + a.getAuctionID() + " is CLOSED");
						if(a.getCurrentBid() == -1)
						{
							System.out.println("\tCurrent Bid: None\n");
						}
						else
						{
							System.out.println("\tCurrent Bid: $" + String.format("%.2f",a.getCurrentBid()) + "\n");
						}
						System.out.println("You can no longer bid on this item./n");
					}
					else
					{
						System.out.println(auctionID + " isn't in the table");
					}
					break;
				case "i":
					System.out.print("Please enter an Auction ID: ");
					String iD = input.nextLine();
					Auction b = table.getAuction(iD);	
					if(b != null)
					{
						System.out.println("Auction " + iD + ":");
						System.out.println("\tSeller: " + b.getSellerName());
						System.out.println("\tBuyer: " + b.getBuyerName());
						System.out.println("\tTime: " + b.getTimeRemaining() + " hours");
						System.out.println("\tInfo: " + b.getItemInfo());
					}
					else
					{
						System.out.println(iD + " is not an ID in the table");
					}
					break;
				case "p":
					table.printTable();
					System.out.println();
					break;
				case "r":
					System.out.println("\nRemoving expired auctions...");
					table.removeExpiredAuctions();
					System.out.println("All expired auctions removed.\n");					
					break;
				case "t":
					System.out.print("How many hours should pass: ");
					int timePassed = -1;
					work = false;
					do
					{
						int temp = -1;
						if(input.hasNextInt())
						{
							temp = input.nextInt();
							work = true;
						}
						else
						{
							System.out.print("Enter a int for the time: ");
							input.nextLine();
							if(input.hasNextInt())
							{
								temp = input.nextInt();
								work = true;
							}
						}
						if(work == true)
						{
							timePassed = temp;
						}
					}while(work != true);
					System.out.println("/nTime passing...");
					table.letTimePass(timePassed);
					System.out.println("Auction times updated./n");
					input.nextLine();
					break;	
				default:
					System.out.print("Please enter a letter from the menu: \n");
					valid = false;
			}			
			System.out.println(menu);
			if(valid)
			{
				System.out.print("Please select an option: ");
			}
			choice = input.nextLine().toLowerCase();
		}
		FileOutputStream fileOut = null;
		try
		{
			fileOut = new FileOutputStream("auction.obj");
			outStream = new ObjectOutputStream(fileOut);
			System.out.println("\nWriting Auction Table to file...");
			outStream.writeObject(table);
			System.out.println("Done!");	
		}
		catch (Exception e)
		{
			
		}
		System.out.println("\nGoodbye...");
	}
}
