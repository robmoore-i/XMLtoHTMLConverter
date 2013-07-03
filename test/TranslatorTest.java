import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class TranslatorTest {
    @Test
    public void canRemoveCDATAFromXML() {
        Translator translator = new Translator();
        assertThat(translator.translate("<![CDATA[blahblahblah]]>"), equalTo("blahblahblah"));
    }
}
