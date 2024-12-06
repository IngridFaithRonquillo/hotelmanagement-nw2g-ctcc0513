package com.mycompany.nw.grp1ronquillo.e;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.SwingUtilities;

public class RegistrationForm extends JFrame {
    private JTextField lastNameField, firstNameField, dobField, emailField, phoneNumberField, addressField;
    private JRadioButton maleRadioButton, femaleRadioButton;
    private JButton backButton, clearButton, registerButton;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/hoteldb";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    public RegistrationForm() {
        setTitle("Registration Form");
        setSize(900, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(230, 190, 138)); 

        add(panel);

        placeComponents(panel);
    }

    private void placeComponents(JPanel panel) {
        int centerX = 450; 

        // Labels and Text Fields
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setBounds(centerX - 200, 30, 100, 25); 
        panel.add(lastNameLabel);
        lastNameField = new JTextField(20);
        lastNameField.setBounds(centerX - 100, 30, 300, 25); 
        lastNameField.setBackground(new Color(245, 220, 175)); 
        panel.add(lastNameField);

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setBounds(centerX - 200, 70, 100, 25); 
        panel.add(firstNameLabel);
        firstNameField = new JTextField(20);
        firstNameField.setBounds(centerX - 100, 70, 300, 25); 
        firstNameField.setBackground(new Color(245, 220, 175)); 
        panel.add(firstNameField);

        JLabel sexLabel = new JLabel("Sex:");
        sexLabel.setBounds(centerX - 200, 110, 100, 25); 
        panel.add(sexLabel);
        maleRadioButton = new JRadioButton("Male");
        maleRadioButton.setBounds(centerX - 100, 110, 80, 25); 
        maleRadioButton.setBackground(new Color(230, 190, 138)); 
        panel.add(maleRadioButton);
        femaleRadioButton = new JRadioButton("Female");
        femaleRadioButton.setBounds(centerX - 20, 110, 100, 25); 
        femaleRadioButton.setBackground(new Color(230, 190, 138)); 
        panel.add(femaleRadioButton);
        ButtonGroup sexButtonGroup = new ButtonGroup();
        sexButtonGroup.add(maleRadioButton);
        sexButtonGroup.add(femaleRadioButton);

        JLabel dobLabel = new JLabel("Date of Birth:");
        dobLabel.setBounds(centerX - 200, 150, 200, 25); 
        panel.add(dobLabel);
        dobField = new JTextField("YYYY-MM-DD", 20);
        dobField.setForeground(Color.GRAY);
        // Focus listener for default text handling
        dobField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (dobField.getText().equals("YYYY-MM-DD")) {
                    dobField.setText("");
                    dobField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (dobField.getText().isEmpty()) {
                    dobField.setForeground(Color.GRAY);
                    dobField.setText("YYYY-MM-DD");
                }
            }
        });
        dobField.setBounds(centerX - 100, 150, 300, 25); 
        dobField.setBackground(new Color(245, 220, 175)); 
        panel.add(dobField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(centerX - 200, 190, 100, 25); 
        panel.add(emailLabel);
        emailField = new JTextField(20);
        emailField.setBounds(centerX - 100, 190, 300, 25); 
        emailField.setBackground(new Color(245, 220, 175)); 
        panel.add(emailField);

        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberLabel.setBounds(centerX - 200, 230, 100, 25); 
        panel.add(phoneNumberLabel);
        phoneNumberField = new JTextField(20);
        phoneNumberField.setBounds(centerX - 100, 230, 300, 25); 
        phoneNumberField.setBackground(new Color(245, 220, 175)); 
        panel.add(phoneNumberField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(centerX - 200, 270, 100, 25); 
        panel.add(addressLabel);
        addressField = new JTextField(20);
        addressField.setBounds(centerX - 100, 270, 300, 25); 
        addressField.setBackground(new Color(245, 220, 175)); 
        panel.add(addressField);

        // Buttons
        backButton = new JButton("Back");
        backButton.setBounds(centerX - 200, 320, 100, 25); 
        backButton.setBackground(new Color(205, 133, 63)); 
        backButton.setForeground(Color.WHITE); // White text
        panel.add(backButton);

        clearButton = new JButton("Clear");
        clearButton.setBounds(centerX - 50, 320, 100, 25); 
        clearButton.setBackground(new Color(205, 133, 63)); 
        clearButton.setForeground(Color.WHITE); 
        panel.add(clearButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(centerX + 100, 320, 100, 25); 
        registerButton.setBackground(new Color(205, 133, 63)); 
        registerButton.setForeground(Color.WHITE); 
        panel.add(registerButton);

        // Action Listeners
        backButton.addActionListener(e -> openLoginForm());
        clearButton.addActionListener(e -> clearFields());
        registerButton.addActionListener(e -> registerUser());

        clearFields(); // Clear fields initially
    }

    // Open login form
    private void openLoginForm() {
        LoginMainForm loginForm = new LoginMainForm();
        loginForm.setVisible(true);
        dispose(); // Close the registration form
    }

    // Clear all input fields
    private void clearFields() {
        lastNameField.setText("");
        firstNameField.setText("");
        maleRadioButton.setSelected(false);
        femaleRadioButton.setSelected(false);
        dobField.setText("YYYY-MM-DD");
        dobField.setForeground(Color.GRAY);
        emailField.setText("");
        phoneNumberField.setText("");
        addressField.setText("");
    }

    // Register user to the database
    private void registerUser() {
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        String sex = maleRadioButton.isSelected() ? "Male" : "Female";
        String dob = dobField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String address = addressField.getText();

        // Validate all fields are filled
        if (lastName.isEmpty() || firstName.isEmpty() || dob.isEmpty() || email.isEmpty() ||
                phoneNumber.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        // Attempt to register user in database
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "INSERT INTO users (lastName, firstName, sex, dob, email, phoneNumber, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, lastName);
            statement.setString(2, firstName);
            statement.setString(3, sex);
            statement.setString(4, dob);
            statement.setString(5, email);
            statement.setString(6, phoneNumber);
            statement.setString(7, address);

            int rowsInserted = statement.executeUpdate();
            connection.close();

            // Check if registration was successful
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                openRoomAvailabilityForm(); // Open room availability form
                dispose(); // Close registration form
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: Registration failed due to database connection issues.");
        }
    }

    // Open room availability form
    private void openRoomAvailabilityForm() {
        RoomAvailabilityForm roomAvailabilityForm = new RoomAvailabilityForm(
                lastNameField.getText(), firstNameField.getText(),
                maleRadioButton.isSelected() ? "Male" : "Female",
                dobField.getText(), emailField.getText(),
                phoneNumberField.getText(), addressField.getText()
        );
        roomAvailabilityForm.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrationForm().setVisible(true));
    }
}