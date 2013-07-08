public class XMLParser { //clean code n shit: methods do 1 thing, name things well, write good tests, no magic strings.
    private static final String emptyAngleBrackets = "<>";
    private static final String closingAngleBrackets = "</>";

    public XMLTag getXMLTagFromOpeningTag(String string) {
        String inputWithoutAngleBrackets = ParserUtils.removeAngleBrackets(string);
        String tagClass = inputWithoutAngleBrackets.split(" ")[0];
        String[] tagAttributeAssignments = ParserUtils.changeInputToNameValueFormat(inputWithoutAngleBrackets, tagClass);
        XMLAttribute[] xmlAttributes = ParserUtils.getXMLAttributes(tagAttributeAssignments);
        return new XMLTag(tagClass, xmlAttributes);
    } //DONE

    public boolean areBracketsBalanced(String inputString) {
        String[] stringArray = ParserUtils.getCharacterMap(inputString);
        String[] filteredStringArray = ParserUtils.filterStringArray(">" + "<" + "/", stringArray); //three characters that form the base of the XML grammar.
        String filteredInput = ParserUtils.rejoinStringArray(ParserUtils.removeNulls(filteredStringArray), 0);
        return filteredInput.startsWith(emptyAngleBrackets) &&
                filteredInput.endsWith(closingAngleBrackets) &&
                (areBracketsBalanced(getContentOfTag("", filteredInput)) ||
                        filteredInput.length() == 5); //<>Y</> Y is nothing or Y is balanced.
    } //DONE

    public String getDescriptionContentFromFullTag(String input) {
        String tagClass = getTagClass(input);
        if (input.contains("-description")) {
            if (!tagClass.endsWith("-description")) {
                return getDescriptionContentFromFullTag(getContentOfTag(tagClass, input));
            } else {
                return ParserUtils.cleanDescription(getContentOfTag(tagClass, input));
            }
        } else {
            return null;
        }
    } //DONE

    public String getContentOfTag(String tagClass, String input) {
        String openingTag = getOpeningTagFromTotalTag(input);
        if (openingTag.length() == tagClass.length() + 2) {
            return input.substring(2 + tagClass.length(), input.length() - 3 - tagClass.length());
        } else {
            String attributes = input.substring(1 + tagClass.length(), openingTag.length() - 1);
            String newOpeningTag = openingTag.replace(attributes, "");
            String newInput = input.replace(openingTag, newOpeningTag);
            return getContentOfTag(tagClass, newInput);
        }
    } //DONE

    public String getTagClass(String inputString) {
        String openingTag = getOpeningTagFromTotalTag(inputString);
        String inputWithoutAngleBrackets = ParserUtils.removeAngleBrackets(openingTag);
        return inputWithoutAngleBrackets.split(" ")[0];
    } //DONE

    public String getOpeningTagFromTotalTag(String totalTag) {
        int positionOfFirstClosingAngleBracket = ParserUtils.getPositionOfFirstClosingAngleBracket(totalTag);
        return totalTag.substring(0, positionOfFirstClosingAngleBracket);
    } //DONE
}