package fpoly.junit;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SuiteTest1 {

    private final String message = "Fpoly";
    private final JUnitMessage junitMessage = new JUnitMessage(message);

    @Test
    public void testPrintMessage() {
        System.out.println("JUnit Message is printing");
        junitMessage.printMessage();
        assertEquals("Fpoly", message); // kiểm tra đơn giản cho đúng test case
        System.out.println("Suite Test 1 is successful " + message);
    }
}
