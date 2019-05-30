package mocauctionhouse;

public class Auctioneer {
    public String name;
    public String address;
    boolean in_auction = false;

    public Auctioneer(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public boolean inAuction() {
        return in_auction;
    }
}