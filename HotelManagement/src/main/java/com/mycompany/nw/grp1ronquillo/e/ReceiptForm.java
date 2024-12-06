package com.mycompany.nw.grp1ronquillo.e;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReceiptForm extends JFrame {
    private JLabel nameLabel, roomTypeLabel, checkInLabel, checkOutLabel;
    
     // Constructor to initialize the UI with booking details
    public ReceiptForm(String lastName, String firstName, String roomType, String checkInDate, String checkOutDate, long totalPrice) {
        initUI(lastName, firstName, roomType, checkInDate, checkOutDate, totalPrice);
    }
    
    private void initUI(String lastName, String firstName, String roomType, String checkInDate, String checkOutDate, long totalPrice) {
        setTitle("Azure Vista: Receipt");
        setSize(530, 450); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        add(panel);

        // Header with hotel name and contact details
        JLabel headerLabel = new JLabel("Azure Vista: Hotel & Resort", SwingConstants.CENTER);
        headerLabel.setBounds(50, 10, 400, 25);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(headerLabel);

        // Contact details
        JLabel contactLabel = new JLabel("Email: azurevista@gmail.com | Contact: 09389498612", SwingConstants.CENTER);
        contactLabel.setBounds(50, 35, 400, 25);
        contactLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(contactLabel);
        // Address
        JLabel addressLabel = new JLabel("Address: Orani, Bataan, Philippines", SwingConstants.CENTER);
        addressLabel.setBounds(50, 55, 400, 25);
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(addressLabel);

        // Add customer and booking details
        addLabels(panel, lastName, firstName, roomType, checkInDate, checkOutDate);

        // Format total price and display it
        String formattedPrice = String.format("PHP %.2f", totalPrice / 1.0); 
        JLabel totalPriceLabel = new JLabel("Total Price: " + formattedPrice, SwingConstants.RIGHT);
        totalPriceLabel.setBounds(50, 200, 450, 25); // Increased the width of the label
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(totalPriceLabel);

        // Thank you message
        JTextArea messageArea = new JTextArea("Your booking is currently under review. You will receive an email notification once your room is officially confirmed. Thank you for choosing to book with us!");
        messageArea.setBounds(50, 250, 400, 60); 
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setOpaque(false);
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(messageArea);

        // Button to go back to login
        JButton backButton = new JButton("Back to Login");
        backButton.setBounds(180, 320, 150, 30); // Position the button at the bottom center
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();// Close the current window
                LoginMainForm loginForm = new LoginMainForm();
                loginForm.setVisible(true); // Open the login form
            }
        });
        panel.add(backButton);
    }

    // Adds labels for customer and booking details
    private void addLabels(JPanel panel, String lastName, String firstName, String roomType, String checkInDate, String checkOutDate) {
        nameLabel = new JLabel("Customer Name: " + firstName + " " + lastName);
        nameLabel.setBounds(50, 100, 300, 25);
        panel.add(nameLabel);

        roomTypeLabel = new JLabel("Room Type: " + roomType);
        roomTypeLabel.setBounds(50, 130, 300, 25);
        panel.add(roomTypeLabel);

        checkInLabel = new JLabel("Check-in Date: " + checkInDate);
        checkInLabel.setBounds(50, 160, 300, 25);
        panel.add(checkInLabel);

        checkOutLabel = new JLabel("Check-out Date: " + checkOutDate);
        checkOutLabel.setBounds(50, 190, 300, 25);
        panel.add(checkOutLabel);
    }
}
