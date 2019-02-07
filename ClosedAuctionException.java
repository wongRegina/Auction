/**
 * 	@author : Regina Wong
*/

/**
 * The exception that is thrown when the user tries to bid on a closed Auction
 */
public class ClosedAuctionException extends Exception {
	/**
	 * Makes a new ClosedAuctionException
	 * @param error
	 * 		the error message
	 */
	public ClosedAuctionException(String error)
	{
		super(error);
	}
}
