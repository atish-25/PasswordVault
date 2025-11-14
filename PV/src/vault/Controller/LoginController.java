package vault.Controller;

import vault.Database.*;
import vault.Algorithms.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    public static boolean login(String username, String password) {
        try (Connection conn = DatabaseManager.connect()) {
            String sql = "SELECT password_hash FROM users WHERE username=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                // Decrypt stored password and compare
                String decryptedPass = EncryptionUtil.decrypt(storedHash);
                return password.equals(decryptedPass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
