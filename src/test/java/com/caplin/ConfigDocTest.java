package com.caplin;

import org.apache.velocity.VelocityContext;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

import static org.hamcrest.core.IsEqual.equalTo;
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
    public void itWritesToTemplateCorrectly() {
        String input = "<DSDK><page name=\"name\"><top-description>description</top-description></page></DSDK>";
        ConfigDoc configDoc = new ConfigDoc(new ByteArrayInputStream(input.getBytes()));
        VelocityContext context = new VelocityContext();
        context.put("title", "name");
        context.put("body", "body");
        context.put("description", "description");
        assertThat(String.valueOf(configDoc.writeFromTemplate(context, "page.vm")).replace("\r\n", ""), equalTo("<!DOCTYPE html><html><head><title>name</title></head><body><h1>name</h1>description<br/>body</body></html>"));
    }

    @Test
    public void canCleanDescription() {
        String input = "<![CDATA[\\verbatimHI\\endverbatim\\noteThis is a note\\anchorWhat does an anchor do?\\refRefere-Format: thingy [value]]]>";
        ConfigDoc configDoc = new ConfigDoc(new ByteArrayInputStream(input.getBytes()));
        assertThat(configDoc.cleanDescription(input), equalTo("HI<br/>Note:This is a noteWhat does an anchor do?Refere-<br/>Format:<br/> thingy [value]<br/>"));
    }

    @Test
    public void canTemplatePage() throws Exception {
        String input = "<page name=\"name\"><top-description>description</top-description>body</page>";
        ConfigDoc configDoc = new ConfigDoc(new ByteArrayInputStream(input.getBytes()));
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(new ByteArrayInputStream(input.getBytes()));
        Node rootElement = document.getDocumentElement();
        assertThat(configDoc.templatePage(rootElement).replace("\r\n", ""), equalTo("<!DOCTYPE html><html><head><title>name</title></head><body><h1>name</h1>description<br/></body></html>"));
    }

    @Test
    public void canTemplateGroup() throws Exception {
        String input = "<group name=\"name\"><group-description>description</group-description>body</group>";
        ConfigDoc configDoc = new ConfigDoc(new ByteArrayInputStream(input.getBytes()));
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(new ByteArrayInputStream(input.getBytes()));
        Node rootElement = document.getDocumentElement();
        assertThat(configDoc.templateGroup(rootElement).replace("\r\n", ""), equalTo("<h3>Group: name</h3>description<br/><br/><br/>End of group: name<br/>"));
    }

    @Test
    public void canTemplateOption() throws Exception {
        String input = "<option name=\"name\" type=\"type\"><option-description>description</option-description></option>";
        ConfigDoc configDoc = new ConfigDoc(new ByteArrayInputStream(input.getBytes()));
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(new ByteArrayInputStream(input.getBytes()));
        Node rootElement = document.getDocumentElement();
        assertThat(configDoc.cleanPage(configDoc.templateOption(rootElement)), equalTo("Option:<br>description<table border=\"1\"><tr><td>name</td><td>name</td></tr><tr><td>type</td><td>type</td></tr></table><br/>"));
    }

    @Test
    public void canCleanPage() {
        String input = "\r\nThis\r te\n\nx\rt sh\r\noul\r\nd be \nvis\rible";
        ConfigDoc configDoc = new ConfigDoc(new ByteArrayInputStream(input.getBytes()));
        assertThat(configDoc.cleanPage(input), equalTo("This text should be visible"));
    }

    @Test
    public void canTemplateAcceptableValues() throws Exception {
        String input = "<acceptableValues><enum name=\"name1\" value=\"1\"><enum-description>description1</enum-description></enum><enum name=\"name2\" value=\"2\"><enum-description>description2</enum-description></enum></acceptableValues>";
        ConfigDoc configDoc = new ConfigDoc(new ByteArrayInputStream(input.getBytes()));
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(new ByteArrayInputStream(input.getBytes()));
        Node rootElement = document.getDocumentElement();
        assertThat(configDoc.cleanPage(configDoc.templateAcceptableValues(rootElement)), equalTo("<table border=\"1\"><tr><td>name</td><td>value</td><td>description</td></tr><tr><td>name1</td><td>1</td><td>description1</td></tr><tr><td>name2</td><td>2</td><td>description2</td></tr></table>"));
    }

    @Test
    public void canTemplateNavigation() throws Exception {
        String[] pageNames = new String[]{"p1", "p2", "p3"};
        ConfigDoc configDoc = new ConfigDoc(new ByteArrayInputStream("".getBytes()));
        assertThat(configDoc.cleanPage(configDoc.templateNavigation(pageNames)), equalTo("<h1>Navigation page</h1><ul><li>p1</li><li>p2</li><li>p3</li></ul>"));
    }
}