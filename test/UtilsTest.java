import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class UtilsTest {
    @Test
    public void positionOfAAtBInCTest() {
        StringUtils stringUtils = new StringUtils();
        assertThat(stringUtils.positionOfNextAFromBInC(" ", 0, "ab c"), equalTo(2));
        assertThat(stringUtils.positionOfNextAFromBInC(" ", 2, "a b111 c"), equalTo(6));
    }
}
