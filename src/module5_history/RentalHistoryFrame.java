import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class RentalHistoryFrame extends JFrame {
    private final RentalHistoryRepository repository;
    private final RentalHistoryTableModel tableModel;
    private final JTable table;
    private final JTextField searchField;
    private final JComboBox<String> statusBox;
    private final JLabel totalLabel;
    private final JLabel activeLabel;
    private final JLabel returnedLabel;
    private final JLabel revenueLabel;

    public RentalHistoryFrame() {
        repository = new RentalHistoryRepository();
        tableModel = new RentalHistoryTableModel();
        table = new JTable(tableModel);
        searchField = new JTextField(22);
        statusBox = new JComboBox<String>(new String[]{"All", "Active", "Returned", "Overdue", "Cancelled"});
        totalLabel = new JLabel("0");
        activeLabel = new JLabel("0");
        returnedLabel = new JLabel("0");
        revenueLabel = new JLabel("Rs. 0");

        setTitle("Vehicle Rental System - Rental History Management");
        setSize(1100, 700);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildDesign();
        refreshTable();
    }

    private void buildDesign() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(244, 247, 252));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Rental History Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(25, 35, 55));

        JLabel subtitle = new JLabel("Manage rental records, returns, overdue rentals and total revenue.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 110, 130));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setOpaque(false);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 12, 12));
        cardsPanel.setOpaque(false);
        cardsPanel.add(createCard("Total Rentals", totalLabel, new Color(31, 82, 154)));
        cardsPanel.add(createCard("Active Rentals", activeLabel, new Color(14, 118, 130)));
        cardsPanel.add(createCard("Returned", returnedLabel, new Color(28, 128, 76)));
        cardsPanel.add(createCard("Revenue", revenueLabel, new Color(142, 88, 22)));

        centerPanel.add(cardsPanel, BorderLayout.NORTH);
        centerPanel.add(createTablePanel(), BorderLayout.CENTER);

        return centerPanel;
    }

    private JPanel createCard(String heading, JLabel valueLabel, Color valueColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 236)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel headingLabel = new JLabel(heading);
        headingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        headingLabel.setForeground(new Color(100, 110, 130));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(valueColor);

        card.add(headingLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 236)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton addButton = createButton("Add Record", new Color(28, 70, 140));
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRecordButtonAction();
            }
        });

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusBox);
        filterPanel.add(addButton);

        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        topBar.setOpaque(false);
        topBar.add(searchField, BorderLayout.CENTER);
        topBar.add(filterPanel, BorderLayout.EAST);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(36);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(236, 241, 249));

        JScrollPane scrollPane = new JScrollPane(table);

        JButton editButton = createButton("Edit", new Color(70, 88, 120));
        JButton returnedButton = createButton("Mark Returned", new Color(28, 128, 76));
        JButton deleteButton = createButton("Delete", new Color(180, 55, 55));

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editRecordButtonAction();
            }
        });

        returnedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                markReturnedButtonAction();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteRecordButtonAction();
            }
        });

        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomBar.setOpaque(false);
        bottomBar.add(editButton);
        bottomBar.add(returnedButton);
        bottomBar.add(deleteButton);

        panel.add(topBar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomBar, BorderLayout.SOUTH);

        addActions();

        return panel;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void addActions() {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                refreshTable();
            }

            public void removeUpdate(DocumentEvent e) {
                refreshTable();
            }

            public void changedUpdate(DocumentEvent e) {
                refreshTable();
            }
        });

        statusBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });
    }

    private void refreshTable() {
        String keyword = searchField.getText();
        String status = statusBox.getSelectedItem().toString();
        tableModel.setRecords(repository.searchRecords(keyword, status));
        updateDashboard();
    }

    private void updateDashboard() {
        int total = repository.getAllRecords().size();
        int active = 0;
        int returned = 0;
        double revenue = 0;

        for (RentalHistoryRecord record : repository.getAllRecords()) {
            if (record.getDisplayStatus().equals("Active") || record.getDisplayStatus().equals("Overdue")) {
                active++;
            }

            if (record.getDisplayStatus().equals("Returned")) {
                returned++;
            }

            revenue = revenue + record.getTotalCost();
        }

        totalLabel.setText(String.valueOf(total));
        activeLabel.setText(String.valueOf(active));
        returnedLabel.setText(String.valueOf(returned));
        revenueLabel.setText("Rs. " + String.format("%.0f", revenue));
    }

    private void addRecordButtonAction() {
        RentalHistoryRecord record = showRecordForm(null);
        if (record != null) {
            repository.addRecord(record);
            refreshTable();
        }
    }

    private void editRecordButtonAction() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record first.");
            return;
        }

        RentalHistoryRecord oldRecord = tableModel.getRecord(selectedRow);
        int actualIndex = findRecordIndex(oldRecord);
        RentalHistoryRecord newRecord = showRecordForm(oldRecord);

        if (newRecord != null && actualIndex >= 0) {
            repository.updateRecord(actualIndex, newRecord);
            refreshTable();
        }
    }

    private void markReturnedButtonAction() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record first.");
            return;
        }

        RentalHistoryRecord record = tableModel.getRecord(selectedRow);
        record.setReturnDate(LocalDate.now());
        record.setStatus(RentalStatus.RETURNED);
        refreshTable();
    }

    private void deleteRecordButtonAction() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record first.");
            return;
        }

        RentalHistoryRecord record = tableModel.getRecord(selectedRow);
        int actualIndex = findRecordIndex(record);

        int answer = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this record?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION && actualIndex >= 0) {
            repository.deleteRecord(actualIndex);
            refreshTable();
        }
    }

    private int findRecordIndex(RentalHistoryRecord selectedRecord) {
        for (int i = 0; i < repository.getAllRecords().size(); i++) {
            RentalHistoryRecord record = repository.getAllRecords().get(i);

            if (record.getRentalId().equals(selectedRecord.getRentalId())) {
                return i;
            }
        }

        return -1;
    }

    private RentalHistoryRecord showRecordForm(RentalHistoryRecord oldRecord) {
        JTextField rentalIdField = new JTextField();
        JTextField customerField = new JTextField();
        JTextField vehicleField = new JTextField();
        JTextField rentDateField = new JTextField();
        JTextField dueDateField = new JTextField();
        JTextField returnDateField = new JTextField();
        JTextField dailyRateField = new JTextField();
        JComboBox<RentalStatus> statusField = new JComboBox<RentalStatus>(RentalStatus.values());

        if (oldRecord == null) {
            rentalIdField.setText("R-" + System.currentTimeMillis() % 100000);
            rentDateField.setText(LocalDate.now().toString());
            dueDateField.setText(LocalDate.now().plusDays(3).toString());
            dailyRateField.setText("5000");
        } else {
            rentalIdField.setText(oldRecord.getRentalId());
            customerField.setText(oldRecord.getCustomerName());
            vehicleField.setText(oldRecord.getVehicleName());
            rentDateField.setText(oldRecord.getRentDate().toString());
            dueDateField.setText(oldRecord.getDueDate().toString());

            if (oldRecord.getReturnDate() != null) {
                returnDateField.setText(oldRecord.getReturnDate().toString());
            }

            dailyRateField.setText(String.valueOf(oldRecord.getDailyRate()));
            statusField.setSelectedItem(oldRecord.getStatus());
        }

        JPanel form = new JPanel(new GridLayout(8, 2, 8, 8));
        form.add(new JLabel("Rental ID"));
        form.add(rentalIdField);
        form.add(new JLabel("Customer Name"));
        form.add(customerField);
        form.add(new JLabel("Vehicle Name"));
        form.add(vehicleField);
        form.add(new JLabel("Rent Date yyyy-mm-dd"));
        form.add(rentDateField);
        form.add(new JLabel("Due Date yyyy-mm-dd"));
        form.add(dueDateField);
        form.add(new JLabel("Return Date blank if active"));
        form.add(returnDateField);
        form.add(new JLabel("Daily Rate"));
        form.add(dailyRateField);
        form.add(new JLabel("Status"));
        form.add(statusField);

        int result = JOptionPane.showConfirmDialog(this, form,
                "Rental History Record",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            LocalDate returnDate = null;

            if (!returnDateField.getText().trim().isEmpty()) {
                returnDate = LocalDate.parse(returnDateField.getText().trim());
            }

            return new RentalHistoryRecord(
                    rentalIdField.getText().trim(),
                    customerField.getText().trim(),
                    vehicleField.getText().trim(),
                    LocalDate.parse(rentDateField.getText().trim()),
                    LocalDate.parse(dueDateField.getText().trim()),
                    returnDate,
                    Double.parseDouble(dailyRateField.getText().trim()),
                    (RentalStatus) statusField.getSelectedItem()
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input. Date format must be yyyy-mm-dd and rate must be numeric.");
            return null;
        }
    }
}