package vault.GUI;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import vault.Database.*;
import vault.Algorithms.*;
import vault.Controller.*;


public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Password Vault - Login");
        setSize(380, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        JPanel background = new JPanel();
        background.setBackground(new Color(30, 30, 46));
        background.setLayout(null);
        setContentPane(background);

        JLabel titleLabel = new JLabel("Password Vault", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(90, 20, 200, 40);
        background.add(titleLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(50, 80, 80, 25);
        background.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(140, 80, 180, 25);
        usernameField.setBackground(new Color(240, 240, 240));
        background.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(50, 115, 80, 25);
        background.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(140, 115, 180, 25);
        passwordField.setBackground(new Color(240, 240, 240));
        background.add(passwordField);

        JButton loginButton = createStyledButton("Login", new Color(76, 139, 245));
        loginButton.setBounds(70, 170, 100, 35);
        background.add(loginButton);

        JButton registerButton = createStyledButton("Register", new Color(102, 204, 102));
        registerButton.setBounds(200, 170, 100, 35);
        background.add(registerButton);

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> new RegisterFrame());

        DatabaseManager.initializeDatabase(); // ensure DB ready

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DatabaseManager.connect()) {
            String sql = "SELECT password FROM users WHERE username=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String decrypted = EncryptionUtil.decrypt(rs.getString("password"));
                if (password.equals(decrypted)) {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    new VaultFrame(username);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Wrong password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new LoginFrame());

}}
