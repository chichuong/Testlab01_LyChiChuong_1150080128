package fploly;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AirthematicTest {
    
    public String message = "Fpoly exception";

    @Test(expected = ArithmeticException.class)
    public void testJUnitMessage() throws Exception {
        System.out.println("Fpoly Junit Message exception is printing ");
        JUnitMessage junitMessage = new JUnitMessage(message);
        junitMessage.printMessage();
    }

    @Test
    public void testJUnitHiMessage() {
        System.out.println("Fpoly Junit Message is printing ");
        JUnitMessage junitMessage = new JUnitMessage(message);
        assertEquals("Hi!" + message, junitMessage.printHiMessage());
    }
}
