public class StringTool {
//A one class library of useful functions for this program.

    public static String rejoinStringList(String[] s, int fromWhichEntry) {
        String s1 = "";
        for (int i = fromWhichEntry; i < s.length; i++) {
            s1 = s1.concat(s[i]);
        }
        return s1;
    }
}