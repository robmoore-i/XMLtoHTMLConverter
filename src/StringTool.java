public class StringTool {
    public static String rejoinStringList(String[] s, int fromWhichEntry) {
        String s1 = "";
        for (int i = fromWhichEntry; i < s.length; i++) {
            s1 = s1.concat(s[i]);
        }
        return s1;
    }

    public static void printArray(Object[] array) {
        for (Object o : array) {
            System.out.print(o);
        }
    }
}
