package vault.Algorithms;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtil {

    private static final String KEY = "MySecretAESKey12"; // 16-char key

    public static String encrypt(String data){
        try{
            SecretKeySpec skey = new SecretKeySpec(KEY.getBytes(),"AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String encrypted){
        try{
            SecretKeySpec skey = new SecretKeySpec(KEY.getBytes(),"AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            byte[] decoded = Base64.getDecoder().decode(encrypted);
            return new String(cipher.doFinal(decoded));
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
