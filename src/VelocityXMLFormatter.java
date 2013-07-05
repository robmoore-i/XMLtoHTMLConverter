import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.StringWriter;
import java.util.Properties;

public class VelocityXMLFormatter {
    private VelocityContext context = new VelocityContext();
    private StringWriter output = new StringWriter();
    private XMLParser parser = new XMLParser();
    private final static String templatePath = "/templates/";

    public VelocityXMLFormatter() {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(properties);
    }

    private Template getTemplate(String templateName) {
        Template template = null;
        try {
            template = Velocity.getTemplate(templatePath + templateName + ".vm");
        } catch (ResourceNotFoundException ignored) {
        } catch (ParseErrorException ignored) {
        } catch (MethodInvocationException ignored) {
        }
        assert template != null;
        return template;
    }

    public String formatOption_OptionDescription(String input) {
        XMLTag tag = parser.getXMLTagFromOpeningTag(parser.getOpeningTagFromTotalTag(input));
        String description = parser.getDescriptionContentFromFullTag(input);
        context.put(tag.attributes[0].name, tag.attributes[0].value);
        context.put(tag.attributes[1].name, tag.attributes[1].value);
        try {
            context.put(tag.attributes[2].name, tag.attributes[2].value);
        } catch (Exception ignored) {
        }
        try {
            context.put(tag.attributes[3].name, tag.attributes[3].value);
        } catch (Exception ignored) {
        }
        context.put("description", description);
        write(getTemplate("Option_OptionDescription"));
        return String.valueOf(output);
    }

    public String formatOption_AcceptableValues_OptionDescription(XMLAttribute[] attributes, XMLTag[] acceptableValues, String description) {
        return String.valueOf(output);
    }

    public String formatPage_TopDescription(String name, String description) {
        return String.valueOf(output);
    }

    private void write(Template template) {
        template.merge(context, output);
    }
}
