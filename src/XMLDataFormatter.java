public class XMLDataFormatter {

    public String makeTableFromXMLTag(XMLTag tag) {
        return "<table border=\"1\">" + makeTableHeaderRow(tag) + makeTableBodyRow(tag) + "</table>";
    }

    private String makeTableHeaderRow(XMLTag tag) {
        String tableRowData = "";
        for (XMLAttribute attribute : tag.attributes) {
            tableRowData = tableRowData.concat("<th>" + attribute.name + "</th>");
        }
        return "<tr>" + tableRowData + "</tr>";
    }

    private String makeTableBodyRow(XMLTag tag) {
        String tableRowData ="";
        for (XMLAttribute attribute : tag.attributes) {
            tableRowData = tableRowData.concat("<td>" + attribute.value + "</td>");
        }
        return tableRowData;
    }
}
