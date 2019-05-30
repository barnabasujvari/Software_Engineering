/**
 * 
 */
package mocauctionhouse;

/**
 * @author pbj
 *
 */
public interface BankingService {
    
    Status transfer(
            String senderAccount,
            String senderAuthCode,
            String receiverAccount,
            Money amount);

}
