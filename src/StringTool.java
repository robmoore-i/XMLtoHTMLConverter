public class StringTool {
//A one class library of useful functions for this program.

    public static String rejoinStringArray(String[] array, int fromWhichEntry) {
        String rejoinedString = "";
        for (int i = fromWhichEntry; i < array.length; i++) {
            rejoinedString = rejoinedString.concat(array[i]);
        }
        return rejoinedString;
    }
}