public class XMLTag {
    public final String tagClass;
    public final XMLAttribute[] attributes;

    public XMLTag(String tagClass, XMLAttribute[] attributes) {
        this.tagClass = tagClass;
        this.attributes = attributes;
    }
}