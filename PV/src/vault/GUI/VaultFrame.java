package vault.GUI;

import vault.Algorithms.*;
import vault.Controller.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VaultFrame extends JFrame {

    private JTextField platformField, usernameField, searchField;
    private JPasswordField passwordField;
    private JTextArea displayArea;
    private String currentUser;
    private List<String[]> vaultEntries; // [platform, username, password]
    private JLabel vaultStrengthLabel;
    
    public VaultFrame(String user) {
        this.currentUser = user;
        VaultController.setCurrentUser(user);

        setTitle("🔐 Password Vault - " + user);
        setSize(550, 600);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel background = new JPanel();
        background.setBackground(new Color(30, 30, 46));
        background.setLayout(null);
        setContentPane(background);

        // Input fields
        JLabel platformLabel = new JLabel("Platform:");
        platformLabel.setForeground(Color.WHITE);
        platformLabel.setBounds(20, 20, 100, 25);

        platformField = new JTextField();
        platformField.setBounds(140, 20, 250, 25);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(20, 60, 100, 25);

        usernameField = new JTextField();
        usernameField.setBounds(140, 60, 250, 25);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(20, 100, 100, 25);

        passwordField = new JPasswordField();
        passwordField.setBounds(140, 100, 250, 25);
        background.add(passwordField);
        
        vaultStrengthLabel = new JLabel(" ");
        vaultStrengthLabel.setForeground(Color.WHITE);
        vaultStrengthLabel.setBounds(140, 130, 250, 25); // adjust position
        background.add(vaultStrengthLabel);

        JButton checkVaultStrengthBtn = createStyledButton("Check Strength", new Color(255, 153, 51));
        checkVaultStrengthBtn.setBounds(400, 100, 120, 25); // right side of password field
        background.add(checkVaultStrengthBtn);
        
        checkVaultStrengthBtn.addActionListener(e -> {
            String pass = new String(passwordField.getPassword());
            int strength = DPPasswordStrength.calculate(pass);
            String level = DPPasswordStrength.getStrengthLevel(strength);
            vaultStrengthLabel.setText("Strength: " + level);
        });
        
    

        
        // Buttons
        JButton saveBtn = createStyledButton("Save", new Color(76, 139, 245));
        saveBtn.setBounds(50, 150, 100, 30);

        JButton showBtn = createStyledButton("Show All", new Color(102, 204, 102));
        showBtn.setBounds(180, 150, 100, 30);

        JButton sortBtn = createStyledButton("Sort by Platform", new Color(255, 153, 51));
        sortBtn.setBounds(310, 150, 150, 30);

        JLabel searchLabel = new JLabel("Search Platform:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setBounds(20, 200, 120, 25);

        searchField = new JTextField();
        searchField.setBounds(140, 200, 250, 25);

        JButton searchBtn = createStyledButton("Search", new Color(204, 102, 255));
        searchBtn.setBounds(400, 200, 80, 25);

        // Generate password button now next to password field
        JButton generateBtn = createStyledButton("Generate Pass", new Color(102, 204, 102));
        generateBtn.setBounds(400, 100, 120, 25);

        JButton topBtn = createStyledButton("Top Platforms", new Color(255, 102, 102));
        topBtn.setBounds(50, 240, 150, 30);

        // Display area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBounds(20, 290, 500, 250);

        // Add components
        background.add(platformLabel); background.add(platformField);
        background.add(userLabel); background.add(usernameField);
        background.add(passLabel); background.add(passwordField);
        background.add(saveBtn); background.add(showBtn); background.add(sortBtn);
        background.add(searchLabel); background.add(searchField); background.add(searchBtn);
        background.add(generateBtn); background.add(topBtn);
        background.add(scrollPane);

        // Button Actions
        saveBtn.addActionListener(e -> savePassword());
        showBtn.addActionListener(e -> loadVault());
        sortBtn.addActionListener(e -> sortVault());
        searchBtn.addActionListener(e -> searchVault());
        generateBtn.addActionListener(e -> generatePasswords());
        topBtn.addActionListener(e -> showTopPlatforms());

        // Initialize vault entries
        vaultEntries = new ArrayList<>();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor){
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return button;
    }

    private void savePassword(){
        String platform = platformField.getText();
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());
        int strength = DPPasswordStrength.calculate(pass);

        if(platform.isEmpty() || user.isEmpty() || pass.isEmpty()){
            JOptionPane.showMessageDialog(this,"Please fill all fields!");
            return;
        }
        
        if(strength < 30){ // define threshold
            JOptionPane.showMessageDialog(this, "Password is too weak!", "Weak Password", JOptionPane.WARNING_MESSAGE);
            return; // prevent saving
        }

        boolean success = VaultController.savePassword(platform,user,pass);
        if(success){
            JOptionPane.showMessageDialog(this,"Saved!");
            platformField.setText(""); usernameField.setText(""); passwordField.setText("");
            loadVault();
        } else {
            JOptionPane.showMessageDialog(this,"Error saving password!");
        }
    }

    private void loadVault(){
        vaultEntries = VaultController.getVaultEntriesArray();
        displayArea.setText("");
        for(String[] entry: vaultEntries){
            displayArea.append("Platform: "+entry[0]+"\nUser: "+entry[1]+"\nPassword: "+entry[2]+"\n-----------------\n");
        }
    }

    private void sortVault(){
        quickSort.sortByPlatform(vaultEntries);
        loadVault();
        JOptionPane.showMessageDialog(this,"Vault sorted by platform!");
    }

    private void searchVault(){
        String target = searchField.getText();
        if(target.isEmpty()){
            JOptionPane.showMessageDialog(this,"Enter platform to search!");
            return;
        }
        quickSort.sortByPlatform(vaultEntries); // ensure sorted
        int idx = BinarySearch.searchPlatform(vaultEntries, target);
        if(idx == -1){
            JOptionPane.showMessageDialog(this,"Platform not found!");
        } else {
            String[] entry = vaultEntries.get(idx);
            JOptionPane.showMessageDialog(this,
                    "Found!\nPlatform: "+entry[0]+"\nUser: "+entry[1]+"\nPassword: "+entry[2]);
        }
    }

    private void generatePasswords(){
        String username = usernameField.getText();
        if(username.isEmpty()){
            JOptionPane.showMessageDialog(this,"Enter username to generate password!");
            return;
        }
        List<String> passwords = BacktrackingPasswordGen.generatePasswordsFromUsername(username, 8, 5);
        StringBuilder sb = new StringBuilder("Generated Passwords:\n");
        passwords.forEach(p -> sb.append(p).append("\n"));
        JOptionPane.showMessageDialog(this,sb.toString());
    }

    private void showTopPlatforms(){
        List<String> top = HeapTopFrequency.topPlatforms(vaultEntries,3);
        StringBuilder sb = new StringBuilder("Top Platforms:\n");
        top.forEach(s -> sb.append(s).append("\n"));
        JOptionPane.showMessageDialog(this,sb.toString());
    }

    // Main method for testing
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new VaultFrame("TestUser"));
    }
}
