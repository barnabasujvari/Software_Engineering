package auctionhouse;

import java.util.logging.Level;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author pbj
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ MoneyTest.class, AuctionHouseTest.class })
public class AllTests {

    public static void main(String[] args) {
        if (args.length >= 2){
            System.err.println("Unrecognised arguments");
            return;
        }  else if (args.length == 1) {
            String loggingLevel = args[0];
            if (loggingLevel.equals("off")) {
                AuctionHouseTest.loggingLevel = Level.OFF;
            } else if (loggingLevel.equals("info")) {
                AuctionHouseTest.loggingLevel = Level.INFO;
            } else if (loggingLevel.equals("fine")) {
                AuctionHouseTest.loggingLevel = Level.FINE;
            } else if (loggingLevel.equals("finer")) {
                AuctionHouseTest.loggingLevel = Level.FINER;
            } else {
                System.err.println("Unrecognised logging level argument: " + loggingLevel);
                return;
            }
        } 
        runJUnitTests();
    }

    public static void runJUnitTests() {

        Result result = JUnitCore.runClasses(AllTests.class);
        System.out.println("TEST RESULTS");
        System.out.println("Number of tests run: " + result.getRunCount());
        if (result.wasSuccessful()) {
            System.out.println("ALL TESTS PASSED");
        } else {
            System.out.println("SOME TESTS FAILED");
            System.out.println("Number of failed tests: " + result.getFailureCount());
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }  
        } 
    }


}   


