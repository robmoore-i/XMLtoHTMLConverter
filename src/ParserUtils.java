public abstract class ParserUtils {
//A one class library of useful functions for this program.

    public static String rejoinStringArray(String[] array, int fromWhichEntry) {
        String rejoinedString = "";
        for (int i = fromWhichEntry; i < array.length; i++) {
            rejoinedString = rejoinedString.concat(array[i]);
        }
        return rejoinedString;
    }

    public static String[] removeNulls(String[] array) {
        int newSize = findSizeOfNewArray(array);
        return populateNewArray(array, newSize);
    }

    private static int findSizeOfNewArray(String[] array) {
        int newSize = 0;
        for (String anArray : array) {
            if (anArray != null) {
                newSize++;
            }
        }
        return newSize;
    }

    private static String[] populateNewArray(String[] array, int newSize) {
        String[] tempArray = new String[newSize];
        System.arraycopy(array, 0, tempArray, 0, newSize);
        return tempArray;
    }

    public static void printArray(String[] array) {
        for (String string : array) {
            System.out.println(string);
        }
    }

    public static int positionOfNextAFromBInC(String A, int B, String C) {
        for (int i = B; i < C.length(); i++) {
            if (String.valueOf(C.charAt(i)).equals(A)) {
                return i;
            }
        }
        return 0;
    }

    public static int getPositionOfFirstClosingAngleBracket(String inputString) {
        int positionOfFirstClosingAngleBracket = 0;
        String[] stringArray = getCharacterMap(inputString);
        for (int i = 0; i < stringArray.length; i++) {
            String string = stringArray[i];
            if (string.equals(">")) {
                positionOfFirstClosingAngleBracket = i + 1;
                break;
            }
        }
        return positionOfFirstClosingAngleBracket;
    }

    public static String[] getCharacterMap(String string) {
        char[] charArrayOfString = string.toCharArray();
        String[] stringArrayOfString = new String[charArrayOfString.length];
        for (int i = 0; i < charArrayOfString.length; i++) {
            stringArrayOfString[i] = String.valueOf(charArrayOfString[i]);
        }
        return stringArrayOfString;
    }

    public static String[] filterStringArray(String filter, String[] array) {
        String[] result = new String[array.length];
        for (int i = 0, j = 0; i < array.length; i++) {
            if (filter.contains(array[i])) {
                result[j] = array[i];
                j++;
            }
        }
        return result;
    }

    public static String removeAngleBrackets(String string) {
        String removeAngleBrackets = string;
        removeAngleBrackets = removeAngleBrackets.replace("<", "");
        removeAngleBrackets = removeAngleBrackets.replace(">", "");
        return removeAngleBrackets;
    }

    public static String[] changeInputToNameValueFormat(String string, String tagClass) {
        String manipulatedString = ParserUtils.rejoinStringArray(string.split("\""), 0);
        if (tagClass.equals("page")) {
            manipulatedString = manipulatedString.replace(tagClass + " ", "");  //remove the tagClass
            manipulatedString = manipulatedString.replace(" ", "-");            //replace the space between the characters in the attribute with a hyphen
            manipulatedString = tagClass + " " + manipulatedString;             //put the tagClass back
        }
        return manipulatedString.split(" ");                                //profit everywhere.
    }

    public static XMLAttribute[] getXMLAttributes(String[] tagAttributeAssignments) {
        XMLAttribute[] xmlAttribute = new XMLAttribute[tagAttributeAssignments.length];
        for (int i = 1, j = 0; i < tagAttributeAssignments.length; i++) {
            xmlAttribute[j] = new XMLAttribute(tagAttributeAssignments[i].split("=")[0], tagAttributeAssignments[i].split("=")[1]);
            j++;
        }
        return xmlAttribute;
    }

    public static String cleanDescription(String descriptionText) {
        String editedDescription = descriptionText;
        editedDescription = editedDescription.replace("<![CDATA[", "");
        editedDescription = editedDescription.replace("]]>", "");
        editedDescription = editedDescription.replace("\\verbatim", "");
        editedDescription = editedDescription.replace("\\endverbatim", "");
        return editedDescription;
    }
}