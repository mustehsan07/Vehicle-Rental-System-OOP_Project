package customer;

import admin.AdminTheme;
import admin.RoundedButton;
import admin.RoundedPanel;
import auth.ProVehicleLogin;
import data.CustomerData;
import data.RentalData;
import data.RentalHistoryData;
import data.SampleDataLoader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.Customer;
import model.RentalHistory;
import rental_history.RentalHistoryPanel;
import vehicle_display.VehicleRentalModuleApp;

public class CustomerDashboard extends JFrame {

    private static Object activeCustomer;
    private final Customer customer;
    private final DefaultTableModel activeRentalTableModel;
    private final RentalHistoryPanel rentalHistoryPanel;
    private final JTable activeRentalTable;
    private final JLabel nameValue = new JLabel();
    private final JLabel emailValue = new JLabel();
    private final JLabel statusValue = new JLabel();
    private final JLabel totalRentalsValue = new JLabel();
    private final JLabel totalPaidValue = new JLabel();
    private final JLabel totalPayableValue = new JLabel();

    public CustomerDashboard() {
        this(resolveCurrentCustomer());
    }

    public CustomerDashboard(Customer customer) {
        this.customer = customer != null ? customer : resolveDefaultCustomer();
        activeRentalTableModel = new DefaultTableModel(new Object[]{"Rental ID", "Vehicle", "Rent Date", "Days", "Payable"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        rentalHistoryPanel = new RentalHistoryPanel();
        activeRentalTable = new JTable(activeRentalTableModel);

        setTitle("Customer Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1260, 780));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setContentPane(buildMainPanel());
        loadCustomerInfo();
        refreshRentalViews();
    }

    private JPanel buildMainPanel() {
        JPanel root = new JPanel(new BorderLayout(0, 18));
        root.setBackground(AdminTheme.BACKGROUND);
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildCenter(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);
        return root;
    }

    private JPanel buildHeader() {
        RoundedPanel header = new RoundedPanel(AdminTheme.CARD, AdminTheme.RADIUS_LARGE, AdminTheme.BORDER, 1);
        header.setLayout(new BorderLayout(16, 0));
        header.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JPanel accentStrip = new JPanel();
        accentStrip.setBackground(AdminTheme.ACCENT);
        accentStrip.setPreferredSize(new Dimension(6, 0));

        JPanel textWrap = new JPanel();
        textWrap.setOpaque(false);
        textWrap.setLayout(new BoxLayout(textWrap, BoxLayout.Y_AXIS));

        JLabel chip = new JLabel("Customer Dashboard");
        chip.setOpaque(true);
        chip.setBackground(AdminTheme.ACCENT_SOFT);
        chip.setForeground(AdminTheme.ACCENT);
        chip.setFont(AdminTheme.CHIP_FONT);
        chip.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        chip.setAlignmentX(LEFT_ALIGNMENT);

        JLabel title = new JLabel("Customer Experience Center");
        title.setFont(AdminTheme.TITLE_FONT);
        title.setForeground(AdminTheme.TEXT_PRIMARY);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("See your profile, returned vehicles, and rental actions in one premium dashboard");
        subtitle.setFont(AdminTheme.SUBTITLE_FONT);
        subtitle.setForeground(AdminTheme.TEXT_SECONDARY);
        subtitle.setAlignmentX(LEFT_ALIGNMENT);

        textWrap.add(chip);
        textWrap.add(javax.swing.Box.createVerticalStrut(10));
        textWrap.add(title);
        textWrap.add(javax.swing.Box.createVerticalStrut(6));
        textWrap.add(subtitle);

        header.add(accentStrip, BorderLayout.WEST);
        header.add(textWrap, BorderLayout.CENTER);
        header.add(buildLogoutButtonPanel(), BorderLayout.EAST);
        return header;
    }

    private JPanel buildLogoutButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panel.setOpaque(false);
        panel.add(buildLogoutButton());
        return panel;
    }

    private RoundedButton buildLogoutButton() {
        RoundedButton logoutButton = new RoundedButton(
                "Logout",
                AdminTheme.CARD,
                AdminTheme.ACCENT_SOFT,
                AdminTheme.ACCENT,
                AdminTheme.BORDER,
                AdminTheme.RADIUS_SMALL
        );
        logoutButton.setFont(AdminTheme.BUTTON_FONT);
        logoutButton.setPreferredSize(new Dimension(110, 30));
        logoutButton.setMinimumSize(new Dimension(110, 30));
        logoutButton.setMaximumSize(new Dimension(110, 30));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(2, 16, 2, 16));
        logoutButton.addActionListener(event -> {
            new ProVehicleLogin().setVisible(true);
            CustomerDashboard.this.dispose();
        });
        return logoutButton;
    }

    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(18, 18));
        center.setOpaque(false);
        center.add(buildLeftColumn(), BorderLayout.WEST);
        center.add(buildRightColumn(), BorderLayout.CENTER);
        return center;
    }

    private JPanel buildLeftColumn() {
        JPanel left = new JPanel(new BorderLayout(0, 18));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(360, 1));
        left.add(buildCustomerInfoCard(), BorderLayout.NORTH);
        left.add(buildActionCard(), BorderLayout.CENTER);
        return left;
    }

    private JPanel buildCustomerInfoCard() {
        RoundedPanel card = new RoundedPanel(AdminTheme.CARD, AdminTheme.RADIUS_LARGE, AdminTheme.BORDER, 1);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel heading = new JLabel("Customer Information");
        heading.setFont(AdminTheme.SUBTITLE_FONT);
        heading.setForeground(AdminTheme.TEXT_PRIMARY);
        card.add(heading, BorderLayout.NORTH);

        JPanel body = new JPanel(new GridLayout(4, 1, 0, 10));
        body.setOpaque(false);
        body.add(infoLine("Name", nameValue));
        body.add(infoLine("Email", emailValue));
        body.add(infoLine("Phone", statusValue));
        body.add(infoLine("Customer ID", createStaticValue(customer.getId())));
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildActionCard() {
        RoundedPanel card = new RoundedPanel(AdminTheme.CARD, AdminTheme.RADIUS_LARGE, AdminTheme.BORDER, 1);
        card.setLayout(new BorderLayout(0, 14));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel heading = new JLabel("Actions");
        heading.setFont(AdminTheme.SUBTITLE_FONT);
        heading.setForeground(AdminTheme.TEXT_PRIMARY);
        card.add(heading, BorderLayout.NORTH);

        JLabel helper = new JLabel("<html><body style='width:260px'>Rent a vehicle from the display screen or process a return using the rented vehicles table.</body></html>");
        helper.setFont(AdminTheme.BODY_FONT);
        helper.setForeground(AdminTheme.TEXT_SECONDARY);
        helper.setVerticalAlignment(SwingConstants.TOP);
        card.add(helper, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(2, 1, 0, 10));
        buttons.setOpaque(false);

        RoundedButton rentBtn = new RoundedButton(
                "Rent New Vehicle",
                AdminTheme.ACCENT,
                AdminTheme.ACCENT_HOVER,
                Color.WHITE,
                AdminTheme.ACCENT,
                AdminTheme.RADIUS_SMALL
        );
        rentBtn.setFont(AdminTheme.BUTTON_FONT);
        rentBtn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        rentBtn.addActionListener(e -> openVehicleDisplay());

        RoundedButton returnBtn = new RoundedButton(
                "Return Vehicle",
                AdminTheme.CARD,
                AdminTheme.ACCENT_SOFT,
                AdminTheme.TEXT_PRIMARY,
                AdminTheme.BORDER,
                AdminTheme.RADIUS_SMALL
        );
        returnBtn.setFont(AdminTheme.BUTTON_FONT);
        returnBtn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        returnBtn.addActionListener(e -> openReturnPopup());

        buttons.add(rentBtn);
        buttons.add(returnBtn);
        card.add(buttons, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildRightColumn() {
        JPanel right = new JPanel(new BorderLayout(0, 18));
        right.setOpaque(false);
        right.add(buildStatsRow(), BorderLayout.NORTH);
        JPanel recordsStack = new JPanel(new GridLayout(2, 1, 0, 18));
        recordsStack.setOpaque(false);
        recordsStack.add(buildActiveRentalCard());
        recordsStack.add(buildRentalTableCard());
        right.add(recordsStack, BorderLayout.CENTER);
        return right;
    }

    private JPanel buildStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 3, 14, 0));
        row.setOpaque(false);
        row.add(metricCard("Total Rentals", totalRentalsValue));
        row.add(metricCard("Total Paid", totalPaidValue));
        row.add(metricCard("Total Payable", totalPayableValue));
        return row;
    }

    private JPanel buildRentalTableCard() {
        RoundedPanel card = new RoundedPanel(AdminTheme.CARD, AdminTheme.RADIUS_LARGE, AdminTheme.BORDER, 1);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel heading = new JLabel("Rental History");
        heading.setFont(AdminTheme.SUBTITLE_FONT);
        heading.setForeground(AdminTheme.TEXT_PRIMARY);
        card.add(heading, BorderLayout.NORTH);

        // Delegated to RentalHistoryPanel (styled consistently)
        card.add(rentalHistoryPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildActiveRentalCard() {
        RoundedPanel card = new RoundedPanel(AdminTheme.CARD, AdminTheme.RADIUS_LARGE, AdminTheme.BORDER, 1);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel heading = new JLabel("Rented Vehicles");
        heading.setFont(AdminTheme.SUBTITLE_FONT);
        heading.setForeground(AdminTheme.TEXT_PRIMARY);
        card.add(heading, BorderLayout.NORTH);

        JTable table = activeRentalTable;
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setRowMargin(0);
        table.setFont(AdminTheme.BODY_FONT);
        table.getTableHeader().setBackground(AdminTheme.TABLE_HEADER_BACKGROUND);
        table.getTableHeader().setForeground(AdminTheme.TABLE_HEADER_FOREGROUND);
        table.getTableHeader().setFont(AdminTheme.TABLE_HEADER_FONT);
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AdminTheme.BORDER));
        table.setSelectionBackground(AdminTheme.ACCENT);
        table.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(AdminTheme.CARD);
        scrollPane.setOpaque(false);
        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildFooter() {
        RoundedPanel footer = new RoundedPanel(AdminTheme.CARD, AdminTheme.RADIUS_MEDIUM, AdminTheme.BORDER, 1);
        footer.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 12));
        footer.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));

        JLabel note = new JLabel("Need to rent or return a vehicle? Use the action buttons on the left.");
        note.setFont(AdminTheme.BODY_FONT);
        note.setForeground(AdminTheme.TEXT_SECONDARY);
        footer.add(note);
        return footer;
    }

    private JPanel infoLine(String label, JLabel valueLabel) {
        JPanel line = new JPanel(new BorderLayout(0, 2));
        line.setOpaque(false);

        JLabel labelView = new JLabel(label);
        labelView.setFont(AdminTheme.CHIP_FONT);
        labelView.setForeground(AdminTheme.TEXT_SECONDARY);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueLabel.setForeground(AdminTheme.TEXT_PRIMARY);

        line.add(labelView, BorderLayout.NORTH);
        line.add(valueLabel, BorderLayout.CENTER);
        return line;
    }

    private RoundedPanel metricCard(String label, JLabel value) {
        RoundedPanel card = new RoundedPanel(AdminTheme.CARD, AdminTheme.RADIUS_MEDIUM, AdminTheme.BORDER_SOFT, 1);
        card.setLayout(new BorderLayout(0, 4));
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel labelView = new JLabel(label);
        labelView.setFont(AdminTheme.CHIP_FONT);
        labelView.setForeground(AdminTheme.TEXT_SECONDARY);

        value.setFont(new Font("Segoe UI", Font.BOLD, 18));
        value.setForeground(AdminTheme.TEXT_PRIMARY);

        card.add(labelView, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        return card;
    }

    private JLabel createStaticValue(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(AdminTheme.TEXT_PRIMARY);
        return label;
    }

    private void loadCustomerInfo() {
        nameValue.setText(customer.getFullName());
        emailValue.setText(customer.getEmail());
        statusValue.setText(customer.getPhone());
    }

    private void refreshRentalViews() {
        refreshActiveRentalTable();
        refreshRentalTable();
    }

    private void refreshActiveRentalTable() {
        activeRentalTableModel.setRowCount(0);
        List<model.Rental> rentals = RentalData.getActiveRentalsForCustomer(customer);
        double payableTotal = 0;
        for (model.Rental rental : rentals) {
            activeRentalTableModel.addRow(new Object[]{
                    rental.getRentalId(),
                    rental.getVehicle().getBrand() + " " + rental.getVehicle().getModel(),
                    rental.getRentDate(),
                    rental.getDays(),
                    formatMoney(rental.getTotalCost())
            });
            payableTotal += rental.getTotalCost();
        }
        totalRentalsValue.setText(String.valueOf(rentals.size()));
        totalPayableValue.setText(formatMoney(payableTotal));
    }

    private void refreshRentalTable() {
        List<RentalHistory> rentals = getCustomerHistory();
        double totalPaid = 0;
        for (RentalHistory row : rentals) {
            totalPaid += row.getTotalCost();
        }
        totalPaidValue.setText(formatMoney(totalPaid));
        rentalHistoryPanel.refreshWithData(rentals);
    }

    private List<RentalHistory> getCustomerHistory() {
        return RentalHistoryData.getHistoryForCustomer(customer.getId());
    }

    private void openVehicleDisplay() {
        try {
            VehicleRentalModuleApp.setCurrentCustomer(customer);
            VehicleRentalModuleApp app = new VehicleRentalModuleApp();
            app.setVisible(true);

            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to open the vehicle display screen.", "Launch Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openReturnPopup() {
        model.Rental rental = getSelectedActiveRental();
        if (rental == null) {
            JOptionPane.showMessageDialog(this, "Please select a rented vehicle from the rented vehicles table.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ReturnPopupDialog dialog = new ReturnPopupDialog(this, rental);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private model.Rental getSelectedActiveRental() {
        int selectedRow = activeRentalTable.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }

        List<model.Rental> rentals = RentalData.getActiveRentalsForCustomer(customer);
        if (selectedRow >= rentals.size()) {
            return null;
        }
        return rentals.get(selectedRow);
    }

    private void completeReturn(model.Rental rental) {
        rental.setReturnDate(java.time.LocalDate.now().toString());
        rental.setStatus("Returned");
        rental.getVehicle().setAvailable(true);
        RentalHistoryData.addHistoryFromRental(rental);
        RentalData.removeRental(rental.getRentalId());

        try {
            Class<?> catalogClass = Class.forName("vehicle_display.VehicleCatalog");
            Object catalog = catalogClass.getConstructor().newInstance();
            catalogClass.getMethod("returnVehicle", String.class).invoke(catalog, rental.getVehicle().getId());
        } catch (ReflectiveOperationException ignored) {
        }

        refreshRentalViews();
    }

    private class ReturnPopupDialog extends JDialog {
        private final model.Rental rental;

        private ReturnPopupDialog(Window owner, model.Rental rental) {
            super(owner, "Return Vehicle", Dialog.ModalityType.APPLICATION_MODAL);
            this.rental = rental;
            buildUi();
        }

        private void buildUi() {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setResizable(false);
            setSize(new Dimension(520, 320));

            JPanel root = new JPanel(new BorderLayout(0, 14));
            root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
            root.setBackground(Color.WHITE);

            JLabel heading = new JLabel("Return Vehicle");
            heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
            heading.setForeground(AdminTheme.TEXT_PRIMARY);
            root.add(heading, BorderLayout.NORTH);

            JPanel body = new JPanel();
            body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
            body.setOpaque(false);
            body.add(popupLine("Vehicle", rental.getVehicle().getBrand() + " " + rental.getVehicle().getModel() + " (" + rental.getVehicle().getId() + ")"));
            body.add(popupLine("Customer", customer.getFullName()));
            body.add(popupLine("Total Payable", formatMoney(rental.getTotalCost())));
            root.add(body, BorderLayout.CENTER);

            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            actions.setOpaque(false);
            RoundedButton payReturnButton = new RoundedButton(
                    "Pay & Return",
                    AdminTheme.ACCENT,
                    AdminTheme.ACCENT_HOVER,
                    Color.WHITE,
                    AdminTheme.ACCENT,
                    AdminTheme.RADIUS_SMALL
            );
            payReturnButton.setFont(AdminTheme.BUTTON_FONT);
            payReturnButton.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
            payReturnButton.addActionListener(event -> openPaymentPopup());
            actions.add(payReturnButton);
            root.add(actions, BorderLayout.SOUTH);

            setContentPane(root);
        }

        private JPanel popupLine(String label, String value) {
            JPanel line = new JPanel(new BorderLayout(0, 2));
            line.setOpaque(false);
            line.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

            JLabel labelView = new JLabel(label);
            labelView.setFont(AdminTheme.CHIP_FONT);
            labelView.setForeground(AdminTheme.TEXT_SECONDARY);

            JLabel valueView = new JLabel(value);
            valueView.setFont(new Font("Segoe UI", Font.BOLD, 14));
            valueView.setForeground(AdminTheme.TEXT_PRIMARY);

            line.add(labelView, BorderLayout.NORTH);
            line.add(valueView, BorderLayout.CENTER);
            return line;
        }

        private void openPaymentPopup() {
            JDialog paymentDialog = new JDialog(this, "Pay Amount", Dialog.ModalityType.APPLICATION_MODAL);
            paymentDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            paymentDialog.setResizable(false);
            paymentDialog.setSize(new Dimension(420, 220));

            JPanel root = new JPanel(new BorderLayout(0, 14));
            root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
            root.setBackground(Color.WHITE);

            JLabel title = new JLabel("Enter Exact Payable Amount");
            title.setFont(new Font("Segoe UI", Font.BOLD, 18));
            title.setForeground(AdminTheme.TEXT_PRIMARY);
            root.add(title, BorderLayout.NORTH);

            JTextField amountField = new JTextField();
            amountField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            amountField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(203, 213, 225)),
                    new EmptyBorder(0, 10, 0, 10)
            ));

            JLabel note = new JLabel("Total payable: " + formatMoney(rental.getTotalCost()));
            note.setForeground(AdminTheme.TEXT_SECONDARY);
            note.setFont(AdminTheme.BODY_FONT);

            JPanel body = new JPanel(new BorderLayout(0, 8));
            body.setOpaque(false);
            body.add(note, BorderLayout.NORTH);
            body.add(amountField, BorderLayout.CENTER);
            root.add(body, BorderLayout.CENTER);

            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            actions.setOpaque(false);
            RoundedButton payButton = new RoundedButton(
                    "Pay",
                    AdminTheme.ACCENT,
                    AdminTheme.ACCENT_HOVER,
                    Color.WHITE,
                    AdminTheme.ACCENT,
                    AdminTheme.RADIUS_SMALL
            );
            payButton.setFont(AdminTheme.BUTTON_FONT);
            payButton.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
            payButton.addActionListener(event -> {
                double enteredAmount;
                try {
                    enteredAmount = Double.parseDouble(amountField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(paymentDialog, "Please input exact payable amount " + formatMoney(rental.getTotalCost()), "Payment Required", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                double payableAmount = rental.getTotalCost();
                if (Math.abs(enteredAmount - payableAmount) > 0.009) {
                    JOptionPane.showMessageDialog(paymentDialog, "Please input exact payable amount " + formatMoney(payableAmount), "Payment Required", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                completeReturn(rental);
                paymentDialog.dispose();
                ReturnPopupDialog.this.dispose();
                JOptionPane.showMessageDialog(CustomerDashboard.this, "Vehicle Returned, Thanks", "Return Completed", JOptionPane.INFORMATION_MESSAGE);
            });
            actions.add(payButton);
            root.add(actions, BorderLayout.SOUTH);

            paymentDialog.setContentPane(root);
            paymentDialog.setLocationRelativeTo(ReturnPopupDialog.this);
            paymentDialog.setVisible(true);
        }
    }

    private String formatMoney(double value) {
        return String.format("PKR %,.2f", value);
    }

    public static void setActiveCustomer(Object customer) {
        activeCustomer = customer;
    }

    private static Customer resolveCurrentCustomer() {
        if (activeCustomer instanceof Customer customer) {
            return customer;
        }
        return resolveDefaultCustomer();
    }

    private static Customer resolveDefaultCustomer() {
        SampleDataLoader.loadSampleData();
        return CustomerData.getCustomers().isEmpty()
                ? new Customer("C001", "Customer", "customer@example.com", "", "03000000000")
                : CustomerData.getCustomers().get(0);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ignored) {
        }

        SwingUtilities.invokeLater(() -> new CustomerDashboard().setVisible(true));
    }
}
