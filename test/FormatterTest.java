import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class FormatterTest {
    @Test
    public void canMakeTableHeader() {
        XMLDataFormatter formatter = new XMLDataFormatter();
        XMLParser parser = new XMLParser();
        XMLTag tag = parser.getXMLTagFromOpeningTag("<option name=\"http-port\" type=\"Integer\">");
        assertThat(tag.attributes[0] == null, equalTo(false));
        assertThat(tag.attributes[1] == null, equalTo(false));
        assertThat(formatter.makeHeaderRow(tag.attributes), equalTo("<tr><th>name</th><th>type</th></tr>"));
    }

    @Test
    public void canMakeTableCorrectly() {
        XMLDataFormatter formatter = new XMLDataFormatter();
        XMLParser parser = new XMLParser();
        XMLTag tag = parser.getXMLTagFromOpeningTag("<option name=\"http-port\" type=\"Integer\">");
        assertThat(formatter.makeTable(tag.attributes), equalTo("<table><tr><th>name</th><th>type</th></tr><tr><td>http-port</td><td>Integer</td></tr></table>"));
    }
}