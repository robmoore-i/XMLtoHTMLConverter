package main.java.com.caplin;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;


public class ConfigDoc {

    private InputStream input;

    public ConfigDoc(InputStream input) {
        this.input = input;
        Velocity.setProperty("directive.set.null.allowed", true);
    }

    public String parse() throws Exception {
        VelocityContext context = new VelocityContext();
        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = dBuilder.parse(input);
        Node rootElement = doc.getDocumentElement();
        if (!"DSDK".equals(rootElement.getNodeName())) {
            throw new Exception("Incorrect root node. Should be DSDK");
        }
        return parseNode(rootElement, context);
    }

    private String parseNode(Node currentNode, VelocityContext context) throws Exception {
        StringBuilder output = new StringBuilder("");
        NodeList nodeList = currentNode.getChildNodes();
        int pageNumber = 1;
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node currentChild = nodeList.item(count);
            String nodeName = currentChild.getNodeName().toLowerCase();
            if (nodeName.equals("page")) {
                String templatedPage = templatePage(currentChild, context);
                makeNewFile(currentChild.getAttributes().getNamedItem("name").getNodeValue() + " pageNumber-" + pageNumber, templatedPage);
                output.append(templatedPage);
                pageNumber++;
            } else if (nodeName.equals("option")) {
                String templatedOption = templateOption(currentChild, context);
                output.append(templatedOption);
            } else if (nodeName.equals("options")) {
                String templatedOptions = templateOptions(currentChild, context);
                output.append(templatedOptions);
            } else if (nodeName.equals("group")) {
                String templatedGroup = templateGroup(currentChild, context);
                output.append(templatedGroup);
            }
        }
        return output.toString();
    }

    //<UTILS>
    private StringWriter writeFromTemplate(VelocityContext context, String templateName) {
        Template template = Velocity.getTemplate("\\src\\main\\resources\\" + templateName);
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);
        return stringWriter;
    }

    private void makeNewFile(String pageName, String contents) throws IOException {
        File writeFile = new File("C:/Users/robertm/Desktop/" + pageName + ".html");
        FileWriter fileWriter = new FileWriter(writeFile.getAbsoluteFile());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(contents);
        bufferedWriter.close();
    }

    private String cleanDescription(String descriptionText) {
        String editedDescription = descriptionText;
        editedDescription = editedDescription.replace("<![CDATA[", "");
        editedDescription = editedDescription.replace("]]>", "");
        editedDescription = editedDescription.replace("\\verbatim", "");
        editedDescription = editedDescription.replace("\\endverbatim", "");
        return editedDescription;
    }
    //</UTILS>

    //<PAGE>
    private String templatePage(Node node, VelocityContext context) throws Exception {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("<h2>").append(node.getAttributes().getNamedItem("name").getNodeValue()).append("</h2>");
        stringBuilder.append("<div>").append(cleanDescription(node.getFirstChild().getTextContent())).append("</div>");
        for (int i = 1; i < node.getChildNodes().getLength(); i++) { //start at 1 to miss out "top-description" which is done above
            stringBuilder.append(parseNode(node.getChildNodes().item(i), context)).append("<div></div>");
        }
        return stringBuilder.toString();
    } //PAGE
    //</PAGE>

    //<GROUP>
    private String templateGroup(Node node, VelocityContext context) throws Exception {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("<h3>").append("Group: ").append(node.getAttributes().getNamedItem("name").getNodeValue()).append("</h3>");
        stringBuilder.append(cleanDescription(node.getFirstChild().getTextContent())).append("");
        stringBuilder.append(parseNode(node, context));
        stringBuilder.append("<div>End of group: ").append(node.getAttributes().getNamedItem("name").getNodeValue()).append("</div>");
        return stringBuilder.toString();
    } //DONE
    //</GROUP>

    //<OPTION>
    private String templateOption(Node node, VelocityContext context) throws Exception {
        assignOptionAttributesToVelocityContext(node, context);
        String templatedAcceptableValues = templateContentsOfOption(node, context);
        StringWriter stringWriter = writeFromTemplate(context, "option.vm");
        return (String.valueOf(stringWriter) + templatedAcceptableValues);
    } //DONE

    private String templateContentsOfOption(Node node, VelocityContext context) {
        String templatedAcceptableValues = "";
        if (node.getChildNodes().getLength() == 1) {
            templatedAcceptableValues = templateContentsOfOptionWithoutAcceptableValues(node, context);
        } else if (node.getChildNodes().getLength() == 2) {
            templatedAcceptableValues = templateContentsOfOptionWithAcceptableValues(node, context);
        }
        return templatedAcceptableValues;
    }

    private String templateContentsOfOptionWithoutAcceptableValues(Node node, VelocityContext context) {
        context.put("description", cleanDescription(node.getFirstChild().getTextContent()));
        return ""; //if this is called then there are no acceptable values so an empty string should be returned by the function "templateContentsOfOption"
    }

    private String templateContentsOfOptionWithAcceptableValues(Node node, VelocityContext context) {
        String templatedAcceptableValues = templateAcceptableValues(node.getFirstChild(), context);
        context.put("description", cleanDescription(node.getLastChild().getTextContent()));
        return templatedAcceptableValues;
    }

    private void assignOptionAttributesToVelocityContext(Node node, VelocityContext context) {
        String[] possibleAttributes = new String[]{"name", "type", "default", "deprecated", "java", "min", "max"};
        for (String possibleAttribute : possibleAttributes) {
            try {
                context.put(possibleAttribute, node.getAttributes().getNamedItem(possibleAttribute).getNodeValue());
            } catch (Exception ignored) {
            }
        }
    }

    private String templateAcceptableValues(Node node, VelocityContext context) {
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node currentChild = node.getChildNodes().item(i);
            context.put("name" + String.valueOf(i), currentChild.getAttributes().getNamedItem("name").getTextContent());
            context.put("value" + String.valueOf(i), currentChild.getAttributes().getNamedItem("value").getTextContent());
            context.put("description" + String.valueOf(i), cleanDescription(currentChild.getFirstChild().getTextContent()));
        }
        StringWriter stringWriter = writeFromTemplate(context, "acceptableValues.vm");
        return String.valueOf(stringWriter);
    }
    //</OPTION>

    //<OPTIONS>
    private String templateOptions(Node node, VelocityContext context) throws Exception {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node currentChild = node.getChildNodes().item(i);
            output.append(templateOption(currentChild, context)).append("<div></div>");
        }
        return output.toString();
    } //DONE
    //</OPTIONS>

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java -jar <inputFileName>");
            return;
        }

        BufferedReader bufferedReader = null;
        String currentLine;
        String input = "";
        bufferedReader = new BufferedReader(new FileReader(args[0]));
        while ((currentLine = bufferedReader.readLine()) != null) {
            input = input.concat(currentLine);
        }
        ConfigDoc configDoc = new ConfigDoc(new ByteArrayInputStream(input.getBytes()));
        String output = configDoc.parse();
    }
}