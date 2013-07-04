public class XMLParser { //clean code n shit: methods do 1 thing, name things well, write good tests.
    private static final String emptyAngleBrackets = "<>";
    private static final String closingAngleBrackets = "</>";

    public XMLTag getXMLTagFromOpeningTag(String string) {
        String inputWithoutAngleBrackets = removeAngleBrackets(string);
        String tagClass = inputWithoutAngleBrackets.split(" ")[0];
        String[] tagAttributeAssignments = changeInputToNameValueFormat(inputWithoutAngleBrackets);
        XMLData[] xmlData = getXmlData(tagAttributeAssignments);
        return new XMLTag(tagClass, xmlData);
    } //DONE

    private XMLData[] getXmlData(String[] tagAttributeAssignments) {
        XMLData[] xmlData = new XMLData[tagAttributeAssignments.length];
        int j = 0;
        for (int i = 1; i < tagAttributeAssignments.length; i++) {
            xmlData[j] = new XMLData(tagAttributeAssignments[i].split("=")[0], tagAttributeAssignments[i].split("=")[1]);
            j++;
        }
        return xmlData;
    }

    private String[] changeInputToNameValueFormat(String string) {
        String splitByQuotes = StringTool.rejoinStringList(string.split("\""), 0);
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
        String filteredInput = StringTool.rejoinStringList(removeNulls(filteredStringArray), 0);
        return filteredInput.startsWith(emptyAngleBrackets) &&
                filteredInput.endsWith(closingAngleBrackets) &&
                (checkThatAngleBracketsBalanced(getContentOfTag("", filteredInput)) ||
                        filteredInput.length() == 5); //<>Y</> Y is nothing or Y is balanced.
    } //DONE

    private String getContentOfTag(String tagClass, String input) {
        return input.substring(2 + tagClass.length(), input.length() - 3 - tagClass.length());
    }

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

    private String[] removeNulls(String[] array) {
        int newSize = findSizeOfNewArray(array);
        return populateNewArray(array, newSize);
    }

    private String[] populateNewArray(String[] array, int newSize) {
        String[] tempArray = new String[newSize];
        System.arraycopy(array, 0, tempArray, 0, newSize);
        return tempArray;
    }

    private int findSizeOfNewArray(String[] array) {
        int newSize = 0;
        for (String anArray : array) {
            if (anArray != null) {
                newSize++;
            }
        }
        return newSize;
    }

    public String getTagClass(String inputString) {
        int positionOfFirstClosingAngleBracket = getPositionOfFirstClosingAngleBracket(inputString);
        String openingTag = inputString.substring(0, positionOfFirstClosingAngleBracket);
        String inputWithoutAngleBrackets = removeAngleBrackets(openingTag);
        return inputWithoutAngleBrackets.split(" ")[0];
    } //DONE

    private int getPositionOfFirstClosingAngleBracket(String inputString) {
        int positionOfFirstClosingAngleBracket = 0;
        String[] stringArray = StringToArrayOfStringsRepresentingEachCharacter(inputString);
        for (int i = 0; i < stringArray.length; i++) {
            String string = stringArray[i];
            if(string.equals(">")) {
                positionOfFirstClosingAngleBracket = i;
                break;
            }
        }
        return positionOfFirstClosingAngleBracket;
    }

    public String getDescriptionContentFromFullTag(String input, XMLTranslator translator) {
        String tagClass = getTagClass(input);
        if (!tagClass.endsWith("-description")) {
            return getDescriptionContentFromFullTag(getContentOfTag(tagClass, input), translator);
        } else {
            return translator.translate(getContentOfTag(tagClass, input));
        }
    } //DONE
}