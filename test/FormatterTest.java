import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class FormatterTest {

    @Test
    public void canMakeTableCorrectly() {
        XMLDataFormatter formatter = new XMLDataFormatter();
        XMLParser parser = new XMLParser();
        XMLTag tag = parser.getXMLTagFromOpeningTag("<option name=\"http-port\" type=\"Integer\">");
        assertThat(formatter.makeTableForTagAttributes(tag.attributes), equalTo("<table><tr><th>name</th><th>type</th></tr><tr><td>http-port</td><td>Integer</td></tr></table>"));
    }

    @Test
    public void canSetupPageCorrectly() {
        XMLDataFormatter formatter = new XMLDataFormatter();
        assertThat(formatter.finishPage("<p>I'm a paragraph</p>"), equalTo("<!DOCTYPE html><html><body><p>I'm a paragraph</p></body></html>"));
    }
}