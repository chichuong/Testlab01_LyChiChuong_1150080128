package fpoly;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ArithmeticTest {

    public String message = "Fpoly exception!";
    JUnitMessage junitMessage = new JUnitMessage(message);

    // Test bắt ngoại lệ
    @Test(expected = ArithmeticException.class)
    public void testPrintMessage() {
        System.out.println("Fpoly Junit Message exception is printing");
        junitMessage.printMessage();
    }

    // Test bình thường
    @Test
    public void testPrintHiMessage() {
        message = "Hi " + message;
        System.out.println("Fpoly Junit Message is printing");
        assertEquals(message, junitMessage.printHiMessage());
    }
}
