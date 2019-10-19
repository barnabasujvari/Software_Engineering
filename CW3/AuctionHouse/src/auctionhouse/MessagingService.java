package auctionhouse;

public interface MessagingService {
    
    void auctionOpened(String address, int lotNumber);
    
    void bidAccepted(String address, int lotNumber, Money amount);
    
    void lotSold(String address, int lotNumber);
    
    void lotUnsold(String address, int lotNumber);

}
