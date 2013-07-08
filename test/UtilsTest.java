import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class UtilsTest {
    @Test
    public void positionOfAAtBInCTest() {
        assertThat(StringUtils.positionOfNextAFromBInC(" ", 0, "ab c"), equalTo(2));
        assertThat(StringUtils.positionOfNextAFromBInC(" ", 2, "a b111 c"), equalTo(6));
        assertThat(StringUtils.removeNulls(new String[]{"a"}), equalTo(new String[]{"a"}));
    }
}
