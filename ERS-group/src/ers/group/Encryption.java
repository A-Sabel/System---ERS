package ers.group;

public class Encryption {
    private static final String KEY1 = "COMPUTER"; 
    private static final String KEY2 = "SCIENCE";

    public static String encrypt(String text) {
        if (text == null) return "";
        StringBuilder sb = new StringBuilder();
        int keyIndex = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // Keep newlines/carriage returns as-is (they're outside our encryption range)
            if (c == '\n' || c == '\r') {
                sb.append(c);
            } else {
                int shift = (KEY1.charAt(keyIndex % KEY1.length()) + KEY2.charAt(keyIndex % KEY2.length())) % 95;
                sb.append((char) (((c - 32 + shift) % 95) + 32));
                keyIndex++;
            }
        }
        return sb.toString();
    }

    public static String decrypt(String text) {
        if (text == null) return "";
        StringBuilder sb = new StringBuilder();
        int keyIndex = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // Keep newlines/carriage returns as-is
            if (c == '\n' || c == '\r') {
                sb.append(c);
            } else {
                int shift = (KEY1.charAt(keyIndex % KEY1.length()) + KEY2.charAt(keyIndex % KEY2.length())) % 95;
                sb.append((char) (((c - 32 - shift + 95) % 95) + 32));
                keyIndex++;
            }
        }
        return sb.toString();
    }
}