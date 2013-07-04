import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class FormatterTest {
    @Test
    public void makesTableCorrectlyForSingleTag() {
        XMLDataFormatter formatter = new XMLDataFormatter();
        XMLParser parser = new XMLParser();
        XMLTag tag = parser.getXMLTagFromOpeningTag("<option name=\"http-port\" type=\"Integer\" java=\"true\">");
        assertThat(formatter.makeTableFromXMLTag(tag), equalTo(
                "<table border=\"1\">" +
                    "<tr>" +
                        "<th>name</th>" +
                        "<th>type</th>" +
                        "<th>java</th>" +
                    "</tr>" +
                    "<tr>" +
                        "<th>http-port</th>" +
                        "<th>Integer</th>" +
                        "<th>true</th>" +
                    "</tr>" +
                "</table>"
        ));
    }
}
