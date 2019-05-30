package auctionhouse;

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
    public Money getCurrentBid() {
        return current_bid;
    }
    public void setCurrentBid(Money current_bid) {
        this.current_bid = current_bid;
    }
    public String getAuctioneerName() {
        return this.auctioneer.getName();
    }
    public String getAuctioneerAddress() {
        return this.auctioneer.getAddress();
    }
    public Buyer getHighestBidder() {
        return highestBidder;
    }
    public void setHighestBidder(Buyer highestBidder) {
        this.highestBidder = highestBidder;
    }
   
    
}