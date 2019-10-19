/**
 * 
 */
package auctionhouse;

/**
 * @author pbj
 *
 */
public class CatalogueEntry {
    private static final String LS = System.lineSeparator();

    public int lotNumber;
    public String description;
    public LotStatus status;
    
    public CatalogueEntry(int lotNumber, String description, LotStatus status) {
        this.lotNumber = lotNumber;
        this.description = description;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CatalogueEntry)) return false;
        CatalogueEntry oCE = (CatalogueEntry) o;
        return lotNumber == oCE.lotNumber &&
                description.equals(oCE.description) &&
                status == oCE.status;
    }
    
    @Override
    public String toString() {
        return LS + Integer.toString(lotNumber) +
                ": " + description + 
                " (" + status + ")";
    }
}
