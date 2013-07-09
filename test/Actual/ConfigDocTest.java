package Actual;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ConfigDocTest {

    @Test(expected = Throwable.class)
    public void itThrowsExceptionWhenNoFileExists() throws Exception {
        ConfigDoc configDoc = new ConfigDoc(new FileInputStream(new File("")));
        configDoc.parse();
    }

    @Test(expected = Throwable.class)
    public void itThrowsExceptionWhenFileIsntXml() throws Exception {
        String input = "";
        ConfigDoc configDoc = new ConfigDoc(new ByteArrayInputStream(input.getBytes()));
        configDoc.parse();
    }

    @Test(expected = Throwable.class)
    public void itThrowsExceptionWhenItDoesntHaveDsdkRootElement() throws Exception {
        String input = "<ROOT></ROOT>";
        ConfigDoc configDoc = new ConfigDoc(new ByteArrayInputStream(input.getBytes()));
        configDoc.parse();
    }

    @Test
    public void itTemplatesOptions() throws Exception {
        String input =
                "<DSDK>" +
                        "<option name=\"a\" type=\"b\" default=\"c\" deprecated=\"d\" java=\"e\">" +
                        "<acceptableValues>" +
                        "<enum name=\"name1\" value=\"value1\"><enum-description>description1</enum-description></enum>" +
                        "<enum name=\"name2\" value=\"value2\"><enum-description>description2</enum-description></enum>" +
                        "<enum name=\"name3\" value=\"value3\"><enum-description>description3</enum-description></enum>" +
                        "<enum name=\"name4\" value=\"value4\"><enum-description>description4</enum-description></enum>" +
                        "</acceptableValues>" +
                        "<option-description>hi</option-description>" +
                        "</option>" +
                        "</DSDK>";
        ConfigDoc configDoc = new ConfigDoc(new ByteArrayInputStream(input.getBytes()));
        assertThat(configDoc.parse().replace("\r\n", ""), equalTo(
                "<p>hi</p>" +
                        "<table border=\"1\">" +
                        "<tr><td>name</td><td>a</td></tr>" +
                        "<tr><td>type</td><td>b</td></tr>" +
                        "<tr><td>default</td><td>c</td></tr>" +
                        "<tr><td>deprecated</td><td>d</td></tr>" +
                        "<tr><td>java</td><td>e</td></tr>" +
                        "</table>" +
                        "<table border=\"1\">" +
                        "<tr><td>name</td><td>value</td><td>description</td></tr>" +
                        "<tr><td>name1</td><td>value1</td><td>description1</td></tr>" +
                        "<tr><td>name2</td><td>value2</td><td>description2</td></tr>" +
                        "<tr><td>name3</td><td>value3</td><td>description3</td></tr>" +
                        "<tr><td>name4</td><td>value4</td><td>description4</td></tr>" +
                        "</table>"));
    }
}
