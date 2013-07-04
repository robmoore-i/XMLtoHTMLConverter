public class XMLTag {
    public final String tagClass;
    public final XMLData[] data;

    public XMLTag(String tagClass, XMLData[] data) {
        this.tagClass = tagClass;
        this.data = data;
    }
}