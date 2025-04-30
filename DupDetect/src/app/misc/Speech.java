package app.misc;

public class Speech {
    public static final char[] VOWELS = {
            'A', 'E', 'I', 'O', 'U', 'Y'
    };

    public static int countSyllables(String str) {
        String upper = str.toUpperCase();
        int count = 0;
        for (int i = 1; i < upper.length(); i++) {
            char prev = upper.charAt(i - 1);
            char curr = upper.charAt(i);
            if (Character.isLetter(prev) && Character.isLetter(curr)) {
                if (isVowel(prev) != isVowel(curr)) {
                    count++;
                }
            }
        }
        return count;
    }

    private static boolean isVowel(char c) {
        for (char v : VOWELS) {
            if (v == c) return true;
        }
        return false;
    }

    public static boolean looksLikeSpeech(String str) {
        String upper = str.toUpperCase();
        int letterCount = 0;
        for (char c : upper.toCharArray()) {
            if (Character.isLetter(c)) letterCount++;
        }

        if (letterCount == 0) return true;

        int syllables = countSyllables(str);
        double ratio = (double) syllables / letterCount;

        return ratio > 0.35;
    }
}
