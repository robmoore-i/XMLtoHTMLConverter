public class XMLParser { //clean code n shit: methods do 1 thing, name things well, write good tests, no magic strings.
    private static final String emptyAngleBrackets = "<>";
    private static final String closingAngleBrackets = "</>";

    public XMLTag getXMLTagFromOpeningTag(String string) {
        String inputWithoutAngleBrackets = removeAngleBrackets(string);
        String tagClass = inputWithoutAngleBrackets.split(" ")[0];
        String[] tagAttributeAssignments = changeInputToNameValueFormat(inputWithoutAngleBrackets);
        XMLAttribute[] xmlAttributes = getXMLAttributes(tagAttributeAssignments);
        return new XMLTag(tagClass, xmlAttributes);
    } //DONE

    private XMLAttribute[] getXMLAttributes(String[] tagAttributeAssignments) {
        XMLAttribute[] xmlAttribute = new XMLAttribute[tagAttributeAssignments.length];
        for (int i = 1, j = 0; i < tagAttributeAssignments.length; i++) {
            xmlAttribute[j] = new XMLAttribute(tagAttributeAssignments[i].split("=")[0], tagAttributeAssignments[i].split("=")[1]);
            j++;
        }
        return xmlAttribute;
    }

    private String[] changeInputToNameValueFormat(String string) {
        String splitByQuotes = StringUtils.rejoinStringArray(string.split("\""), 0);
        return splitByQuotes.split(" ");
    }

    private String removeAngleBrackets(String string) {
        String removeAngleBrackets = string;
        removeAngleBrackets = removeAngleBrackets.replace("<", "");
        removeAngleBrackets = removeAngleBrackets.replace(">", "");
        return removeAngleBrackets;
    }

    public boolean checkThatAngleBracketsBalanced(String inputString) {
        String[] stringArray = StringToArrayOfStringsRepresentingEachCharacter(inputString);
        String[] filteredStringArray = filterStringArray(">" + "<" + "/", stringArray); //three characters that form the base of the XML grammar.
        String filteredInput = StringUtils.rejoinStringArray(StringUtils.removeNulls(filteredStringArray), 0);
        return filteredInput.startsWith(emptyAngleBrackets) &&
                filteredInput.endsWith(closingAngleBrackets) &&
                (checkThatAngleBracketsBalanced(getContentOfTag("", filteredInput)) ||
                        filteredInput.length() == 5); //<>Y</> Y is nothing or Y is balanced.
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

    private String[] StringToArrayOfStringsRepresentingEachCharacter(String string) {
        char[] charArrayOfString = string.toCharArray();
        String[] stringArrayOfString = new String[charArrayOfString.length];
        for (int i = 0; i < charArrayOfString.length; i++) {
            stringArrayOfString[i] = String.valueOf(charArrayOfString[i]);
        }
        return stringArrayOfString;
    }

    private String[] filterStringArray(String filter, String[] array) {
        String[] result = new String[array.length];
        for (int i = 0, j = 0; i < array.length; i++) {
            if (filter.contains(array[i])) {
                result[j] = array[i];
                j++;
            }
        }
        return result;
    }

    public String getTagClass(String inputString) {
        String openingTag = getOpeningTagFromTotalTag(inputString);
        String inputWithoutAngleBrackets = removeAngleBrackets(openingTag);
        return inputWithoutAngleBrackets.split(" ")[0];
    } //DONE

    public String getOpeningTagFromTotalTag(String totalTag) {
        int positionOfFirstClosingAngleBracket = getPositionOfFirstClosingAngleBracket(totalTag);
        return totalTag.substring(0, positionOfFirstClosingAngleBracket);
    } //DONE

    private int getPositionOfFirstClosingAngleBracket(String inputString) {
        int positionOfFirstClosingAngleBracket = 0;
        String[] stringArray = StringToArrayOfStringsRepresentingEachCharacter(inputString);
        for (int i = 0; i < stringArray.length; i++) {
            String string = stringArray[i];
            if (string.equals(">")) {
                positionOfFirstClosingAngleBracket = i + 1;
                break;
            }
        }
        return positionOfFirstClosingAngleBracket;
    }

    public String getDescriptionContentFromFullTag(String input) {
        String tagClass = getTagClass(input);
        if (input.contains("-description")) {
            if (!tagClass.endsWith("-description")) {
                return getDescriptionContentFromFullTag(getContentOfTag(tagClass, input));
            } else {
                return cleanDescription(getContentOfTag(tagClass, input));
            }
        } else {
            return null;
        }
    } //DONE

    public String cleanDescription(String descriptionText) {
        String editedDescription = descriptionText;
        editedDescription = editedDescription.replace("<![CDATA[", "");
        editedDescription = editedDescription.replace("]]>", "");
        editedDescription = editedDescription.replace("\\verbatim", "");
        editedDescription = editedDescription.replace("\\endverbatim", "");
        return editedDescription;
    } //DONE
}