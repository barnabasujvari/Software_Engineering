package mocauctionhouse;

public class Auction{
    private Lot lot;
    private Money current_bid = new Money("0");
    private Auctioneer auctioneer;
    private Buyer highestBidder;
    
     
    public Auction(Lot lot, Auctioneer auctioneer) {
        this.lot=lot;
        this.auctioneer = auctioneer;
        lot.setStatus(LotStatus.IN_AUCTION);
    }
    public void setLotStatus(LotStatus status){
        lot.setStatus(status);
    }
    
}