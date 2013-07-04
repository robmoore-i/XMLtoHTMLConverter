import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class TranslatorTest {
    @Test
    public void canRemoveCDATAFromXML() {
        XMLTranslator translator = new XMLTranslator();
        assertThat(translator.translateDescription(""), equalTo(""));
        assertThat(translator.translateDescription("<![CDATA[]]>"), equalTo(""));
        assertThat(translator.translateDescription("<![CDATA[blah blah blah]]>"), equalTo("blah blah blah"));
        assertThat(translator.translateDescription("<![CDATA[!£\"\\\"$%^&*())]]>"), equalTo("!£\"\\\"$%^&*())"));
        assertThat(translator.translateDescription("blah-blah-blah"), equalTo("blah-blah-blah"));
        assertThat(translator.translateDescription(
                "<![CDATA[Network port to listen for connections from DataSource peers. The default of 0 means that no connections can be made to this DataSource.]]>"),
                equalTo("Network port to listen for connections from DataSource peers. The default of 0 means that no connections can be made to this DataSource."));
    }

    @Test
    public void optionTagTranslationTest() {
        XMLTranslator translator = new XMLTranslator();
        XMLParser parser = new XMLParser();
        assertThat(translator.translateAloneOptionTag("<option name=\"datasrc-port\" type=\"Integer\" default=\"0\" java=\"true\"><option-description>" +
                "<![CDATA[Network port to listen for connections from DataSource peers. The default of 0 means that no connections can be made to this DataSource." +
                "]]></option-description></option>", parser),
                equalTo(
                        "<h3><div>datasrc-port</div></h3><p><div>Network port to listen for connections from DataSource peers. The default of 0 means that no connections can be made to this DataSource.</div></p><table><tr><td>type</td><td>Integer</td></tr><tr><td>default</td><td>0</td></tr><tr><td>java</td><td>true</td></tr></table>"
                ));
    }
}