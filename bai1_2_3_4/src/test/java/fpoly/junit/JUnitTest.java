package fpoly.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SuiteTest1.class,
        SuiteTest2.class
})
public class JUnitTest {
    // class này để trống, chỉ dùng để gom test suite
}
