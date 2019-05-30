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
public class MockMessagingService implements MessagingService {
    private static Logger logger = Logger.getLogger("auctionhouse");

    private Set<String> expectedEvents;
    private Set<String> actualEvents;

    public MockMessagingService() {
        expectedEvents = new HashSet<String>();
        actualEvents = new HashSet<String>();
    }

    private String makeOpenedEventString(
            String toAddress,
            int lotNumber) {
        return toAddress + ": " + "Auction of lot " 
                + Integer.toString(lotNumber) + " opened";
    }
    private String makeBidEventString(
            String toAddress,
            int lotNumber,
            Money amount) {
        return toAddress + ": " + amount + 
                " bid on lot " + Integer.toString(lotNumber);
    }
    private String makeSoldEventString(
            String toAddress,
            int lotNumber) {
        return toAddress + ": " + "lot " 
                + Integer.toString(lotNumber) + " has sold";
    }
    private String makeUnsoldEventString(
            String toAddress,
            int lotNumber) {
        return toAddress + ": lot " 
                + Integer.toString(lotNumber) + " has not sold";
    }
    /*
     * Implementations for MessagingService interface methods
     */

    public void auctionOpened(String address, int lotNumber) {
     
        String s = makeOpenedEventString(address, lotNumber);
        logger.finer(s); 
        actualEvents.add(s);
    }
    
    public void bidAccepted(String address, int lotNumber,Money amount) {
        
        String s = makeBidEventString(address, lotNumber, amount);
        logger.finer(s); 
        actualEvents.add(s);
    }
    
    public void lotSold(String address, int lotNumber) {
        
        String s = makeSoldEventString(address, lotNumber);
        logger.finer(s); 
        actualEvents.add(s);
    }
    public void lotUnsold(String address, int lotNumber) {
        String s = makeUnsoldEventString(address, lotNumber);
        logger.finer(s);        
        actualEvents.add(s);
    }
    /*
     * Test methods
     */
    public void expectAuctionOpened(String address, int lotNumber) {
        expectedEvents.add(makeOpenedEventString(address, lotNumber));
    }
    public void expectBidReceived(String address, int lotNumber,Money amount) {
        expectedEvents.add(makeBidEventString(address, lotNumber, amount));
    }
    public void expectLotSold(String address, int lotNumber) {
        expectedEvents.add(makeSoldEventString(address, lotNumber));
    }
    public void expectLotUnsold(String address, int lotNumber) {
        expectedEvents.add(makeUnsoldEventString(address, lotNumber));
    }
    
    public void verify() {

        assertEquals(expectedEvents, actualEvents);
        expectedEvents.clear();
        actualEvents.clear();
        return;
    }


}
