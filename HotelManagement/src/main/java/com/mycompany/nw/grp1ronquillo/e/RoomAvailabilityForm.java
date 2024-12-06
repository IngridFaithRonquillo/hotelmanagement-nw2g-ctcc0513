package com.mycompany.nw.grp1ronquillo.e;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class RoomAvailabilityForm extends JFrame {
    // Database connection
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/hoteldb";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";
    
    private JComboBox<String> roomTypeComboBox;
    private JTextArea availabilityTextArea, roomStatusTextArea;
    private JTextField checkInField, checkOutField;
    private JButton bookButton, clearButton, backButton;
    private String lastName, firstName, sex, dob, email, phoneNumber, address;

    private String[] roomTypes = {"Single", "Double", "Suite"};

    // Constructor initializes UI and sets room details
    public RoomAvailabilityForm(String lastName, String firstName, String sex, String dob, String email, String phoneNumber, String address) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.sex = sex;
        this.dob = dob;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        initUI(); // Initialize the UI components
    }

    private void initUI() {
        setTitle("Room Availability");
        setSize(600, 670);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(230, 190, 138));
        add(panel);

        addImage(panel); // Add image to the form
        addFormFields(panel); // Add form fields like check-in, check-out, room types
        addButtons(panel); // Add action buttons

        updateAvailability(); // Initially update availability based on the first room type
    }
    // Load image from resources and scale it
    private void addImage(JPanel panel) {
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/Rooms.jpg"));
        Image image = originalIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(resizedIcon);
        imageLabel.setBounds(150, 10, 300, 200);
        panel.add(imageLabel);
    }
    // Create and add form fields for room type, availability, dates, etc.
    private void addFormFields(JPanel panel) {
        JLabel roomLabel = createLabel("Room Type:", 50, 250, 100, 25);
        panel.add(roomLabel);

        roomTypeComboBox = new JComboBox<>(roomTypes);
        roomTypeComboBox.setBounds(160, 250, 150, 25);
        roomTypeComboBox.addActionListener(e -> updateAvailability()); // Update availability on room selection
        panel.add(roomTypeComboBox);

        JLabel availabilityLabel = createLabel("Room Offers:", 50, 290, 150, 25);
        panel.add(availabilityLabel);
        availabilityTextArea = createTextArea(50, 310, 500, 100);
        panel.add(availabilityTextArea);

        JLabel roomStatusLabel = createLabel("Room Status:", 50, 420, 150, 25);
        panel.add(roomStatusLabel);
        roomStatusTextArea = createTextArea(50, 440, 500, 25);
        panel.add(roomStatusTextArea);

        JLabel checkInLabel = createLabel("Check-in Date (YYYY-MM-DD):", 50, 480, 200, 25);
        panel.add(checkInLabel);
        checkInField = createTextField(260, 480, 150, 25);
        panel.add(checkInField);

        JLabel checkOutLabel = createLabel("Check-out Date (YYYY-MM-DD):", 50, 520, 200, 25);
        panel.add(checkOutLabel);
        checkOutField = createTextField(260, 520, 150, 25);
        panel.add(checkOutField);
    }
    // Create a label with specified text and position
    private JLabel createLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        return label;
    }
    // Create a text area for displaying room availability and status
    private JTextArea createTextArea(int x, int y, int width, int height) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false); // Make the text area non-editable
        textArea.setBackground(Color.WHITE);
        textArea.setBounds(x, y, width, height);
        return textArea;
    }
    // Create a text field with specified dimensions
    private JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBackground(new Color(245, 220, 175));
        textField.setBounds(x, y, width, height);
        return textField;
    }
    // Add buttons (Back, Clear, Book) to the panel
    private void addButtons(JPanel panel) {
        backButton = createButton("Back");
        backButton.setBounds(100, 560, 100, 25);
        panel.add(backButton);

        clearButton = createButton("Clear");
        clearButton.setBounds(220, 560, 100, 25);
        panel.add(clearButton);

        bookButton = createButton("Book");
        bookButton.setBounds(340, 560, 100, 25);
        bookButton.addActionListener(e -> handleBooking()); // Handle booking action
        panel.add(bookButton);
    }
    // Create a button with specified text
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(205, 133, 63));
        button.setForeground(Color.WHITE);
        return button;
    }
    // Update room availability information based on selected room type
    private void updateAvailability() {
        String roomType = (String) roomTypeComboBox.getSelectedItem();
        availabilityTextArea.setText(getAvailabilityInfo(roomType));
        roomStatusTextArea.setText(getRoomStatus(roomType));
    }
    // Get room availability information based on room type
    private String getAvailabilityInfo(String roomType) {
        switch (roomType) {
            case "Single":
                return "Single Room:\n- 1 Bed\n- AC: Yes\n- Heater: No\n- Price: PHP 1500 per night";
            case "Double":
                return "Double Room:\n- 2 Beds\n- AC: Yes\n- Heater: Yes\n- Price: PHP 2500 per night";
            case "Suite":
                return "Suite:\n- 1 King Bed\n- Living Room\n- AC: Yes\n- Heater: Yes\n- Price: PHP 4000 per night";
            default:
                return "No information available for this room type.";
        }
    }
    // Get the room status based on selected room type
    private String getRoomStatus(String roomType) {
        return roomType.equals("Double") ? "Not Available" : "Available";
    }
    // Handle booking process
    private void handleBooking() {
        String roomType = (String) roomTypeComboBox.getSelectedItem();
        String checkInDate = checkInField.getText();
        String checkOutDate = checkOutField.getText();

        LocalDate checkIn;
        LocalDate checkOut;
        try {
            checkIn = LocalDate.parse(checkInDate);
            checkOut = LocalDate.parse(checkOutDate);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please enter date in YYYY-MM-DD format.");
            return;
        }

        if (getRoomStatus(roomType).equals("Not Available")) {
            JOptionPane.showMessageDialog(this, "Selected room type is not available for booking.");
            return;
        }

        long totalPrice = calculateTotalPrice(roomType, checkIn, checkOut);

        if (insertBooking(roomType, checkInDate, checkOutDate, totalPrice)) {
        ReceiptForm receiptForm = new ReceiptForm(lastName, firstName, roomType, checkInDate, checkOutDate, totalPrice);
            receiptForm.setVisible(true);
            dispose(); // Close the current RoomAvailabilityForm
        } else {
            JOptionPane.showMessageDialog(this, "Booking failed.");
        }
    }
    // Calculate total price based on room type and number of nights
    private long calculateTotalPrice(String roomType, LocalDate checkIn, LocalDate checkOut) {
        int nights = (int) (checkOut.toEpochDay() - checkIn.toEpochDay());
        long pricePerNight;
        switch (roomType) {
            case "Single":
                pricePerNight = 1500;
                break;
            case "Double":
                pricePerNight = 2500;
                break;
            case "Suite":
                pricePerNight = 4000;
                break;
            default:
                pricePerNight = 0;
        }
        return nights * pricePerNight;
    }
    // Insert booking data into the database
    private boolean insertBooking(String roomType, String checkInDate, String checkOutDate, long totalPrice) {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            String sql = "INSERT INTO bookings (roomType, checkIn, checkOut, totalPrice) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, roomType);
            statement.setString(2, checkInDate);
            statement.setString(3, checkOutDate);
            statement.setLong(4, totalPrice);

            int rowsInserted = statement.executeUpdate();
            connection.close();
            return rowsInserted > 0; // Return true if booking was inserted successfully
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an error occurred
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
             new RoomAvailabilityForm("", "", "", "", "", "", "").setVisible(true);
        });
    }
}