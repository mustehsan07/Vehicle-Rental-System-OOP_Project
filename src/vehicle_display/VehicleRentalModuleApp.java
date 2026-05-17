import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.text.NumberFormat;

public class VehicleRentalModuleApp extends JFrame {
    private static final Color BACKGROUND = new Color(16, 24, 39);
    private static final Color SURFACE = new Color(24, 35, 56);
    private static final Color SURFACE_LIGHT = new Color(33, 48, 74);
    private static final Color TEXT = new Color(232, 238, 247);
    private static final Color MUTED_TEXT = new Color(148, 163, 184);
    private static final Color ACCENT = new Color(20, 184, 166);
    private static final Color ACCENT_DARK = new Color(15, 118, 110);
    private static final String SEARCH_PLACEHOLDER = "Search by ID, name, or brand";
    private static final String DAYS_PLACEHOLDER = "Example: 3";

    private final VehicleCatalog catalog = new VehicleCatalog();
    private final RentalCostCalculator calculator = new RentalCostCalculator();
    private final VehicleTableModel tableModel = new VehicleTableModel();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(java.util.Locale.forLanguageTag("en-PK"));

    private JTable vehicleTable;
    private JTextField searchField;
    private JComboBox<String> typeComboBox;
    private JTextField daysField;
    private JLabel selectedVehicleLabel;
    private JLabel subtotalLabel;
    private JLabel discountLabel;
    private JLabel serviceChargeLabel;
    private JLabel grandTotalLabel;

    public VehicleRentalModuleApp() {
        super("Vehicle Rental System - Available Vehicles");
        configureWindow();
        buildLayout();
        applyFilters();
    }

    private void configureWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 720);
        setMinimumSize(new Dimension(980, 620));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND);
    }

    private void buildLayout() {
        JPanel root = new JPanel(new BorderLayout(18, 18));
        root.setBackground(BACKGROUND);
        root.setBorder(new EmptyBorder(22, 24, 22, 24));

        root.add(createHeaderPanel(), BorderLayout.NORTH);
        root.add(createTablePanel(), BorderLayout.CENTER);
        root.add(createCalculationPanel(), BorderLayout.EAST);

        setContentPane(root);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout(16, 12));
        header.setOpaque(false);

        JPanel titlePanel = new JPanel(new BorderLayout(0, 4));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Available Vehicles");
        title.setForeground(TEXT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));

        JLabel subtitle = new JLabel("Search vehicles, select a type, and calculate rental cost instantly.");
        subtitle.setForeground(MUTED_TEXT);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        JPanel filters = new JPanel(new GridBagLayout());
        filters.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 8, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        searchField = createInputField(SEARCH_PLACEHOLDER);
        searchField.addActionListener(event -> applyFilters());

        typeComboBox = new JComboBox<>(new String[]{"All", "Car", "SUV", "Van", "Bike", "Luxury"});
        styleComboBox(typeComboBox);
        typeComboBox.addActionListener(event -> applyFilters());

        JButton searchButton = createPrimaryButton("Search");
        searchButton.addActionListener(event -> applyFilters());

        gbc.gridx = 0;
        filters.add(searchField, gbc);
        gbc.gridx = 1;
        filters.add(typeComboBox, gbc);
        gbc.gridx = 2;
        filters.add(searchButton, gbc);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(filters, BorderLayout.EAST);

        return header;
    }

    private JPanel createTablePanel() {
        JPanel panel = createSurfacePanel(new BorderLayout(0, 0));

        vehicleTable = new JTable(tableModel);
        vehicleTable.setRowHeight(44);
        vehicleTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        vehicleTable.setForeground(TEXT);
        vehicleTable.setBackground(SURFACE);
        vehicleTable.setGridColor(new Color(51, 65, 85));
        vehicleTable.setShowVerticalLines(false);
        vehicleTable.setRowSelectionAllowed(false);
        vehicleTable.setCellSelectionEnabled(false);
        vehicleTable.getModel().addTableModelListener(event -> updateSelectedVehicleLabel());
        vehicleTable.getColumnModel().getColumn(0).setMaxWidth(76);
        vehicleTable.getColumnModel().getColumn(0).setMinWidth(68);

        JTableHeader header = vehicleTable.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 42));
        header.setDefaultRenderer(new HeaderRenderer());

        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(SURFACE);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCalculationPanel() {
        JPanel panel = createSurfacePanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(340, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel heading = new JLabel("Rental Cost");
        heading.setForeground(TEXT);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        gbc.gridy = 0;
        gbc.insets = new Insets(6, 0, 4, 0);
        panel.add(heading, gbc);

        selectedVehicleLabel = new JLabel("No vehicle selected");
        selectedVehicleLabel.setForeground(MUTED_TEXT);
        selectedVehicleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 24, 0);
        panel.add(selectedVehicleLabel, gbc);

        JLabel daysLabel = createFieldLabel("Rental Days");
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 8, 0);
        panel.add(daysLabel, gbc);

        daysField = createInputField(DAYS_PLACEHOLDER);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 18, 0);
        panel.add(daysField, gbc);

        JButton calculateButton = createPrimaryButton("Calculate Total");
        calculateButton.addActionListener(event -> calculateCost());
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 24, 0);
        panel.add(calculateButton, gbc);

        subtotalLabel = createAmountRow(panel, gbc, 5, "Subtotal", "PKR 0.00");
        discountLabel = createAmountRow(panel, gbc, 6, "Long Rental Discount", "PKR 0.00");
        serviceChargeLabel = createAmountRow(panel, gbc, 7, "Service Charge", "PKR 0.00");

        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(new Color(8, 145, 128));
        totalPanel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel totalText = new JLabel("Grand Total");
        totalText.setForeground(Color.WHITE);
        totalText.setFont(new Font("Segoe UI", Font.BOLD, 14));

        grandTotalLabel = new JLabel("PKR 0.00", SwingConstants.RIGHT);
        grandTotalLabel.setForeground(Color.WHITE);
        grandTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));

        totalPanel.add(totalText, BorderLayout.NORTH);
        totalPanel.add(grandTotalLabel, BorderLayout.SOUTH);

        gbc.gridy = 8;
        gbc.insets = new Insets(12, 0, 0, 0);
        panel.add(totalPanel, gbc);

        gbc.gridy = 9;
        gbc.weighty = 1;
        panel.add(new JLabel(), gbc);

        return panel;
    }

    private JLabel createAmountRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setOpaque(false);
        rowPanel.setBorder(new EmptyBorder(8, 0, 8, 0));

        JLabel nameLabel = new JLabel(label);
        nameLabel.setForeground(MUTED_TEXT);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel amountLabel = new JLabel(value, SwingConstants.RIGHT);
        amountLabel.setForeground(TEXT);
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        rowPanel.add(nameLabel, BorderLayout.WEST);
        rowPanel.add(amountLabel, BorderLayout.EAST);

        gbc.gridy = row;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(rowPanel, gbc);

        return amountLabel;
    }

    private void applyFilters() {
        tableModel.setVehicles(catalog.searchAvailableVehicles(
                getFieldText(searchField, SEARCH_PLACEHOLDER),
                typeComboBox == null ? "All" : String.valueOf(typeComboBox.getSelectedItem())
        ));
    }

    private void calculateCost() {
        try {
            Vehicle selectedVehicle = tableModel.getSelectedVehicle();
            int rentalDays = Integer.parseInt(getFieldText(daysField, DAYS_PLACEHOLDER));
            RentalCost cost = calculator.calculate(selectedVehicle, rentalDays);

            subtotalLabel.setText(currencyFormat.format(cost.getSubtotal()));
            discountLabel.setText("- " + currencyFormat.format(cost.getDiscount()));
            serviceChargeLabel.setText(currencyFormat.format(cost.getServiceCharge()));
            grandTotalLabel.setText(currencyFormat.format(cost.getGrandTotal()));
        } catch (NumberFormatException exception) {
            showError("Please enter rental days as a valid number.");
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void updateSelectedVehicleLabel() {
        Vehicle selectedVehicle = tableModel.getSelectedVehicle();

        if (selectedVehicle == null) {
            selectedVehicleLabel.setText("No vehicle selected");
            return;
        }

        selectedVehicleLabel.setText(selectedVehicle.getVehicleId() + " - " + selectedVehicle.getName());
    }

    private String getFieldText(JTextField field, String placeholder) {
        if (field == null || field.getText().equals(placeholder)) {
            return "";
        }

        return field.getText().trim();
    }

    private JPanel createSurfacePanel(java.awt.LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(SURFACE);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));
        return panel;
    }

    private JTextField createInputField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setPreferredSize(new Dimension(230, 42));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT);
        field.setBackground(SURFACE_LIGHT);
        field.setCaretColor(TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85)),
                new EmptyBorder(0, 12, 0, 12)
        ));

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent event) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent event) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                }
            }
        });

        return field;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return label;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D graphics2D = (Graphics2D) graphics.create();
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setColor(getBackground());
                graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                graphics2D.dispose();
                super.paintComponent(graphics);
            }
        };

        button.setPreferredSize(new Dimension(150, 42));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(ACCENT);
        button.setOpaque(true);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent event) {
                button.setBackground(ACCENT_DARK);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent event) {
                button.setBackground(ACCENT);
            }
        });

        return button;
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setPreferredSize(new Dimension(150, 42));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setForeground(TEXT);
        comboBox.setBackground(SURFACE_LIGHT);
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85)));
        comboBox.setFocusable(false);
        comboBox.setRenderer(new DarkComboBoxRenderer());

        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("v");
                button.setForeground(TEXT);
                button.setBackground(SURFACE_LIGHT);
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setFocusPainted(false);
                return button;
            }
        });
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Required", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Default Swing look and feel will be used.
            }

            new VehicleRentalModuleApp().setVisible(true);
        });
    }

    private static class HeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public java.awt.Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            label.setOpaque(true);
            label.setBackground(SURFACE_LIGHT);
            label.setForeground(TEXT);
            label.setFont(new Font("Segoe UI", Font.BOLD, 13));
            label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(51, 65, 85)),
                    new EmptyBorder(0, 10, 0, 10)
            ));

            return label;
        }
    }

    private static class DarkComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public java.awt.Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);

            label.setOpaque(true);
            label.setText(String.valueOf(value));
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            label.setForeground(TEXT);
            label.setBackground(isSelected ? ACCENT_DARK : SURFACE_LIGHT);
            label.setBorder(new EmptyBorder(10, 12, 10, 12));

            return label;
        }
    }
}
