/**
 * 
 */
package auctionhouse;

import java.util.List;

/**
 * @author pbj
 *
 */
public interface AuctionHouse {

    Status registerBuyer(
            String name,
            String address,
            String bankAccount,
            String bankAuthCode);
    
    Status registerSeller(
            String name,
            String address,
            String bankAccount);
    
    Status addLot(
            String sellerName,
            int number,
            String description,
            Money reservePrice);
    
    List<CatalogueEntry> viewCatalogue();
    
    Status noteInterest(
            String buyerName,
            int lotNumber);
    
    Status openAuction(
            String auctioneerName,
            String auctioneerAddress,
            int lotNumber);
    
    Status makeBid(
            String buyerName,
            int lotNumber,
            Money bid);
    
    Status closeAuction(
            String auctioneerName,
            int lotNumber);
    
    
}
