package com.caplin;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;

public class ConfigDoc {
    private InputStream input;

    public ConfigDoc(InputStream input) {
        this.input = input;
        Velocity.setProperty("directive.set.null.allowed", true);
    }

    public void parse() throws Exception {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(input);
        Node rootElement = document.getDocumentElement();
        if (!"DSDK".equals(rootElement.getNodeName())) {
            throw new Exception("Incorrect root node. Should be DSDK");
        }
        NodeList pages = rootElement.getChildNodes();
        String[] pageNames = new String[pages.getLength()];
        for (int i = 0; i < pages.getLength(); i++) {
            Node page = pages.item(i);
            makeNewFile(page.getAttributes().getNamedItem("name").getNodeValue(), templatePage(page));
            pageNames[i] = page.getAttributes().getNamedItem("name").getNodeValue();
            //gg
        }
        makeNewFile("Navigation page", templateNavigation(pageNames));
    }

    private String parseNode(Node currentNode) throws Exception {
        StringBuilder output = new StringBuilder("");
        NodeList nodeList = currentNode.getChildNodes();
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node currentChild = nodeList.item(count);
            String nodeName = currentChild.getNodeName().toLowerCase();
            switch (nodeName) {
                case "option":
                    output.append(templateOption(currentChild));
                    break;
                case "options":
                    output.append(templateOptions(currentChild));
                    break;
                case "group":
                    output.append(templateGroup(currentChild));
                    break;
            }
        }
        return output.toString();
    }

    //<UTILS>
    public String writeFromTemplate(VelocityContext context, String templateName) {
        Template template = Velocity.getTemplate("\\src\\main\\resources\\" + templateName);
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);
        return stringWriter.toString();
    } //TESTED

    private void makeNewFile(String pageName, String contents) throws IOException {
        File writeFile = new File("C:/Users/robertm/Desktop/" + pageName + ".html");
        FileWriter fileWriter = new FileWriter(writeFile.getAbsoluteFile());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(contents);
        bufferedWriter.close();
    } //UNTESTABLE

    public String cleanDescription(String descriptionText) {
        String editedDescription = descriptionText;
        editedDescription = editedDescription.replace("<![CDATA[", "");
        editedDescription = editedDescription.replace("]]>", "");
        editedDescription = editedDescription.replace("\\verbatim", "");
        editedDescription = editedDescription.replace("\\endverbatim", "");
        editedDescription = editedDescription.replace("\\note", "<br/>Note:");
        editedDescription = editedDescription.replace("\\anchor", "");
        editedDescription = editedDescription.replace("\\ref", "");
        editedDescription = editedDescription.replace("Format:", "<br/>Format:<br/>");
        editedDescription = editedDescription.replace("[value]", "[value]<br/>");
        return editedDescription;
    } //TESTED

    public String cleanPage(String page) {
        return page.replace("\r\n", "").replace("\n", "").replace("\r", "");
    } //TESTED
    //</UTILS>

    //<NAVIGATION>
    public String templateNavigation(String[] pageNames) throws Exception {
        VelocityContext context = new VelocityContext();
        context.put("pageNames", pageNames);
        return writeFromTemplate(context, "navigation.vm");
    } //TESTED
    //</NAVIGATION>

    //<PAGE>
    public String templatePage(Node node) throws Exception {
        VelocityContext context = new VelocityContext();
        context.put("title", node.getAttributes().getNamedItem("name").getNodeValue());
        context.put("description", cleanDescription(node.getFirstChild().getTextContent()));
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 1; i < node.getChildNodes().getLength(); i++) { //start at 1 to miss out "top-description" which is done above
            stringBuilder.append(parseNode(node.getChildNodes().item(i)));
        }
        context.put("body", stringBuilder.toString());
        return writeFromTemplate(context, "page.vm");
    } //TESTED
    //</PAGE>

    //<GROUP>
    public String templateGroup(Node node) throws Exception {
        VelocityContext context = new VelocityContext();
        context.put("title", node.getAttributes().getNamedItem("name").getNodeValue());
        context.put("description", cleanDescription(node.getFirstChild().getTextContent()));
        context.put("body", parseNode(node));
        return writeFromTemplate(context, "group.vm");
    } //TESTED
    //</GROUP>

    //<OPTIONS>
    private String templateOptions(Node node) throws Exception {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node currentChild = node.getChildNodes().item(i);
            output.append(templateOption(currentChild));
        }
        return output.toString();
    } //TESTED BY CONJECTURE
    //</OPTIONS>

    //<OPTION>
    public String templateOption(Node node) throws Exception {
        VelocityContext context = new VelocityContext();
        assignOptionAttributesToVelocityContext(node, context);
        String templatedAcceptableValues = templateContentsOfOption(node, context);
        String templatedOption = writeFromTemplate(context, "option.vm");
        return (templatedOption + templatedAcceptableValues + "<br/>");
    } //TESTED

    private String templateContentsOfOption(Node node, VelocityContext context) {
        String templatedContentsOfOption;
        if (node.getChildNodes().getLength() == 1) {
            templatedContentsOfOption = templateContentsOfOptionWithoutAcceptableValues(node, context);
        } else if (node.getChildNodes().getLength() == 2 && !node.getFirstChild().getNodeName().equals("shortName")) {
            templatedContentsOfOption = templateContentsOfOptionWithAcceptableValues(node, context);
        } else {
            templatedContentsOfOption = templateContentsOfOptionWithShortName(node, context);
        }
        return templatedContentsOfOption;
    }

    private String templateContentsOfOptionWithoutAcceptableValues(Node node, VelocityContext context) {
        context.put("description", cleanDescription(node.getFirstChild().getTextContent()));
        return ""; //if this is called then there are no acceptable values so an empty string should be returned by the function "templateContentsOfOption"
    }

    private String templateContentsOfOptionWithShortName(Node node, VelocityContext context) {
        context.put("description", cleanDescription(node.getLastChild().getTextContent()));
        return "";
    }

    private void assignOptionAttributesToVelocityContext(Node node, VelocityContext context) {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i < node.getAttributes().getLength(); i++) {
            names.add(node.getAttributes().item(i).getNodeName());
            values.add(node.getAttributes().item(i).getNodeValue());
        }
        context.put("names", names);
        context.put("values", values);
        context.put("length", names.size() - 1);
    }

    //<ACCEPTABLE-VALUES>
    private String templateContentsOfOptionWithAcceptableValues(Node node, VelocityContext context) {
        String templatedAcceptableValues = templateAcceptableValues(node.getFirstChild());
        context.put("description", cleanDescription(node.getLastChild().getTextContent()));
        return templatedAcceptableValues;
    }

    public String templateAcceptableValues(Node node) {
        VelocityContext context = new VelocityContext();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node currentChild = node.getChildNodes().item(i);
            names.add(currentChild.getAttributes().getNamedItem("name").getNodeValue());
            values.add(currentChild.getAttributes().getNamedItem("value").getNodeValue());
            descriptions.add(cleanDescription(currentChild.getFirstChild().getTextContent()));
        }
        context.put("names", names);
        context.put("values", values);
        context.put("descriptions", descriptions);
        context.put("length", names.size() - 1);
        return writeFromTemplate(context, "acceptableValues.vm");
    } //TESTED
    //</ACCEPTABLE-VALUES>
    //</OPTION>

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java -jar <inputFileName>");
            return;
        }

        BufferedReader bufferedReader;
        String currentLine;
        String input = "";
        bufferedReader = new BufferedReader(new FileReader(args[0]));
        while ((currentLine = bufferedReader.readLine()) != null) {
            input = input.concat(currentLine);
        }
        ConfigDoc configDoc = new ConfigDoc(new ByteArrayInputStream(input.getBytes()));
        configDoc.parse();
    }
}