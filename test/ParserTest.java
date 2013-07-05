import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ParserTest {
    @Test
    public void canParseOpeningTagData() {
        XMLParser parser = new XMLParser();
        XMLTag xmlTag = parser.getXMLTagFromOpeningTag("<option name=\"attributes-src-port\" type=\"Integer\" default=\"0\" java=\"true\">");
        XMLAttribute[] attributes = xmlTag.attributes;
        assertThat(attributes[0].name, equalTo("name"));
        assertThat(attributes[0].value, equalTo("attributes-src-port"));
        assertThat(xmlTag.tagClass, equalTo("option"));
    }

    @Test
    public void canGetTagClassFromOpeningTag() {
        XMLParser parser = new XMLParser();
        XMLTag xmlTag1 = parser.getXMLTagFromOpeningTag("<option>");
        assertThat(xmlTag1.tagClass, equalTo("option"));
        XMLTag xmlTag2 = parser.getXMLTagFromOpeningTag("<tag>");
        assertThat(xmlTag2.tagClass, equalTo("tag"));
    }

    @Test
    public void canCheckThatAngleBracketsBalanced() {
        XMLParser parser = new XMLParser();
        assertThat(parser.checkThatAngleBracketsBalanced("<option><tag-description></tag-description></option>"), equalTo(true));
        assertThat(parser.checkThatAngleBracketsBalanced("<option></option>"), equalTo(true));
        assertThat(parser.checkThatAngleBracketsBalanced("<option><option>"), equalTo(false));
        assertThat(parser.checkThatAngleBracketsBalanced("</option></option>"), equalTo(false));
        assertThat(parser.checkThatAngleBracketsBalanced("<option name=\"blah\" type=\"blah\"></option>"), equalTo(true));
    }

    @Test
    public void canGetTagClassFromFullTag() {
        XMLParser parser = new XMLParser();
        assertThat(parser.getTagClass("<option></option>"), equalTo("option"));
        assertThat(parser.getTagClass("<option><option-x></option-x></option>"), equalTo("option"));
        assertThat(parser.getTagClass("<option><option-description>blah</option-description></option>"), equalTo("option"));
        assertThat(parser.getTagClass("<option-description>blah</option-description>"), equalTo("option-description"));
    }

    @Test
    public void canGetContentOfTag() {
        XMLParser parser = new XMLParser();
        assertThat(parser.getContentOfTag("option", "<option>blah-blah-blah</option>"), equalTo("blah-blah-blah"));
        assertThat(parser.getContentOfTag("option", "<option name=\"http-port\">blah-blah-blah</option>"), equalTo("blah-blah-blah"));
        assertThat(parser.getContentOfTag("option", "<option name=\"http-port\"><option-description>blah-blah-blah</option-description></option>"), equalTo("<option-description>blah-blah-blah</option-description>"));
    }

    @Test
    public void canGetDescriptionFromFullTag() {
        XMLParser parser = new XMLParser();
        XMLTranslator translator = new XMLTranslator();
        assertThat(parser.getDescriptionContentFromFullTag("<option><option-description>blah</option-description></option>", translator), equalTo("blah"));
        assertThat(parser.getDescriptionContentFromFullTag("<option><option-description>blah</option-description></option>", translator), equalTo("blah"));
        assertThat(parser.getDescriptionContentFromFullTag("<option><![CDATA[blah blah]]></option>", translator), equalTo(null));
        assertThat(parser.getDescriptionContentFromFullTag("<option name=\"http-port\"><option-description><![CDATA[blah-blah-blah]]></option-description></option>", translator), equalTo("blah-blah-blah"));
    }
}

/*
<option name="attributes-src-port" type="Integer" default="0" java="true">
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