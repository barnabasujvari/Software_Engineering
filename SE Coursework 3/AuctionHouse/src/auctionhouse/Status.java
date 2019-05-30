/**
 * 
 */
package auctionhouse;

/**
 * @author pbj
 *
 */
public class Status {
    public static enum Kind {
        OK, 
        ERROR, 
        SALE, 
        SALE_PENDING_PAYMENT,
        NO_SALE
        }


    public Kind kind;
    public String message;

    public Status(Kind k) {
        kind = k;
        message = "";
    }
    public Status(Kind k, String m) {
        kind = k;
        message = m;
    }

    // Convenience methods
    public static Status OK() { return new Status(Kind.OK); }
    public static Status error(String message) {
        return new Status(Kind.ERROR, message);
    }
}
