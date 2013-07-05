import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class FormatterTest {
    @Test
    public void canFormatOption_OptionDescriptionCorrectly() {
        VelocityXMLFormatter formatter = new VelocityXMLFormatter();
        String input = "<option name=\"addr\" type=\"String\"><option-description><![CDATA[IP address access is permitted from]]></option-description></option>";
        assertThat(formatter.formatOption_OptionDescription(input), equalTo("<table class=\"Option_OptionDescription\">\r\n<tr><td>name</td><td>addr</td></tr>\r\n<tr><td>type</td><td>String</td></tr>\r\n<tr><td>deprecated</td><td>$deprecated</td></tr>\r\n<tr><td>default</td><td>$default</td></tr>\r\n<tr><td>java</td><td>$java</td></tr>\r\n</table>"));
    }
}
