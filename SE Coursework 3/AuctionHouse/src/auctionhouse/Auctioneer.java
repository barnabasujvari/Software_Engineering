package auctionhouse;

public class Auctioneer {
    public String name;
    public String address;
    boolean in_auction = false;

    public Auctioneer(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
    public void setStatus(boolean status) {
        this.in_auction = status;
    }

    public boolean inAuction() {
        return in_auction;
    }
}