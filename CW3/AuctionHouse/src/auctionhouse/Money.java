
package auctionhouse;

/**
 * This is a class that uses double values to represent GBP
 * it implements the Comparable, and gives some basic methods for 
 * interacting with the values
 * @author pbj
 * 
 */
public class Money implements Comparable<Money> {
 /**
  * @param value the numerical value of the object in GBP
  */
    private double value;
    /**
     * Rounds value to the nearest Pence
     * @param pounds the original value that will be rounded
     * @return rounded value of pounds in pence 
     */
    private static long getNearestPence(double pounds) {
        return Math.round(pounds * 100.0);
    }
    /**
     * Normalise the value of the given amount meaning it rounds it without 
     * increasing the numerical value by *100
     * @param pounds 
     * @return rounded normalised value of pounds
     */
    private static double normalise(double pounds) {
        return getNearestPence(pounds)/100.0;
        
    }
    /**
     * Constructor for money, that takes a string and makes a normalised 
     * money value
     * @param pounds money in String form
     */
    public Money(String pounds) {
        value = normalise(Double.parseDouble(pounds));
    }
    /**
     * Private Constructor for money
     * @param pounds money in double value
     */
    private Money(double pounds) {
        value = pounds;
    }
    /**
     * Adding 2 Money Objects together
     * @param m Money object to be added to subject Money
     * @return value of addition on 2 values
     */
    public Money add(Money m) {
        return new Money(value + m.value);
    }
    
    /**
     * Subtracts 2 Money Objects 
     * @param m Money object to be subtracted from the subject 
     * @return value of subtraction of the 2 values
     */
    public Money subtract(Money m) {
        return new Money(value - m.value);
    }
    /**
     * modifies the value of the Money by the given percentage
     * @param percent desired change in percentage (usually between 0-100)
     * @return augmented money value
     */
    public Money addPercent(double percent) {
        return new Money(normalise(value * (1 + percent/100.0)));
    }
    /**
     * ToString method for easy printing
     */
    @Override
    public String toString() {
        return String.format("%.2f", value);
    }
    /**
     * utility method for comparison, works like  the usual compareTo method
     * @param m right side of comparison
     * @return returns negativ int or 0 or pos int for < = > respectively 
     */
    public int compareTo(Money m) {
        return Long.compare(getNearestPence(value),  getNearestPence(m.value)); 
    }
    /**
     * returns boolean for (x <= m)
     * @param m right side of comparison
     * @return boolean value of comparison
     */
    public Boolean lessEqual(Money m) {
        return compareTo(m) <= 0;
    }
    /**
     * returns boolean for (x = m)
     * @param o right side of comparison
     * @return boolean value of comparison     
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Money)) return false;
        Money oM = (Money) o;
        return compareTo(oM) == 0;       
    }
    /**
     * returns the hashCode value of the object
     */
    @Override
    public int hashCode() {
        return Long.hashCode(getNearestPence(value));
    }
      

}
