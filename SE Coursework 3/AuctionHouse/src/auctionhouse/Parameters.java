package auctionhouse;
/**
 * 
 * @author pbj
 *
 */
public class Parameters {
    public final double buyerPremium;
    public final double commission;
    public final Money increment;
    public final String houseBankAccount;
    public final String houseBankAuthCode;
    public final MessagingService messagingService;
    public final BankingService bankingService;
    
    public Parameters(
            double buyerPremium,
            double commission,
            Money increment,
            String houseBankAccount,
            String houseBankAuthCode,
            MessagingService messagingService,
            BankingService bankingService) {
        
        this.buyerPremium = buyerPremium;
        this.commission = commission;
        this.increment = increment;
        this.houseBankAccount = houseBankAccount;
        this.houseBankAuthCode = houseBankAuthCode;
        this.messagingService = messagingService;
        this.bankingService = bankingService;
        
    }
}
