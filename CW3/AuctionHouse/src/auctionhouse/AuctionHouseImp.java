/**
 * 
 */
package auctionhouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.Arrays;

/**
 * @author pbj
 *
 */
public class AuctionHouseImp implements AuctionHouse {
    // our data
    private Parameters parameters;
    private HashMap<String, Buyer> buyers = new HashMap<String, Buyer>();
    private HashMap<String, Seller> sellers = new HashMap<String, Seller>();
    private HashMap<Integer, Lot> lots = new HashMap<Integer, Lot>();
    private HashMap<Integer, Auction> auctions = new HashMap<Integer, Auction>();
    private HashMap<String, Auctioneer> auctioneers = new HashMap<String, Auctioneer>();
    private MessagingService msg;
    private BankingService bank;

    // end of our data
    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();

    private String startBanner(String messageName) {
        return LS
                + "-------------------------------------------------------------"
                + LS + "MESSAGE IN: " + messageName + LS
                + "-------------------------------------------------------------";
    }

    public AuctionHouseImp(Parameters parameters) {
        this.parameters = parameters;
        msg = parameters.messagingService;
        bank = parameters.bankingService;
    }

    public Status registerBuyer(String name, String address, String bankAccount,
            String bankAuthCode) {
        logger.fine(startBanner("registerBuyer " + name));
        if (buyers.containsKey(name)) {
            logger.warning("Buyer name already in use");
            return Status.error("This Buyer name is already in use");
        }
        Buyer buyer = new Buyer(name, address, bankAccount, bankAuthCode);
        buyers.put(name, buyer);
        return Status.OK();
    }

    public Status registerSeller(String name, String address,
            String bankAccount) {
        logger.fine(startBanner("registerSeller " + name));
        if (sellers.containsKey(name)) {
            logger.warning("Seller name already in use");
            return Status.error("Duplicate name not allowed");
        }
        Seller seller = new Seller(name, address, bankAccount);
        sellers.put(name, seller);
        return Status.OK();
    }

    public Status addLot(String sellerName, int number, String description,
            Money reservePrice) {
        logger.fine(startBanner("addLot " + sellerName + " " + number));

        if (!lots.containsKey(number)) {
            if (sellers.containsKey(sellerName)) {
                Lot lot = new Lot(sellerName, number, description,
                        reservePrice);
                lots.put(number, lot);
                return Status.OK();
            } else {
                logger.warning("Seller not registered");
                return Status.error("Seller not registered");
            }
        } else {
            logger.warning("Lot number already in use");
            return Status.error("Lot number already in use");
        }

    }

    public List<CatalogueEntry> viewCatalogue() {
        logger.fine(startBanner("viewCatalog"));
        List<CatalogueEntry> catalogue = new ArrayList<CatalogueEntry>();
        Integer[] lotNumstemporary = lots.keySet().toArray(new Integer[lots.size()]);
        int[] lotNums = new int[lotNumstemporary.length];
        for (int i = 0; i<lotNumstemporary.length; i++) {
            lotNums[i] = lotNumstemporary[i].intValue();
        }
        Arrays.sort(lotNums);
        for (int i = 0; i < lotNums.length; i++) {
            String lotDes = lots.get(lotNums[i]).getDescription();
            LotStatus lotStat = lots.get(lotNums[i]).getStatus();
            catalogue.add(new CatalogueEntry(lotNums[i], lotDes, lotStat));
            System.out.print(lotNums[i]); 
        }
        
        logger.fine("Catalogue: " + (catalogue.toString()));
        return catalogue;
    }

    public Status noteInterest(String buyerName, int lotNumber) {
        logger.fine(startBanner("noteInterest " + buyerName + " " + lotNumber));
        // check if buyer is registered
        if (buyers.containsKey(buyerName)) {
            // checks if lot exists in database
            if (lots.containsKey(lotNumber)) {
                // check if buyer is already registered inside the
                // addInterestedBuyer()
                lots.get(lotNumber).addInterestedBuyer(buyers.get(buyerName));
            }

        } else {
            logger.warning("Buyer not registered");
            return Status.error("Buyer not registered");
        }
        return Status.OK();
    }

    public Status openAuction(String auctioneerName, String auctioneerAddress,
            int lotNumber) {
        logger.fine(
                startBanner("openAuction " + auctioneerName + " " + lotNumber));
        // if lot dosent exist return error
        if (!lots.containsKey(lotNumber)) {
            logger.warning("Lot not in database");
            return Status.error("Lot not in database");
        }

        Lot currentLot = lots.get(lotNumber);
        if (currentLot.getStatus() == LotStatus.UNSOLD) {
            // sends a message to interested buyers and the seller
            String selleraddress = sellers.get(currentLot.getSellerName())
                    .getAddress();
            String[] buyeraddress = currentLot.getInterestedBuyersAddresses();
            for (String s : buyeraddress) {
                parameters.messagingService.auctionOpened(s, lotNumber);
            }
            parameters.messagingService.auctionOpened(selleraddress, lotNumber);
            // check if auctioneer is in database
            if (auctioneers.containsKey(auctioneerName)) {
                // checks if auctioneer is not in an other auction
                if (!auctioneers.get(auctioneerName).inAuction()) {
                    auctioneers.get(auctioneerName).setStatus(true);
                } else {
                    logger.warning("Auctioneer already in auction");
                    return Status
                            .error("Auctioneer already in an other auction");
                }

            } // creates new auctioneer if not in database
            else {
                auctioneers.put(auctioneerName,
                        new Auctioneer(auctioneerName, auctioneerAddress));
            }
            // open auction
            auctions.put(lotNumber, new Auction(lots.get(lotNumber),
                    auctioneers.get(auctioneerName)));
            return Status.OK();
        }

        if (currentLot.getStatus() == LotStatus.SOLD) {
            logger.warning(startBanner("Lot already sold"));
            return Status.error("Lot already sold, can't open auction");
        } else if (currentLot.getStatus() == LotStatus.IN_AUCTION) {
            logger.warning(startBanner("Lot already in auction"));
            return Status.error("Lot already in auction, can't open an again");
        } else {
            logger.warning(startBanner("Lot already sold and pending payment"));
            return Status
                    .error("This lot is already sold and is pending payment.");
        }
    }

    public Status makeBid(String buyerName, int lotNumber, Money bid) {
        logger.fine(startBanner(
                "makeBid " + buyerName + " " + lotNumber + " " + bid));
        Lot currentLot = lots.get(lotNumber);
        // buyer exists
        if (buyers.containsKey(buyerName)) {
            // lot exists
            if (lots.containsKey(lotNumber)) {
                // lot in auction
                if (lots.get(lotNumber).getStatus() == LotStatus.IN_AUCTION) {
                    // bid is OK to go through
                    if (currentLot.interestedBuyers.contains(buyers.get(buyerName))) {
                        
                    
                    Money difference = bid
                            .subtract(auctions.get(lotNumber).getCurrentBid());
                    // difference has to be >= increment
                    if (difference.compareTo(parameters.increment) >= 0) {
                        // placing bid
                        auctions.get(lotNumber).setCurrentBid(bid);
                        auctions.get(lotNumber)
                                .setHighestBidder(buyers.get(buyerName));

                        // sending messages to
                        // seller,auctioneer
                        String selleraddress = sellers
                                .get(currentLot.getSellerName()).getAddress();
                        msg.bidAccepted(selleraddress, lotNumber, bid);
                        String auctaddress = auctions.get(lotNumber)
                                .getAuctioneerAddress();
                        msg.bidAccepted(auctaddress, lotNumber, bid);
                        // buyers except the maker of the bid
                        String[] buyeraddress = currentLot
                                .getInterestedBuyersAddresses();
                        for (String s : buyeraddress) {
                            if (!buyers.get(buyerName).getAddress().equals(s)) {
                                msg.bidAccepted(s, lotNumber, bid);
                            }

                        }
                        return Status.OK();
                    } else {
                        logger.warning(startBanner("Too Small bid"));
                        return Status
                                .error("Too Small bid,this bid has to be >= ("
                                        + parameters.increment + "+"
                                        + auctions.get(lotNumber)
                                                .getCurrentBid());
                    }
                    }else{
                    logger.warning(startBanner("Buyer didnt note interest"));
                    return Status.error("Buyer didnt note interest"); 
                }
                } else {
                    logger.warning(startBanner("Lot not in auction"));
                    return Status.error("Lot not in auction,cant make bid");
                }
            } else {
                logger.warning(startBanner("Lot not in database"));
                return Status.error(
                        "Lot not in database, so its not in auction so cant make bid");
            }
        } else {
            logger.warning(startBanner("Buyer not in database"));
            return Status
                    .error("Buyer not in database, so he/she cant make a bid");
        }

    }

    private Status gatherPayment(int lotNumber, Money highest_bid,
            Buyer highestBidder) {
        // from buyer to AH(with buyers premium)
        String senderAccount = highestBidder.getBankAccount();
        String senderAuthCode = highestBidder.getBankAuthCode();
        String receiverAccount = parameters.houseBankAccount;
        Money amount = highest_bid.addPercent(parameters.buyerPremium);
        return bank.transfer(senderAccount, senderAuthCode, receiverAccount,
                amount);

    }

    private Status makePayment(int lotNumber, Money highest_bid,
            Buyer highestBidder) {
        // sellers.get(lots.get(lotNumber).getSellerName()).getAddress();
        // from AH to Seller (less sellers commission)
        String senderAccount = parameters.houseBankAccount;
        String senderAuthCode = parameters.houseBankAuthCode;
        String receiverAccount = sellers
                .get(lots.get(lotNumber).getSellerName()).getBankAccount();
        Money amount = highest_bid.addPercent(-parameters.commission);
        return bank.transfer(senderAccount, senderAuthCode, receiverAccount,
                amount);

    }

    public Status closeAuction(String auctioneerName, int lotNumber) {
        logger.fine(startBanner(
                "closeAuction " + auctioneerName + " " + lotNumber));
        Lot currentLot = lots.get(lotNumber);

        // auctioneer in database
        if (auctioneers.containsKey(auctioneerName)) {
            // lot exists
            if (lots.containsKey(lotNumber)) {
                // lot in auction
                if (lots.get(lotNumber).getStatus() == LotStatus.IN_AUCTION) {
                    Money highest_bid = auctions.get(lotNumber).getCurrentBid();
                    if (highest_bid != new Money("0")) {
                        Money resP = lots.get(lotNumber).getReservePrice();
                        // highest bid is bigger than reserve price
                        if (highest_bid.compareTo(resP) >= 0) {
                            // close auction
                            auctions.get(lotNumber).setLotStatus(
                                    LotStatus.SOLD_PENDING_PAYMENT);
                            auctioneers.get(auctioneerName).setStatus(false);// auctioneer's
                                                                             // job
                                                                             // ended

                            // payment
                            Buyer highestBidder = auctions.get(lotNumber)
                                    .getHighestBidder();
                            Status in = gatherPayment(lotNumber, highest_bid,
                                    highestBidder);
                            // Stage 1 ok
                            if (Status.Kind.OK == in.kind) {
                                Status out = makePayment(lotNumber, highest_bid,
                                        highestBidder);
                                // Stage 2 OK
                                if (Status.Kind.OK == out.kind) {
                                    auctions.get(lotNumber)
                                            .setLotStatus(LotStatus.SOLD);
                                    // messages
                                    // seller
                                    String selleraddress = sellers
                                            .get(currentLot.getSellerName())
                                            .getAddress();
                                    msg.lotSold(selleraddress, lotNumber);
                                    // buyers who noted interest
                                    String[] buyeraddress = currentLot
                                            .getInterestedBuyersAddresses();
                                    for (String s : buyeraddress) {
                                        msg.lotSold(s, lotNumber);
                                    }
                                    return new Status(Status.Kind.SALE,
                                            "Sale went through");
                                } // didnt go through Stage 2
                                else {
                                    logger.warning(startBanner(out.message));
                                    return new Status(
                                            Status.Kind.SALE_PENDING_PAYMENT,
                                            out.message);
                                }
                            } // didnt go through Stage 1
                            else {
                                logger.warning(startBanner(in.message));
                                return new Status(
                                        Status.Kind.SALE_PENDING_PAYMENT,
                                        in.message);
                            }
                        } else {
                            logger.fine(startBanner("Sale didnt go through"));
                            auctions.get(lotNumber)
                                    .setLotStatus(LotStatus.UNSOLD);

                            // messages
                            // seller
                            String selleraddress = sellers
                                    .get(currentLot.getSellerName())
                                    .getAddress();
                            msg.lotUnsold(selleraddress, lotNumber);
                            // buyers who noted interest
                            String[] buyeraddress = currentLot
                                    .getInterestedBuyersAddresses();
                            for (String s : buyeraddress) {
                                msg.lotUnsold(s, lotNumber);
                            }
                            return new Status(Status.Kind.NO_SALE,
                                    "Sale didnt go through");
                        }
                    } else {
                        logger.warning(startBanner("Lot Not in auction"));
                        return new Status(Status.Kind.NO_SALE,
                                "Sale didnt go through");
                    }
                } else {
                    logger.warning(startBanner("Lot Not in auction"));
                    return Status.error("Lot Not in auction");
                }
            } else {
                logger.warning(startBanner("Lot not in database"));
                return Status.error("Lot not in database");
            }
        } else {
            logger.warning(startBanner("Auctioneer not in database"));
            return Status.error("Auctioneer not in database");
        }
    }
}