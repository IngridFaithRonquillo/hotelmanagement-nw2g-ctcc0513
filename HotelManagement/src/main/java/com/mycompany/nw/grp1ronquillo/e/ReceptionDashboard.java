package com.mycompany.nw.grp1ronquillo.e;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;

public class ReceptionDashboard extends JFrame {
    // Database connection
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/hoteldb";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    private JTextField searchField;
    private JTable queueTable;
    private JTable bookedTable;
    private DefaultTableModel queueModel;
    private DefaultTableModel bookedModel;
    private Queue<Integer> customerQueue;

    // Labels for displaying customer details
    private JLabel selectedIdLabel;
    private JLabel selectedNameLabel;
    private JLabel selectedRoomLabel;
    private JLabel selectedCheckInLabel;
    private JLabel selectedCheckOutLabel;
    private JLabel selectedPriceLabel;

    public ReceptionDashboard() {
        setTitle("Reception Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // LinkedList data structure used to implement a Queue ADT (FIFO), but booking confirmation and deletion are manual, bypassing FIFO.
        customerQueue = new LinkedList<>();

        initializeQueueFromDatabase(); // Load customer queue from the database

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(80, 45, 0));
        JLabel titleLabel = new JLabel("Reception Dashboard");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel);

        // Search field in header
        // Implements a searching algorithm for customer data retrieval.
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchCustomer());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(new Color(80, 45, 0));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());

        // Customer Details Panel
        JPanel customerDetailsPanel = new JPanel(new GridLayout(3, 3, 15, 15));
        customerDetailsPanel.setBorder(BorderFactory.createTitledBorder("Selected Customer Details"));
        customerDetailsPanel.setBackground(Color.WHITE);
        customerDetailsPanel.add(new JLabel("ID:"));
        selectedIdLabel = new JLabel("-");
        customerDetailsPanel.add(selectedIdLabel);

        customerDetailsPanel.add(new JLabel("Customer Name:"));
        selectedNameLabel = new JLabel("-");
        customerDetailsPanel.add(selectedNameLabel);
        customerDetailsPanel.add(new JLabel("Room Type:"));
        selectedRoomLabel = new JLabel("-");
        customerDetailsPanel.add(selectedRoomLabel);
        customerDetailsPanel.add(new JLabel("Check-In:"));
        selectedCheckInLabel = new JLabel("-");
        customerDetailsPanel.add(selectedCheckInLabel);
        customerDetailsPanel.add(new JLabel("Total Price:"));
        selectedPriceLabel = new JLabel("-");
        customerDetailsPanel.add(selectedPriceLabel);
        customerDetailsPanel.add(new JLabel("Check-Out:"));
        selectedCheckOutLabel = new JLabel("-");
        customerDetailsPanel.add(selectedCheckOutLabel);

        centerPanel.add(customerDetailsPanel, BorderLayout.NORTH);

        // Tables Panel
        JPanel tablesPanel = new JPanel(new GridLayout(2, 1));

        // Queue Table
        queueModel = new DefaultTableModel(new String[]{"ID", "Customer Name", "Room Type", "Check-In", "Check-Out", "Total Price"}, 0);
        queueTable = new JTable(queueModel);
        queueTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        queueTable.getSelectionModel().addListSelectionListener(e -> displaySelectedCustomerDetails());
        JScrollPane queueScrollPane = new JScrollPane(queueTable);
        populateQueueTable();

        JPanel queuePanel = new JPanel(new BorderLayout());
        queuePanel.add(new JLabel("Customer Queue"), BorderLayout.NORTH);
        queuePanel.add(queueScrollPane, BorderLayout.CENTER);

        JButton confirmBookingButton = new JButton("Confirm Booking");
        confirmBookingButton.addActionListener(e -> confirmBooking());
        JButton deleteBookingButton = new JButton("Delete Booking");
        deleteBookingButton.addActionListener(e -> deleteBooking());

        JPanel queueButtonsPanel = new JPanel();
        queueButtonsPanel.add(confirmBookingButton);
        queueButtonsPanel.add(deleteBookingButton);
        queuePanel.add(queueButtonsPanel, BorderLayout.SOUTH);

        tablesPanel.add(queuePanel);

        // Booked Table
        bookedModel = new DefaultTableModel(new String[]{"ID", "Customer Name", "Room Type", "Check-In", "Check-Out", "Total Price"}, 0);
        bookedTable = new JTable(bookedModel);
        JScrollPane bookedScrollPane = new JScrollPane(bookedTable);
        populateBookedTable();

        JPanel bookedPanel = new JPanel(new BorderLayout());
        bookedPanel.add(new JLabel("Booked Customers"), BorderLayout.NORTH);
        bookedPanel.add(bookedScrollPane, BorderLayout.CENTER);
        tablesPanel.add(bookedPanel);
        centerPanel.add(tablesPanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Back to Login button (Back to LoginMainForm)
        JButton backButton = new JButton("Back to Login");
        backButton.addActionListener(e -> navigateToLoginForm());
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(backButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Searches for customers in the database and displays matching results.
    private void searchCustomer() {
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty()) {
            DefaultTableModel searchModel = new DefaultTableModel(new String[]{"ID", "Customer Name", "Room Type", "Check-In", "Check-Out", "Total Price"}, 0);
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT b.id, CONCAT(u.firstName, ' ', u.lastName) AS fullName, b.roomType, b.checkIn, b.checkOut, b.totalPrice " +
                                 "FROM bookings b " +
                                 "JOIN users u ON b.id = u.id " +
                                 "WHERE CONCAT(u.firstName, ' ', u.lastName) LIKE ?")) {
                stmt.setString(1, "%" + searchText + "%");
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    searchModel.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("fullName"),
                            rs.getString("roomType"),
                            rs.getDate("checkIn"),
                            rs.getDate("checkOut"),
                            rs.getDouble("totalPrice")
                    });
                }

                JTable searchTable = new JTable(searchModel);
                JOptionPane.showMessageDialog(this, new JScrollPane(searchTable), "Search Results", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error searching for customer: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a search term.");
        }
    }

    // Loads the queue from the database
    private void initializeQueueFromDatabase() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT id FROM bookings WHERE status = 'pending'";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                customerQueue.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading queue from database: " + e.getMessage());
        }
    }

    // Populates the queue table with pending bookings
    private void populateQueueTable() {
        queueModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT b.id, CONCAT(u.firstName, ' ', u.lastName) AS fullName, b.roomType, b.checkIn, b.checkOut, b.totalPrice " +
                             "FROM bookings b " +
                             "JOIN users u ON b.id = u.id " +
                             "WHERE b.status = 'pending'")) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                queueModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("fullName"),
                        rs.getString("roomType"),
                        rs.getDate("checkIn"),
                        rs.getDate("checkOut"),
                        rs.getDouble("totalPrice")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error populating queue table: " + e.getMessage());
        }
    }

    // Populates the booked table with confirmed bookings
    private void populateBookedTable() {
        bookedModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT b.id, CONCAT(u.firstName, ' ', u.lastName) AS fullName, b.roomType, b.checkIn, b.checkOut, b.totalPrice " +
                             "FROM bookings b " +
                             "JOIN users u ON b.id = u.id " +
                             "WHERE b.status = 'confirmed'")) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookedModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("fullName"),
                        rs.getString("roomType"),
                        rs.getDate("checkIn"),
                        rs.getDate("checkOut"),
                        rs.getDouble("totalPrice")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error populating booked table: " + e.getMessage());
        }
    }

    // Displays selected customer's details
    private void displaySelectedCustomerDetails() {
        int selectedRow = queueTable.getSelectedRow();
        if (selectedRow != -1) {
            int customerId = (int) queueModel.getValueAt(selectedRow, 0);
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT b.id, CONCAT(u.firstName, ' ', u.lastName) AS fullName, b.roomType, b.checkIn, b.checkOut, b.totalPrice " +
                                 "FROM bookings b " +
                                 "JOIN users u ON b.id = u.id " +
                                 "WHERE b.id = ?")) {
                stmt.setInt(1, customerId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    selectedIdLabel.setText(String.valueOf(rs.getInt("id")));
                    selectedNameLabel.setText(rs.getString("fullName"));
                    selectedRoomLabel.setText(rs.getString("roomType"));
                    selectedCheckInLabel.setText(rs.getDate("checkIn").toString());
                    selectedCheckOutLabel.setText(rs.getDate("checkOut").toString());
                    selectedPriceLabel.setText(String.valueOf(rs.getDouble("totalPrice")));
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error displaying selected customer details: " + e.getMessage());
            }
        }
    }

    // Confirms booking for selected customer
    private void confirmBooking() {
        int selectedRow = queueTable.getSelectedRow();
        if (selectedRow != -1) {
            int customerId = (int) queueModel.getValueAt(selectedRow, 0);
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE bookings SET status = 'confirmed' WHERE id = ?")) {
                stmt.setInt(1, customerId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Booking confirmed for customer ID: " + customerId);
                populateQueueTable();
                populateBookedTable();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error confirming booking: " + e.getMessage());
            }
        }
    }

    // Deletes booking for selected customer
    private void deleteBooking() {
        int selectedRow = queueTable.getSelectedRow();
        if (selectedRow != -1) {
            int customerId = (int) queueModel.getValueAt(selectedRow, 0);
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(
                         "DELETE FROM bookings WHERE id = ?")) {
                stmt.setInt(1, customerId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Booking deleted for customer ID: " + customerId);
                populateQueueTable();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting booking: " + e.getMessage());
            }
        }
    }
    // Opens the Login form and closes the current window.
    private void navigateToLoginForm() {
        new LoginMainForm().setVisible(true);
        dispose();
    }
    // Launches the Reception Dashboard.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReceptionDashboard().setVisible(true));
    }
}    