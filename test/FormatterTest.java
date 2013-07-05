import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class FormatterTest {

    @Test
    public void canSetupPageCorrectly() {
        HTMLFormatter formatter = new HTMLFormatter();
        assertThat(formatter.finishPage("<p>I'm a paragraph</p>"), equalTo("<!DOCTYPE html><html><body><p>I'm a paragraph</p></body></html>"));
    }
}