public class HTMLFormatter {

    public String finishPage(String unfinishedHTMLDocument) {
        return "<!DOCTYPE html><html><body>" + unfinishedHTMLDocument + "</body></html>";
    }
}