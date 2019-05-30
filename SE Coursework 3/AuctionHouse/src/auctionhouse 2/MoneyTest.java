/**
 * 
 */
package auctionhouse;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * @author pbj
 *
 */
public class MoneyTest {

    @Test    
    public void testAdd() {
        Money val1 = new Money("12.34");
        Money val2 = new Money("0.66");
        Money result = val1.add(val2);
        assertEquals("13.00", result.toString());
    }

    /*
     ***********************************************************************
     * BEGIN MODIFICATION AREA
     ***********************************************************************
     * Add all your JUnit tests for the Money class below.
     */

    @Test
    public void testSubtract() {
        Money val1 = new Money("10.00");
        Money val2 = new Money("4.00");
        Money result = val1.subtract(val2);
        assertEquals("6.00", result.toString());
    }
    
    @Test
    public void testAddPercent() {
        Money val = new Money("20.00");
        Money result = val.addPercent(10.00);
        assertEquals("22.00", result.toString());
        
    }
    
    @Test
    public void testToString() {
        Money val = new Money("24.99");
        String result = val.toString();
        assertEquals("24.99",result);
    }
    
    @Test
    public void testCompareTo() {
        Money val1 = new Money("1.00");
        Money val2 = new Money("2.00");
        Money val3 = new Money("1.50");
        Money val4 = new Money("2.00");
        // if x == y, returns 0
        int equal = val2.compareTo(val4);
        assertEquals(0, equal);
        // if x < y, returns a value less than 0
        int lesser = val1.compareTo(val2);
        assertEquals(lesser < 0, true);
        // if x > y, returns a value greater than 0
        int greater = val2.compareTo(val3);
        assertEquals(greater > 0, true);
    }
    
    @Test 
    public void testLessEqual() {
        // returns True if lesser
        Money val1 = new Money("10");
        Money val2 = new Money("11");
        boolean less = val1.lessEqual(val2);
        assertEquals(less, true);
        boolean greater = val2.lessEqual(val1);
        assertEquals(greater, false);
    }
    
    @Test 
    public void testEquals() {
        Money val1 = new Money("1.00");
        Money val2 = new Money("2.00");
        Money val3 = new Money("1.00");
        boolean equals = val1.equals(val3);
        boolean notequ = val3.equals(val2);
        assertEquals(equals, true);
        assertEquals(notequ,false);
        
    }
    
    // changes from pounds to pence
    @Test
    public void testHashCode() {
        Money val = new Money("10.00");
        int result = val.hashCode();
        assertEquals(1000, result);
}
    
    

    /*
     * Put all class modifications above.
     ***********************************************************************
     * END MODIFICATION AREA
     ***********************************************************************
     */


}
