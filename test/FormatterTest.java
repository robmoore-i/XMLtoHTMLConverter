import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class FormatterTest {
//    @Test
//    public void canFormatOption_OptionDescriptionCorrectly() {
//        VelocityXMLFormatter formatter = new VelocityXMLFormatter();
//        String optionTag = "<option name=\"addr\" type=\"String\"><option-description><![CDATA[IP address access is permitted from]]></option-description></option>";
//        assertThat(formatter.formatOption_OptionDescription(optionTag), equalTo("<table class=\"Option_OptionDescription\">\r\n<tr><td>name</td><td>addr</td></tr>\r\n<tr><td>type</td><td>String</td></tr>\r\n<tr><td>deprecated</td><td>$deprecated</td></tr>\r\n<tr><td>default</td><td>$default</td></tr>\r\n<tr><td>java</td><td>$java</td></tr>\r\n</table>"));
//    }

    @Test
    public void canFormatPage_TopDescriptionCorrectly() {
        VelocityXMLFormatter formatter = new VelocityXMLFormatter();
        String pageTag =
                "<page name=\"Name-Mapping\"><top-description><![CDATA[A DataSource application can be configured to map its internal namespace to a different external namespace.]]>" +
                        "</top-description></page>";
        assertThat(formatter.formatPage_TopDescription(pageTag), equalTo("<!DOCTYPE>\r\n<html>\r\n<body>\r\n<h1>Name-Mapping</h1>\r\n<p>A DataSource application can be configured to map its internal namespace to a different external namespace.</p>\r\n</body>\r\n</html>"));
    }
}