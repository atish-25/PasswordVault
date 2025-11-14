package vault.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/password_vault";
    private static final String USER = "root";       // your MySQL username
    private static final String PASS = "root"; // your MySQL password

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static void initializeDatabase() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {

            String sqlUsers = "CREATE TABLE IF NOT EXISTS users (" +
                    "username VARCHAR(50) PRIMARY KEY, " +
                    "password VARCHAR(255) NOT NULL)";
            stmt.execute(sqlUsers);

            String sqlVault = "CREATE TABLE IF NOT EXISTS vault (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50), " +
                    "platform VARCHAR(50), " +
                    "siteUser VARCHAR(50), " +
                    "sitePass VARCHAR(255))";
            stmt.execute(sqlVault);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
