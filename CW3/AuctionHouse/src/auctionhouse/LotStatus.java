/**
 * 
 */
package auctionhouse;

/**
 * @author pbj
 *
 */
public enum LotStatus {
    UNSOLD,              // Either not auctioned or unsold after auction
    IN_AUCTION,          // In a currently running auction 
    SOLD,                // Sold in an auction
    SOLD_PENDING_PAYMENT // Sold, but attempt to collect money from buyer has failed
}
