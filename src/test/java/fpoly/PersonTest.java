package fpoly;

import static org.junit.Assert.fail;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PersonTest {

    // ================= CÁCH 1: ExpectedException Rule =================
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testExpectedExceptionRule() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid age");

        new Person("Fpoly", -1);
    }

    // ================= CÁCH 2: @Test(expected=...) =================
    @Test(expected = IllegalArgumentException.class)
    public void testExpectedExceptionAnnotation() {
        new Person("Fpoly", -1);
    }

    // ================= CÁCH 3: try-catch =================
    @Test
    public void testExpectedExceptionTryCatch() {
        try {
            new Person("Fpoly", -1);
            fail("Should have thrown IllegalArgumentException because age is invalid");
        } catch (IllegalArgumentException e) {
            // Test PASS nếu vào đây
        }
    }
}
