package customer;

import admin.AdminTheme;
import admin.RoundedButton;
import admin.RoundedPanel;
import app.MainClass;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class CustomerDashboard extends JFrame {
    private final MainClass.UserRow customer;
    private final DefaultTableModel rentalTableModel;
    private final JLabel nameValue = new JLabel();
    private final JLabel emailValue = new JLabel();
    private final JLabel statusValue = new JLabel();
    private final JLabel rentalsValue = new JLabel();
    private final JLabel totalBillValue = new JLabel();

    public CustomerDashboard() {
        this(resolveDefaultCustomer());
    }

    public CustomerDashboard(MainClass.UserRow customer) {
        this.customer = customer != null ? customer : resolveDefaultCustomer();
        rentalTableModel = new DefaultTableModel(new Object[]{"Rental ID", "Vehicle", "Date", "Duration", "Bill"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        setTitle("Customer Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1260, 780));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setContentPane(buildMainPanel());
        loadCustomerInfo();
        refreshRentalTable();
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

        JLabel subtitle = new JLabel("See your profile, rented vehicles, and rental actions in one premium dashboard");
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
        header.add(buildHeaderMeta(), BorderLayout.EAST);
        return header;
    }

    private JPanel buildHeaderMeta() {
        JPanel wrap = new JPanel(new GridLayout(2, 1, 0, 10));
        wrap.setOpaque(false);
        wrap.add(summaryBadge("Customer ID", customer.id));
        wrap.add(summaryBadge("Current Status", customer.status));
        return wrap;
    }

    private RoundedPanel summaryBadge(String label, String value) {
        RoundedPanel card = new RoundedPanel(AdminTheme.CARD_ALT, AdminTheme.RADIUS_MEDIUM, AdminTheme.BORDER_SOFT, 1);
        card.setLayout(new BorderLayout(0, 2));
        card.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JLabel labelView = new JLabel(label);
        labelView.setFont(AdminTheme.CHIP_FONT);
        labelView.setForeground(AdminTheme.TEXT_SECONDARY);

        JLabel valueView = new JLabel(value != null ? value : "-");
        valueView.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueView.setForeground(AdminTheme.TEXT_PRIMARY);

        card.add(labelView, BorderLayout.NORTH);
        card.add(valueView, BorderLayout.CENTER);
        return card;
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
        body.add(infoLine("Role", createStaticValue("Customer")));
        body.add(infoLine("Membership", statusValue));
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

        JLabel helper = new JLabel("Start a new rental or process a return using the rental module.");
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
        rentBtn.addActionListener(e -> openRentalModule("Rent New Vehicle"));

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
        returnBtn.addActionListener(e -> openRentalModule("Return Vehicle"));

        buttons.add(rentBtn);
        buttons.add(returnBtn);
        card.add(buttons, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildRightColumn() {
        JPanel right = new JPanel(new BorderLayout(0, 18));
        right.setOpaque(false);
        right.add(buildStatsRow(), BorderLayout.NORTH);
        right.add(buildRentalTableCard(), BorderLayout.CENTER);
        return right;
    }

    private JPanel buildStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 3, 14, 0));
        row.setOpaque(false);
        row.add(metricCard("Rented Vehicles", rentalsValue));
        row.add(metricCard("Total Bill", totalBillValue));
        row.add(metricCard("Profile", createStaticValue(customer.fullName)));
        return row;
    }

    private JPanel buildRentalTableCard() {
        RoundedPanel card = new RoundedPanel(AdminTheme.CARD, AdminTheme.RADIUS_LARGE, AdminTheme.BORDER, 1);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel heading = new JLabel("Rented Vehicles");
        heading.setFont(AdminTheme.SUBTITLE_FONT);
        heading.setForeground(AdminTheme.TEXT_PRIMARY);
        card.add(heading, BorderLayout.NORTH);

        JTable table = new JTable(rentalTableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    component.setBackground(row % 2 == 0 ? AdminTheme.CARD : AdminTheme.CARD_ALT);
                }
                return component;
            }
        };
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
        DefaultTableCellRenderer centered = new DefaultTableCellRenderer();
        centered.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centered);
        table.getColumnModel().getColumn(2).setCellRenderer(centered);
        table.getColumnModel().getColumn(3).setCellRenderer(centered);
        table.getColumnModel().getColumn(4).setCellRenderer(centered);

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
        nameValue.setText(customer.fullName);
        emailValue.setText(customer.email);
        statusValue.setText(customer.status);
    }

    private void refreshRentalTable() {
        rentalTableModel.setRowCount(0);
        List<MainClass.RentalHistoryRow> rentals = getCustomerRentals();
        double totalBill = 0;
        for (MainClass.RentalHistoryRow row : rentals) {
            rentalTableModel.addRow(new Object[]{row.id, row.vehicleName, row.date, row.duration, formatMoney(row.bill)});
            totalBill += row.bill;
        }
        rentalsValue.setText(String.valueOf(rentals.size()));
        totalBillValue.setText(formatMoney(totalBill));
    }

    private List<MainClass.RentalHistoryRow> getCustomerRentals() {
        List<MainClass.RentalHistoryRow> rentals = new ArrayList<>();
        for (MainClass.RentalHistoryRow row : MainClass.getRentalHistory()) {
            if (customer.fullName.equalsIgnoreCase(row.customerName) || customer.email.equalsIgnoreCase(row.customerName)) {
                rentals.add(row);
            }
        }
        return rentals;
    }

    private void openRentalModule(String actionName) {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Open the Rent & Return module to " + actionName.toLowerCase() + "?",
                actionName,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            try {
                Class<?> rentalAppClass = Class.forName("vehiclerental.VehicleRentalApp");
                JFrame rentalFrame = (JFrame) rentalAppClass.getConstructor().newInstance();
                rentalFrame.setVisible(true);
                dispose();
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Unable to open the rental module.", "Launch Error", JOptionPane.ERROR_MESSAGE);
            } catch (ReflectiveOperationException ex) {
                JOptionPane.showMessageDialog(this, "Unable to open the rental module.", "Launch Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String formatMoney(double value) {
        return String.format("PKR %,.2f", value);
    }

    private static MainClass.UserRow resolveDefaultCustomer() {
        for (MainClass.UserRow row : MainClass.getUsers()) {
            if ("customer".equalsIgnoreCase(row.role)) {
                return row;
            }
        }
        return MainClass.getUsers().isEmpty()
                ? new MainClass.UserRow("C001", "Customer", "customer@example.com", "", "Active", "customer")
                : MainClass.getUsers().get(0);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ignored) {
        }

        SwingUtilities.invokeLater(() -> new CustomerDashboard().setVisible(true));
    }
}
