package vault.Controller;

import vault.Algorithms.*;
import vault.Database.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VaultController {

    private static String currentUser;

    public static void setCurrentUser(String user){
        currentUser = user;
    }

    // Save password into DB with AES encryption
    public static boolean savePassword(String platform, String username, String password){
        try(Connection conn = DatabaseManager.connect()){
            String sql = "INSERT INTO vault(username, platform, siteUser, sitePass) VALUES(?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, currentUser);
            stmt.setString(2, platform);
            stmt.setString(3, username);
            stmt.setString(4, EncryptionUtil.encrypt(password));
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    // Get vault entries as String[] for algorithms
    public static List<String[]> getVaultEntriesArray(){
        List<String[]> entries = new ArrayList<>();
        try(Connection conn = DatabaseManager.connect()){
            String sql = "SELECT platform, siteUser, sitePass FROM vault WHERE username=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, currentUser);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String platform = rs.getString("platform");
                String user = rs.getString("siteUser");
                String pass = EncryptionUtil.decrypt(rs.getString("sitePass"));
                entries.add(new String[]{platform,user,pass});
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return entries;
    }

    // Optional: get entries as formatted String
    public static String getVaultEntriesString(){
        StringBuilder sb = new StringBuilder();
        List<String[]> entries = getVaultEntriesArray();
        for(String[] e: entries){
            sb.append("Platform: ").append(e[0]).append("\n")
              .append("User: ").append(e[1]).append("\n")
              .append("Password: ").append(e[2]).append("\n----------------\n");
        }
        return sb.toString();
    }

    // For testing purposes
    public static void main(String[] args){
        setCurrentUser("TestUser");
        savePassword("Gmail","test@gmail.com","Pass123!");
        savePassword("Facebook","fbuser","FbPass@1");
        List<String[]> entries = getVaultEntriesArray();
        entries.forEach(e -> System.out.println(e[0]+" | "+e[1]+" | "+e[2]));
    }
}
