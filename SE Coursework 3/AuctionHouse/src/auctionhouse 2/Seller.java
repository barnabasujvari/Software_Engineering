package auctionhouse;

public class Seller {
    private String name;
    private String address;
    private String bankAccount;
    
    public Seller(String name,
            String address,
            String bankAccount) {
        this.name=name;
        this.address=address;
        this.bankAccount=bankAccount;
        
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getBankAccount() {
        return bankAccount;
    }
}
