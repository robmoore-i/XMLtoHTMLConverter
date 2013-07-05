public class XMLTranslator {
    public String translateDescription(String descriptionText) {
        String editedDescription = descriptionText;
        editedDescription = removeCDATA(editedDescription);
        editedDescription = removeVerbatim(editedDescription);
        return editedDescription;
    } //DONE

    private String removeVerbatim(String editedDescription) {
        editedDescription = editedDescription.replace("\\verbatim", "");
        editedDescription = editedDescription.replace("\\endverbatim", "");
        return editedDescription;
    }

    private String removeCDATA(String editedDescription) {
        editedDescription = editedDescription.replace("<![CDATA[", "");
        editedDescription = editedDescription.replace("]]>", "");
        return editedDescription;
    }

    public String translateAloneOptionTag(String totalOptionTag, XMLParser parser) {
        String openingTagFromTotalTag = parser.getOpeningTagFromTotalTag(totalOptionTag);
        XMLTag tag = parser.getXMLTagFromOpeningTag(openingTagFromTotalTag);
        String title = tag.attributes[0].value;
        String description = translateDescription(parser.getDescriptionContentFromFullTag(totalOptionTag, this));
        String table = buildTable(tag);
        return "<h3><div>" + title + "</div></h3>" + "<p><div>" + description + "</div></p>" + table;
    }

    private String buildTable(XMLTag tag) {
        String tableContents = "";
        for (int i = 1; i < tag.attributes.length - 1; i++) {
            tableContents += "<tr><td>" + tag.attributes[i].name + "</td><td>" + tag.attributes[i].value + "</td></tr>";
        }
        return "<table border=\"1\">" + tableContents + "</table>";
    }
}