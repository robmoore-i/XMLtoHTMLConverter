public class XMLParser { //clean code n shit.

    public XMLTag getDataFromOpeningTag(String string) {
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

    public boolean checkThatAngleBracketsBalanced(String string) {
        String[] stringArray = StringToArrayOfStringsRepresentingEachCharacter(string);
        String filter = "<>/";
        String[] filteredStringArray = filterStringArray(filter, stringArray);
        String filteredInput = StringTool.rejoinStringList(removeNulls(filteredStringArray), 0);
        return filteredInput.startsWith("<>") &&  //starts with <>
                filteredInput.endsWith("</>") &&   //ends with </>
                (checkThatAngleBracketsBalanced(filteredInput.substring(2, filteredInput.length() - 3)) ||
                        filteredInput.length() == 5); //<>Y</> Y is nothing or Y is balanced.
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
}
