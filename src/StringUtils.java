public class StringUtils {
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
}