public class XMLDataFormatter {
    public String makeTableForTagAttributes(XMLAttribute[] attributes) {
        String headerRow = makeHeaderRow(attributes);
        String bodyRow = makeBodyRow(attributes);
        return "<table>" + headerRow + bodyRow + "</table>";
    } //DONE

    private String makeHeaderRow(XMLAttribute[] attributes) {
        String headerRow = "";
        for (int i = 0; i < attributes.length - 1; i++) {
            XMLAttribute attribute = attributes[i];
            headerRow += "<th>" + attribute.name + "</th>";
        }
        return "<tr>" + headerRow + "</tr>";
    }

    private String makeBodyRow(XMLAttribute[] attributes) {
        String bodyRow = "";
        for (int i = 0; i < attributes.length - 1; i++) {
            XMLAttribute attribute = attributes[i];
            bodyRow += "<td>" + attribute.value + "</td>";
        }
        return "<tr>" + bodyRow + "</tr>";
    }

    public String finishPage(String penultimateOutput) {
        return "<!DOCTYPE html><html><body>" + penultimateOutput + "</body></html>";
    }
}