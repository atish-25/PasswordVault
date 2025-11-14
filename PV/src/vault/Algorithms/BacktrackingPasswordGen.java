package vault.Algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BacktrackingPasswordGen {

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";

    public static List<String> generatePasswordsFromUsername(String username, int length, int count) {
        List<String> results = new ArrayList<>();
        Random random = new Random();
        for(int i=0;i<count;i++){
            StringBuilder sb = new StringBuilder();
            for(int j=0;j<length;j++){
                // 50% chance pick char from username, 50% random
                if(!username.isEmpty() && random.nextBoolean()){
                    sb.append(username.charAt(random.nextInt(username.length())));
                } else {
                    sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
                }
            }
            results.add(sb.toString());
        }
        return results;
    }
}
