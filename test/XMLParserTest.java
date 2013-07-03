import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class XMLParserTest {
    @Test
    public void canParseOpeningTagData() {
        XMLParser parser = new XMLParser();
        XMLTag xmlTag = parser.getDataFromOpeningTag("<option name=\"data-src-port\" type=\"Integer\" default=\"0\" java=\"true\">");
        XMLData[] data = xmlTag.data;
        assertThat(data[0].name, equalTo("name"));
        assertThat(data[0].value, equalTo("data-src-port"));
        assertThat(xmlTag.tagClass, equalTo("option"));
    }

    @Test
    public void canGetTagClassFromOpeningTag() {
        XMLParser parser = new XMLParser();
        XMLTag xmlTag1 = parser.getDataFromOpeningTag("<option>");
        assertThat(xmlTag1.tagClass, equalTo("option"));
        XMLTag xmlTag2 = parser.getDataFromOpeningTag("<tag>");
        assertThat(xmlTag2.tagClass, equalTo("tag"));
    }

    @Test
    public void canCheckThatAngleBracketsBalanced() {
        XMLParser parser = new XMLParser();
        assertThat(parser.checkThatAngleBracketsBalanced("<option><tag></tag></option>"), equalTo(true));
        assertThat(parser.checkThatAngleBracketsBalanced("<option></option>"), equalTo(true));
        assertThat(parser.checkThatAngleBracketsBalanced("<option><option>"), equalTo(false));
        assertThat(parser.checkThatAngleBracketsBalanced("</option></option>"), equalTo(false));
        assertThat(parser.checkThatAngleBracketsBalanced("<option name=\"blah\" href=\"blah\"></option>"), equalTo(true));
    }
}

/*
<option name="data-src-port" type="Integer" default="0" java="true">
    <option-description>
        <!
            [CDATA
                [
                    Network port to listen for connections from DataSource peers. The default of 0
                    means that no connections can be made to this DataSource.
                ]
            ]
        >
    </option-description>
</option>
*/