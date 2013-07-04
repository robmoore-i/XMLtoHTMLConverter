import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class TranslatorTest {
    @Test
    public void canRemoveCDATAFromXML() {
        XMLTranslator translator = new XMLTranslator();
        assertThat(translator.translate(""), equalTo(""));
        assertThat(translator.translate("<![CDATA[]]>"), equalTo(""));
        assertThat(translator.translate("<![CDATA[blah blah blah]]>"), equalTo("blah blah blah"));
        assertThat(translator.translate("<![CDATA[!£\"\\\"$%^&*())]]>"), equalTo("!£\"\\\"$%^&*())"));
        assertThat(translator.translate("blahblahblah"), equalTo("blahblahblah"));
    }
}