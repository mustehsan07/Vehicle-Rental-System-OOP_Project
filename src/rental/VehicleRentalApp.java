package vehiclerental;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class VehicleRentalApp extends JFrame {
    private static final Color BACKGROUND = new Color(15, 23, 42);
    private static final Color SURFACE = new Color(30, 41, 59);
    private static final Color SURFACE_LIGHT = new Color(51, 65, 85);
    private static final Color PRIMARY = new Color(20, 184, 166);
    private static final Color PRIMARY_DARK = new Color(15, 118, 110);
    private static final Color TEXT = new Color(241, 245, 249);
    private static final Color MUTED = new Color(148, 163, 184);
    private static final Color WARNING = new Color(251, 191, 36);

    private final RentalService rentalService;
    private final DefaultTableModel vehicleTableModel;
    private final DefaultTableModel rentalTableModel;
    private final JTable vehicleTable;
    private final JTable rentalTable;
    private final JTextField customerNameField;
    private final JTextField customerPhoneField;
    private final JComboBox<String> vehicleComboBox;
    private final JSpinner rentDateSpinner;
    private final JSpinner expectedReturnDateSpinner;
    private final JSpinner actualReturnDateSpinner;
    private final JLabel selectedVehicleLabel;
    private final JLabel estimateLabel;
    private final JLabel statusLabel;
    private final DecimalFormat moneyFormat;

    public VehicleRentalApp() {
        rentalService = new RentalService();
        moneyFormat = new DecimalFormat("PKR #,##0.00");

        vehicleTableModel = new DefaultTableModel(new Object[]{"ID", "Vehicle", "Category", "Daily Rate", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        rentalTableModel = new DefaultTableModel(new Object[]{"Rental ID", "Customer", "Vehicle", "Rent Date", "Due Date", "Cost", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        vehicleTable = buildTable(vehicleTableModel);
        rentalTable = buildTable(rentalTableModel);
        customerNameField = buildTextField();
        customerPhoneField = buildTextField();
        vehicleComboBox = new JComboBox<>();
        styleComboBox(vehicleComboBox);
        rentDateSpinner = buildDateSpinner(new Date());
        expectedReturnDateSpinner = buildDateSpinner(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        actualReturnDateSpinner = buildDateSpinner(new Date());
        selectedVehicleLabel = new JLabel("Select a vehicle to begin");
        estimateLabel = new JLabel("Estimated Cost: PKR 0.00");
        statusLabel = new JLabel("Ready");

        configureWindow();
        setContentPane(buildMainPanel());
        loadVehicleComboBox();
        refreshTables();
        updateEstimate();
    }

    private void configureWindow() {
        setTitle("Vehicle Rental System - Rent & Return Module");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 720));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private JPanel buildMainPanel() {
        JPanel root = new JPanel(new BorderLayout(18, 18));
        root.setBackground(BACKGROUND);
        root.setBorder(new EmptyBorder(18, 18, 18, 18));
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildCenterPanel(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);
        return root;
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);

        JLabel title = new JLabel("Rent & Return Vehicles");
        title.setForeground(TEXT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel subtitle = new JLabel("Professional OOP module for managing active vehicle rentals and returns");
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel textPanel = new JPanel(new BorderLayout(0, 4));
        textPanel.setOpaque(false);
        textPanel.add(title, BorderLayout.NORTH);
        textPanel.add(subtitle, BorderLayout.SOUTH);

        JLabel badge = new JLabel("Java Swing  |  OOP Final Project", SwingConstants.CENTER);
        badge.setOpaque(true);
        badge.setBackground(SURFACE);
        badge.setForeground(PRIMARY);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 13));
        badge.setBorder(new EmptyBorder(10, 16, 10, 16));

        panel.add(textPanel, BorderLayout.WEST);
        panel.add(badge, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildCenterPanel() {
        JPanel center = new JPanel(new BorderLayout(18, 18));
        center.setOpaque(false);
        center.add(buildRentPanel(), BorderLayout.WEST);
        center.add(buildTablesPanel(), BorderLayout.CENTER);
        return center;
    }

    private JPanel buildRentPanel() {
        JPanel panel = buildCardPanel();
        panel.setPreferredSize(new Dimension(360, 620));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        addSectionTitle(panel, gbc, "New Rental");
        addLabel(panel, gbc, "Customer Name");
        panel.add(customerNameField, gbc);
        addLabel(panel, gbc, "Customer Phone");
        panel.add(customerPhoneField, gbc);
        addLabel(panel, gbc, "Available Vehicle");
        panel.add(vehicleComboBox, gbc);
        addLabel(panel, gbc, "Rent Date");
        panel.add(rentDateSpinner, gbc);
        addLabel(panel, gbc, "Expected Return Date");
        panel.add(expectedReturnDateSpinner, gbc);

        selectedVehicleLabel.setForeground(MUTED);
        selectedVehicleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(selectedVehicleLabel, gbc);

        estimateLabel.setForeground(PRIMARY);
        estimateLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        estimateLabel.setBorder(new EmptyBorder(10, 0, 12, 0));
        panel.add(estimateLabel, gbc);

        JButton rentButton = buildPrimaryButton("Rent Vehicle");
        rentButton.addActionListener(event -> rentSelectedVehicle());
        panel.add(rentButton, gbc);

        addDivider(panel, gbc);
        addSectionTitle(panel, gbc, "Return Vehicle");
        addLabel(panel, gbc, "Actual Return Date");
        panel.add(actualReturnDateSpinner, gbc);

        JButton returnButton = buildSecondaryButton("Return Selected Rental");
        returnButton.addActionListener(event -> returnSelectedRental());
        panel.add(returnButton, gbc);

        vehicleComboBox.addActionListener(event -> updateEstimate());
        rentDateSpinner.addChangeListener(event -> updateEstimate());
        expectedReturnDateSpinner.addChangeListener(event -> updateEstimate());

        gbc.weighty = 1;
        panel.add(new JLabel(), gbc);
        return panel;
    }

    private JPanel buildTablesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 18));
        panel.setOpaque(false);
        panel.add(buildVehiclePanel(), BorderLayout.NORTH);
        panel.add(buildRentalPanel(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildVehiclePanel() {
        JPanel panel = buildCardPanel();
        panel.setLayout(new BorderLayout(0, 12));
        panel.setPreferredSize(new Dimension(720, 265));
        panel.add(buildPanelTitle("Fleet Status"), BorderLayout.NORTH);
        panel.add(new JScrollPane(vehicleTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildRentalPanel() {
        JPanel panel = buildCardPanel();
        panel.setLayout(new BorderLayout(0, 12));
        panel.add(buildPanelTitle("Active & Completed Rentals"), BorderLayout.NORTH);
        panel.add(new JScrollPane(rentalTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(SURFACE);
        footer.setBorder(new EmptyBorder(12, 16, 12, 16));
        statusLabel.setForeground(MUTED);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        footer.add(statusLabel, BorderLayout.WEST);
        return footer;
    }

    private void rentSelectedVehicle() {
        try {
            String vehicleId = getSelectedVehicleId();
            RentalRecord record = rentalService.rentVehicle(
                    vehicleId,
                    customerNameField.getText(),
                    customerPhoneField.getText(),
                    getDate(rentDateSpinner),
                    getDate(expectedReturnDateSpinner)
            );

            customerNameField.setText("");
            customerPhoneField.setText("");
            refreshTables();
            loadVehicleComboBox();
            updateEstimate();
            statusLabel.setText("Rental created successfully: " + record.getRentalId());
            showReceipt("Rental Confirmed", record, false);
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    private void returnSelectedRental() {
        int selectedRow = rentalTable.getSelectedRow();
        if (selectedRow < 0) {
            showError("Please select an active rental from the rental table.");
            return;
        }

        String rentalId = rentalTable.getValueAt(selectedRow, 0).toString();
        try {
            RentalRecord record = rentalService.returnVehicle(rentalId, getDate(actualReturnDateSpinner));
            refreshTables();
            loadVehicleComboBox();
            updateEstimate();
            statusLabel.setText("Vehicle returned successfully: " + record.getVehicle().getDisplayName());
            showReceipt("Return Completed", record, true);
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    private void updateEstimate() {
        String vehicleId = getSelectedVehicleId();
        if (vehicleId == null) {
            selectedVehicleLabel.setText("No available vehicle selected");
            estimateLabel.setText("Estimated Cost: PKR 0.00");
            return;
        }

        Vehicle vehicle = rentalService.findVehicle(vehicleId).orElse(null);
        if (vehicle == null) {
            return;
        }

        LocalDate start = getDate(rentDateSpinner);
        LocalDate end = getDate(expectedReturnDateSpinner);
        long days = Math.max(1, ChronoUnit.DAYS.between(start, end));
        double cost = days * vehicle.getDailyRate();

        selectedVehicleLabel.setText(vehicle.getDisplayName() + " - " + vehicle.getCategory() + " - " + moneyFormat.format(vehicle.getDailyRate()) + "/day");
        estimateLabel.setText("Estimated Cost: " + moneyFormat.format(cost));
    }

    private void refreshTables() {
        vehicleTableModel.setRowCount(0);
        for (Vehicle vehicle : rentalService.getVehicles()) {
            vehicleTableModel.addRow(new Object[]{
                    vehicle.getVehicleId(),
                    vehicle.getBrand() + " " + vehicle.getModel(),
                    vehicle.getCategory(),
                    moneyFormat.format(vehicle.getDailyRate()),
                    vehicle.isAvailable() ? "Available" : "Rented"
            });
        }

        rentalTableModel.setRowCount(0);
        for (RentalRecord record : rentalService.getRentalRecords()) {
            rentalTableModel.addRow(new Object[]{
                    record.getRentalId(),
                    record.getCustomerName(),
                    record.getVehicle().getDisplayName(),
                    record.getRentDate(),
                    record.getExpectedReturnDate(),
                    moneyFormat.format(record.getFinalCost()),
                    record.getStatus()
            });
        }
    }

    private void loadVehicleComboBox() {
        vehicleComboBox.removeAllItems();
        for (Vehicle vehicle : rentalService.getVehicles()) {
            if (vehicle.isAvailable()) {
                vehicleComboBox.addItem(vehicle.getVehicleId() + " - " + vehicle.getBrand() + " " + vehicle.getModel());
            }
        }
    }

    private String getSelectedVehicleId() {
        Object selected = vehicleComboBox.getSelectedItem();
        if (selected == null) {
            return null;
        }
        String value = selected.toString();
        return value.substring(0, value.indexOf(" - "));
    }

    private LocalDate getDate(JSpinner spinner) {
        Date date = (Date) spinner.getValue();
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void showReceipt(String title, RentalRecord record, boolean returned) {
        String message = "Rental ID: " + record.getRentalId()
                + "\nCustomer: " + record.getCustomerName()
                + "\nPhone: " + record.getCustomerPhone()
                + "\nVehicle: " + record.getVehicle().getDisplayName()
                + "\nRent Date: " + record.getRentDate()
                + "\nExpected Return: " + record.getExpectedReturnDate()
                + (returned ? "\nActual Return: " + record.getActualReturnDate() : "")
                + "\nTotal Cost: " + moneyFormat.format(record.getFinalCost())
                + "\nStatus: " + record.getStatus();
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        statusLabel.setText("Action failed: " + message);
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private JTable buildTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(TEXT);
        table.setBackground(SURFACE);
        table.setGridColor(SURFACE_LIGHT);
        table.setSelectionBackground(PRIMARY_DARK);
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(SURFACE_LIGHT);
        table.getTableHeader().setForeground(TEXT);
        table.setDefaultRenderer(Object.class, new StatusCellRenderer());
        return table;
    }

    private JTextField buildTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(250, 38));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT);
        field.setBackground(SURFACE_LIGHT);
        field.setCaretColor(TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(71, 85, 105)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private JSpinner buildDateSpinner(Date date) {
        JSpinner spinner = new JSpinner(new SpinnerDateModel(date, null, null, java.util.Calendar.DAY_OF_MONTH));
        spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd"));
        spinner.setPreferredSize(new Dimension(250, 38));
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        Component editor = spinner.getEditor();
        editor.setBackground(SURFACE_LIGHT);
        return spinner;
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setPreferredSize(new Dimension(250, 38));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(SURFACE_LIGHT);
        comboBox.setForeground(TEXT);
    }

    private JButton buildPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        return styleButton(button);
    }

    private JButton buildSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(SURFACE_LIGHT);
        button.setForeground(TEXT);
        return styleButton(button);
    }

    private JButton styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(250, 42));
        return button;
    }

    private JPanel buildCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(SURFACE);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));
        return panel;
    }

    private JLabel buildPanelTitle(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        return label;
    }

    private void addSectionTitle(JPanel panel, GridBagConstraints gbc, String text) {
        JLabel label = buildPanelTitle(text);
        gbc.insets = new Insets(4, 0, 10, 0);
        panel.add(label, gbc);
        gbc.insets = new Insets(8, 0, 8, 0);
    }

    private void addLabel(JPanel panel, GridBagConstraints gbc, String text) {
        JLabel label = new JLabel(text);
        label.setForeground(MUTED);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(label, gbc);
    }

    private void addDivider(JPanel panel, GridBagConstraints gbc) {
        JPanel divider = new JPanel();
        divider.setPreferredSize(new Dimension(250, 1));
        divider.setBackground(SURFACE_LIGHT);
        gbc.insets = new Insets(18, 0, 18, 0);
        panel.add(divider, gbc);
        gbc.insets = new Insets(8, 0, 8, 0);
    }

    private class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            component.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            setBorder(new EmptyBorder(0, 8, 0, 8));
            if (!isSelected) {
                component.setBackground(SURFACE);
                component.setForeground(TEXT);
                if (value != null && ("Available".equals(value.toString()) || "Active".equals(value.toString()))) {
                    component.setForeground(PRIMARY);
                } else if (value != null && ("Rented".equals(value.toString()) || "Returned".equals(value.toString()))) {
                    component.setForeground(WARNING);
                }
            }
            return component;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            VehicleRentalApp app = new VehicleRentalApp();
            app.setVisible(true);
        });
    }
}
