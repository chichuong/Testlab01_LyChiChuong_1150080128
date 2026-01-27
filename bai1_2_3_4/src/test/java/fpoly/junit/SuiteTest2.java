package fpoly.junit;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SuiteTest2 {

    @Test
    public void createAndSetName() {
        String expected = "y";
        String actual = "y";
        assertEquals(expected, actual);
        System.out.println("Suite Test 2 is successful " + actual);
    }

    @Test
    public void testHiMessage() {
        JUnitMessage junitMessage = new JUnitMessage("Fpoly");
        String expected = "Hi Fpoly";
        assertEquals(expected, junitMessage.printHiMessage());
        System.out.println("JUnit Hi Message is printing");
    }
}
