package ers.group;

public class Encryption {
    // Your chosen keys
    private static final String KEY1 = "COMPUTER"; 
    private static final String KEY2 = "SCIENCE";

    public static String encrypt(String text) {
        if (text == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            // Keep the CSV format readable (commas and newlines)
            if (c == ',' || c == '\n' || c == '\r') {
                sb.append(c);
            } else {
                // Shift by Key 1 ('C', then 'O', then 'M'...)
                int shift1 = KEY1.charAt(i % KEY1.length());
                // Shift by Key 2 ('S', then 'C', then 'I'...)
                int shift2 = KEY2.charAt(i % KEY2.length());
                
                sb.append((char) (c + shift1 + shift2));
            }
        }
        return sb.toString();
    }

    public static String decrypt(String text) {
        if (text == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == ',' || c == '\n' || c == '\r') {
                sb.append(c);
            } else {
                int shift1 = KEY1.charAt(i % KEY1.length());
                int shift2 = KEY2.charAt(i % KEY2.length());
                
                // Subtract both keys to go backwards
                sb.append((char) (c - shift1 - shift2));
            }
        }
        return sb.toString();
    }
}