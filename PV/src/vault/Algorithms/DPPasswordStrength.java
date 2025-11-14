package vault.Algorithms;

public class DPPasswordStrength {

    // Returns a strength score from 0 to 100
    public static int calculate(String password) {
        if(password == null || password.isEmpty()) return 0;

        int n = password.length();
        int score = 0;

        boolean hasLower = false, hasUpper = false, hasDigit = false, hasSpecial = false;

        // DP array to track repeated substrings length
        int[] repeat = new int[n];

        for(int i = 0; i < n; i++) {
            char c = password.charAt(i);
            if(Character.isLowerCase(c)) hasLower = true;
            else if(Character.isUpperCase(c)) hasUpper = true;
            else if(Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;

            // Check for consecutive repeated characters
            if(i > 0 && c == password.charAt(i-1)) repeat[i] = repeat[i-1] + 1;
        }

        // Base score on character types
        int typeCount = 0;
        if(hasLower) typeCount++;
        if(hasUpper) typeCount++;
        if(hasDigit) typeCount++;
        if(hasSpecial) typeCount++;
        score += typeCount * 20; // up to 80 points

        // Deduct points for repeated sequences
        int repeatPenalty = 0;
        for(int r : repeat) repeatPenalty += r * 5;
        score -= repeatPenalty;

        // Deduct points for short passwords
        if(n < 8) score -= 10;
        if(n < 6) score -= 10;

        // Ensure score is between 0 and 100
        if(score > 100) score = 100;
        if(score < 0) score = 0;

        return score;
    }

    // Helper method to get textual strength
    public static String getStrengthLevel(int score) {
        if(score < 30) return "Weak";
        else if(score < 60) return "Medium";
        else if(score < 80) return "Strong";
        else return "Very Strong";
    }
}
