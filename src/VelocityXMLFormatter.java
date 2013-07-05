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

    private void formatOption_OptionDescription(XMLAttribute[] attributes, String description) {

    }

    private void formatOption_AcceptableValues_OptionDescription(XMLAttribute[] attributes, XMLTag[] acceptableValues, String description) {
    }

    private void page_TopDescription(String name, String description) {
    }

    private void write(Template template) {
        template.merge(context, output);
    }
}
