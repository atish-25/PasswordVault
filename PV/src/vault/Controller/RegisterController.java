package vault.Controller;

import vault.Database.*;
import vault.Algorithms.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterController {

    public static boolean register(String username, String password) {
        try (Connection conn = DatabaseManager.connect()) {
            // Encrypt the password before storing
            String encryptedPass = EncryptionUtil.encrypt(password);

            String sql = "INSERT INTO users(username, password_hash) VALUES(?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, encryptedPass);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
