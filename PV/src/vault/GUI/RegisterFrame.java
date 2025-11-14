package vault.GUI;

import vault.Algorithms.*;
import vault.Controller.*;
import vault.Database.*;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel strengthLabel;

    public RegisterFrame() {
        setTitle("🔐 Register - Password Vault");
        setSize(380, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        
  

        JPanel background = new JPanel();
        background.setBackground(new Color(30, 30, 46));
        background.setLayout(null);
        setContentPane(background);

        JLabel titleLabel = new JLabel("Create Account", SwingConstants.CENTER);
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
        
        strengthLabel = new JLabel(" ");
        strengthLabel.setForeground(Color.WHITE);
        strengthLabel.setBounds(140, 145, 180, 25); // position below password field
        background.add(strengthLabel);

        JButton checkStrengthBtn = createStyledButton("Check Strength", new Color(255, 153, 51));
        checkStrengthBtn.setBounds(50, 145, 100, 25); // next to password field
        background.add(checkStrengthBtn);

        checkStrengthBtn.addActionListener(e -> {
            String password = new String(passwordField.getPassword());
            int strength = DPPasswordStrength.calculate(password);
            String level = DPPasswordStrength.getStrengthLevel(strength);
            strengthLabel.setText("Strength: " + level);
        });

        
        JButton registerButton = createStyledButton("Register", new Color(102, 204, 102));
        registerButton.setBounds(70, 170, 100, 35);
        background.add(registerButton);

        JButton backButton = createStyledButton("Back", new Color(76, 139, 245));
        backButton.setBounds(200, 170, 100, 35);
        background.add(backButton);

        registerButton.addActionListener(e -> handleRegister());
        backButton.addActionListener(e -> backToLogin());

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

    private void handleRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int strength = DPPasswordStrength.calculate(password);
        if(strength < 50){ // your chosen threshold
            JOptionPane.showMessageDialog(this, "Password too weak!", "Weak Password", JOptionPane.WARNING_MESSAGE);
            return; // stop registration if password is weak
        }
        
        String encryptedPass = EncryptionUtil.encrypt(password);

        try (Connection conn = DatabaseManager.connect()) {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, encryptedPass);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registration successful!");
            new LoginFrame();
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backToLogin() {
        new LoginFrame();
        dispose();
    }


public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new RegisterFrame());
}
}
