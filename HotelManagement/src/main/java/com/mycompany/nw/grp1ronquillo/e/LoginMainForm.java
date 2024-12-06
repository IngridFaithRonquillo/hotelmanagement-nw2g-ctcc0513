package com.mycompany.nw.grp1ronquillo.e;

import javax.swing.*;
import java.awt.*;

public class LoginMainForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton clearButton, loginButton, registerButton;

    public LoginMainForm() {
        setTitle("Azure Vista Hotel Login");
        setSize(900, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(230, 190, 138)); 

        add(panel); 

        placeComponents(panel); 

        // Clear button action: Clears input fields
        clearButton.addActionListener(e -> {
            usernameField.setText(""); 
            passwordField.setText("");
        });

        // ActionListener for Login button
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Validate username and password
            if (username.equals("ingrid") && password.equals("1234")) {
                // Open Reception Dashboard form
                new ReceptionDashboard().setVisible(true);
                dispose(); // Close login form
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password.");
            }
        });

        // ActionListener for Register button
        registerButton.addActionListener(e -> {
            new RegistrationForm().setVisible(true); 
            dispose(); 
        });
    }

    // Method to place components on the panel
    private void placeComponents(JPanel panel) {
        // Load and resize logo image
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/Logo.jpg"));
        Image image = originalIcon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(image);

        JLabel imageLabel = new JLabel(resizedIcon);
        imageLabel.setBounds(30, 50, 400, 300); 
        panel.add(imageLabel);

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to Azure Vista Hotel!");
        welcomeLabel.setBounds(530, 50, 400, 100); 
        welcomeLabel.setForeground(new Color(80, 45, 0));
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18)); 
        panel.add(welcomeLabel);

        // Username label and field
        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(450, 200, 80, 25); 
        userLabel.setForeground(new Color(80, 45, 0)); 
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(550, 200, 300, 25); 
        usernameField.setBackground(new Color(245, 220, 175)); 
        panel.add(usernameField);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(450, 250, 80, 25); 
        passwordLabel.setForeground(new Color(80, 45, 0)); 
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(550, 250, 300, 25); 
        passwordField.setBackground(new Color(245, 220, 175)); 
        panel.add(passwordField);

        // Clear button
        clearButton = new JButton("Clear");
        clearButton.setBounds(570, 300, 100, 25); 
        clearButton.setBackground(new Color(205, 133, 63)); 
        clearButton.setForeground(Color.WHITE); 
        panel.add(clearButton);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setBounds(730, 300, 100, 25); 
        loginButton.setBackground(new Color(205, 133, 63)); 
        loginButton.setForeground(Color.WHITE);
        panel.add(loginButton);
        
        // OR label
        JLabel orLabel = new JLabel("OR");
        orLabel.setBounds(685, 326, 50, 25); 
        orLabel.setFont(new Font("Arial", Font.ITALIC, 12)); 
        panel.add(orLabel);

        // Register button
        registerButton = new JButton("Register");
        registerButton.setBounds(570, 350, 260, 25); 
        registerButton.setBackground(new Color(205, 133, 63)); 
        registerButton.setForeground(Color.WHITE); 
        panel.add(registerButton); 
    }

    public static void main(String[] args) {
        // Launch the application
        SwingUtilities.invokeLater(() -> new LoginMainForm().setVisible(true));
    }
}