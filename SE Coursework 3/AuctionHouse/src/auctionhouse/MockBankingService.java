/**
 * 
 */
package auctionhouse;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author pbj
 *
 */
public class MockBankingService implements BankingService {
    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();

    
    private Set<String> expectedEvents;
    private Set<String> actualEvents;
    
    private Set<String> badAccounts;
    
    public MockBankingService() {
        expectedEvents = new HashSet<String>();
        actualEvents = new HashSet<String>();
        
        badAccounts = new HashSet<String>();
    }
  
    
    
    private String makeTransferEventString(
                String senderAccount,
                String senderAuthCode,
                String receiverAccount,
                Money amount) {

        return "From: " + senderAccount 
                + " (" + senderAuthCode 
                + ") To: " + receiverAccount 
                + " Amount: " + amount;
     
    }
    /*
     * Implementation of BankingService interface
     */
    public Status transfer(
            String senderAccount,
            String senderAuthCode,
            String receiverAccount,
            Money amount) {
        
        String s = makeTransferEventString(
                senderAccount, 
                senderAuthCode, 
                receiverAccount, 
                amount);
        
        logger.finer(s);
        
        actualEvents.add(LS + s);
        
        if (badAccounts.contains(senderAccount)) {
            return Status.error("Transfer failed from bad account " + senderAccount);
        } else {
            return Status.OK();
        }
    }

    /*
     * Test interface
     */
    
    public void setBadAccount(String badAccount) {
        badAccounts.add(badAccount);
    }
    
    public void expectTransfer(
            String senderAccount,
            String senderAuthCode,
            String receiverAccount,
            Money amount) {
        
        expectedEvents.add(LS + 
                makeTransferEventString(
                        senderAccount, 
                        senderAuthCode, 
                        receiverAccount, 
                        amount));
        
    }
    
    public void verify() {
        
        assertEquals(expectedEvents, actualEvents);
        return;
    }


}
