package mocauctionhouse;

import java.util.ArrayList;

public class Lot {
    private String sellerName;
    private int number;
    private String description;
    private Money reservePrice;
    private LotStatus status = LotStatus.UNSOLD;
    private ArrayList<Buyer> interestedBuyers = new ArrayList<Buyer>();

    
    public LotStatus getStatus() {
        return status;
    }
    public void setStatus(LotStatus status) {
        this.status = status;
    }
    public Lot(String sellerName,
            int number,
            String description,
            Money reservePrice){
        this.sellerName = sellerName;
        this.number =number;
        this.description = description;
        this.reservePrice = reservePrice;
    }
    //adds buyer to set if not yet in it
    public void addInterestedBuyer(Buyer buyer) {
        if(!this.interestedBuyers.contains(buyer)) {
            interestedBuyers.add(buyer);
        }
    }
    public String[] getInterestedBuyersAddresses() {
        String[] addresses = new String[this.interestedBuyers.size()];
        for (int k = 0; k<interestedBuyers.size();k++) {
            addresses[k]=this.interestedBuyers.get(k).getAddress();
        }
        return addresses;
    }
    
}
