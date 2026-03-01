package fploy;

import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PersonTest {
    
    // Test 1: Sử dụng @Rule với ExpectedException
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testExpectedException() {
        exception.expect(IllegalArgumentException.class);
        new Person("Fpoly", -1);
    }

    // Test 2: Sử dụng @Test với expected parameter
    @Test(expected = IllegalArgumentException.class)
    public void testExpectedException2() {
        new Person("fpoly", -1);
    }

    // Test 3: Sử dụng try-catch
    @Test
    public void testExpectedException3() {
        try {
            new Person("fpoly", -1);
            fail("Should have thrown an IllegalArgumentException because age is invalid!");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }
}
