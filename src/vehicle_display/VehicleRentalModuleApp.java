package vehicle_display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class VehicleRentalModuleApp extends JFrame {
    private static final Color BACKGROUND = new Color(233, 238, 243);
    private static final Color CARD = Color.WHITE;
    private static final Color CARD_ALT = new Color(248, 250, 251);
    private static final Color SURFACE_LIGHT = new Color(30, 41, 59);
    private static final Color TEXT = new Color(15, 23, 42);
    private static final Color MUTED_TEXT = new Color(100, 116, 139);
    private static final Color ACCENT = new Color(15, 118, 110);
    private static final Color ACCENT_DARK = new Color(17, 94, 89);
    private static final Color ACCENT_SOFT = new Color(217, 240, 238);
    private static final String SEARCH_PLACEHOLDER = "Search by ID, name, or brand";

    private static Object currentCustomer;

    private final VehicleCatalog catalog = new VehicleCatalog();
    private final VehicleTableModel tableModel = new VehicleTableModel();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-PK"));
    private final Object customer;

    private JTable vehicleTable;
    private JTextField searchField;
    private JComboBox<String> typeComboBox;
    private JButton rentButton;
    private JLabel selectedVehicleLabel;
    private JLabel selectedVehicleMetaLabel;
    private JLabel brandValueLabel;
    private JLabel typeValueLabel;
    private JLabel seatsValueLabel;
    private JLabel transmissionValueLabel;
    private JLabel rateValueLabel;
    private JLabel statusLabel;
    private int selectedRow = -1;

    public VehicleRentalModuleApp() {
        super("Vehicle Rental System - Available Vehicles");
        this.customer = currentCustomer;
        configureWindow();
        setContentPane(buildRoot());
        applyFilters();
        updateSelectionDetails();
    }

    public static void setCurrentCustomer(Object customer) {
        currentCustomer = customer;
    }

    private void configureWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1260, 780));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private JPanel buildRoot() {
        JPanel root = new JPanel(new BorderLayout(18, 18)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, BACKGROUND, getWidth(), getHeight(), CARD_ALT));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setBorder(new EmptyBorder(22, 24, 22, 24));
        root.setOpaque(false);
        root.add(createHeaderPanel(), BorderLayout.NORTH);
        root.add(createContentPanel(), BorderLayout.CENTER);
        root.add(createFooterPanel(), BorderLayout.SOUTH);
        return root;
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout(18, 14));
        header.setOpaque(false);

        JPanel titlePanel = new JPanel(new BorderLayout(0, 8));
        titlePanel.setOpaque(false);

        JLabel chip = new JLabel("Vehicle Rental Module");
        chip.setOpaque(true);
        chip.setBackground(ACCENT_SOFT);
        chip.setForeground(ACCENT);
        chip.setFont(new Font("Segoe UI", Font.BOLD, 12));
        chip.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

        JPanel chipWrap = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        chipWrap.setOpaque(false);
        chipWrap.add(chip);

        JLabel title = new JLabel("Available Vehicles");
        title.setForeground(TEXT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));

        JLabel subtitle = new JLabel("Pick a vehicle from the list, review the rental total, and confirm the booking in one screen.");
        subtitle.setForeground(MUTED_TEXT);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        titlePanel.add(chipWrap, BorderLayout.NORTH);
        titlePanel.add(title, BorderLayout.CENTER);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        JPanel filters = new JPanel(new GridBagLayout());
        filters.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 8, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        searchField = createPlaceholderField(SEARCH_PLACEHOLDER);
        searchField.addActionListener(event -> applyFilters());

        typeComboBox = new RoundedComboBox<>(new String[]{"All", "Car", "SUV", "Van", "Bike", "Luxury"});
        styleComboBox(typeComboBox);
        typeComboBox.addActionListener(event -> applyFilters());

        JButton searchButton = createPrimaryButton("Search");
        searchButton.addActionListener(event -> applyFilters());

        JButton resetButton = createSecondaryButton("Reset");
        resetButton.addActionListener(event -> resetFilters());

        JButton backButton = createSecondaryButton("Back");
        backButton.addActionListener(event -> openCustomerDashboard());

        gbc.gridx = 0;
        filters.add(searchField, gbc);
        gbc.gridx = 1;
        filters.add(typeComboBox, gbc);
        gbc.gridx = 2;
        filters.add(backButton, gbc);
        gbc.gridx = 3;
        filters.add(searchButton, gbc);
        gbc.gridx = 4;
        filters.add(resetButton, gbc);

        header.add(titlePanel, BorderLayout.CENTER);
        header.add(filters, BorderLayout.EAST);
        return header;
    }

    private JPanel createContentPanel() {
        JPanel center = new JPanel(new BorderLayout(18, 18));
        center.setOpaque(false);
        center.add(createTableCard(), BorderLayout.CENTER);
        center.add(createBookingCard(), BorderLayout.EAST);
        return center;
    }

    private JPanel createTableCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 12));

        JLabel heading = createCardHeading("Available Fleet");
        JLabel helper = createMutedLabel("Double-click a row to select a vehicle. Rented vehicles disappear from this list.");

        JPanel top = new JPanel(new BorderLayout(0, 4));
        top.setOpaque(false);
        top.add(heading, BorderLayout.NORTH);
        top.add(helper, BorderLayout.SOUTH);

        vehicleTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (selectedRow == row) {
                    component.setBackground(ACCENT_SOFT);
                } else {
                    component.setBackground(row % 2 == 0 ? CARD : CARD_ALT);
                }
                component.setForeground(TEXT);
                if (component instanceof javax.swing.JComponent jComponent) {
                    jComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 228, 235)));
                }
                return component;
            }
        };
        vehicleTable.setRowHeight(40);
        vehicleTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        vehicleTable.setForeground(TEXT);
        vehicleTable.setBackground(CARD);
        vehicleTable.setShowVerticalLines(false);
        vehicleTable.setShowHorizontalLines(false);
        vehicleTable.setIntercellSpacing(new Dimension(0, 0));
        vehicleTable.setRowSelectionAllowed(false);
        vehicleTable.setCellSelectionEnabled(false);
        vehicleTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                if (event.getClickCount() == 2) {
                    int row = vehicleTable.rowAtPoint(event.getPoint());
                    if (row >= 0) {
                        selectVehicleRow(row);
                    }
                }
            }
        });
        vehicleTable.getColumnModel().getColumn(4).setMaxWidth(70);
        vehicleTable.getColumnModel().getColumn(4).setCellRenderer(new LeftAlignedRenderer());
        vehicleTable.getColumnModel().getColumn(0).setCellRenderer(new RowBodyRenderer());
        vehicleTable.getColumnModel().getColumn(1).setCellRenderer(new RowBodyRenderer());
        vehicleTable.getColumnModel().getColumn(2).setCellRenderer(new RowBodyRenderer());
        vehicleTable.getColumnModel().getColumn(3).setCellRenderer(new RowBodyRenderer());
        vehicleTable.getColumnModel().getColumn(5).setCellRenderer(new RowBodyRenderer());
        vehicleTable.getColumnModel().getColumn(6).setCellRenderer(new RowBodyRenderer());

        JTableHeader header = vehicleTable.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setDefaultRenderer(new HeaderRenderer());

        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CARD);
        scrollPane.setOpaque(false);

        card.add(top, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    private JPanel createBookingCard() {
        JPanel card = createCardPanel();
        card.setPreferredSize(new Dimension(400, 0));
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(8, 0, 8, 0);

        JLabel heading = createCardHeading("Rental Details");
        gbc.gridy = 0;
        card.add(heading, gbc);

        selectedVehicleLabel = new JLabel("No vehicle selected");
        selectedVehicleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        selectedVehicleLabel.setForeground(TEXT);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 4, 0);
        card.add(selectedVehicleLabel, gbc);

        selectedVehicleMetaLabel = createMutedLabel("Choose a vehicle to see rental details.");
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 14, 0);
        card.add(selectedVehicleMetaLabel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(16, 0, 8, 0);
        card.add(createDetailsGrid(), gbc);

        rentButton = createPrimaryButton("Rent This Vehicle");
        rentButton.setEnabled(false);
        rentButton.addActionListener(event -> rentSelectedVehicle());
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 0, 0);
        card.add(rentButton, gbc);

        gbc.gridy = 5;
        gbc.weighty = 1;
        card.add(new JLabel(), gbc);
        return card;
    }

    private JPanel createDetailsGrid() {
        JPanel details = new JPanel(new GridBagLayout());
        details.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 8, 8);

        brandValueLabel = createDetailValueLabel("-");
        typeValueLabel = createDetailValueLabel("-");
        seatsValueLabel = createDetailValueLabel("-");
        transmissionValueLabel = createDetailValueLabel("-");
        rateValueLabel = createDetailValueLabel("-");

        gbc.gridx = 0;
        gbc.gridy = 0;
        details.add(createDetailChip("Brand", brandValueLabel), gbc);
        gbc.gridx = 1;
        details.add(createDetailChip("Type", typeValueLabel), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        details.add(createDetailChip("Seats", seatsValueLabel), gbc);
        gbc.gridx = 1;
        details.add(createDetailChip("Transmission", transmissionValueLabel), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        details.add(createDetailChip("Daily Rate", rateValueLabel), gbc);
        return details;
    }

    private JPanel createDetailChip(String labelText, JLabel valueLabel) {
        JPanel chip = new JPanel(new BorderLayout(0, 2));
        chip.setBackground(CARD_ALT);
        chip.setBorder(new EmptyBorder(10, 12, 10, 12));

        JLabel label = new JLabel(labelText);
        label.setForeground(MUTED_TEXT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));

        valueLabel.setForeground(TEXT);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        chip.add(label, BorderLayout.NORTH);
        chip.add(valueLabel, BorderLayout.CENTER);
        return chip;
    }

    private JLabel createDetailValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }

    private JPanel createFooterPanel() {
        JPanel footer = createCardPanel();
        footer.setLayout(new BorderLayout());
        statusLabel = new JLabel("Ready to rent a vehicle.");
        statusLabel.setForeground(MUTED_TEXT);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        footer.add(statusLabel, BorderLayout.WEST);
        return footer;
    }

    private JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 34, 34);
                g2.setColor(new Color(220, 228, 235));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 34, 34);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));
        return panel;
    }

    private JTextField createPlaceholderField(String placeholder) {
        JTextField field = new RoundedTextField(placeholder);
        field.setPreferredSize(new Dimension(230, 42));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent event) {
                if (placeholder.equals(field.getText())) {
                    field.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent event) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                }
            }
        });
        return field;
    }

    private JLabel createCardHeading(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        return label;
    }

    private JLabel createMutedLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(MUTED_TEXT);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
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

    private JButton createSecondaryButton(String text) {
        JButton button = new RoundedActionButton(text, Color.WHITE, ACCENT, new Color(203, 213, 225));
        button.setPreferredSize(new Dimension(120, 42));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(ACCENT);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setPreferredSize(new Dimension(150, 42));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setForeground(TEXT);
        comboBox.setOpaque(false);
        comboBox.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        comboBox.setFocusable(false);
        comboBox.setRenderer(new DarkComboBoxRenderer());
        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("v");
                button.setForeground(TEXT);
                button.setBackground(CARD);
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setFocusPainted(false);
                button.setOpaque(false);
                return button;
            }
        });
    }

    private void applyFilters() {
        tableModel.setVehicles(catalog.searchAvailableVehicles(
                getFieldText(searchField, SEARCH_PLACEHOLDER),
                typeComboBox == null ? "All" : String.valueOf(typeComboBox.getSelectedItem())
        ));
        selectedRow = -1;
        updateSelectionDetails();
    }

    private void resetFilters() {
        searchField.setText(SEARCH_PLACEHOLDER);
        typeComboBox.setSelectedIndex(0);
        applyFilters();
        statusLabel.setText("Filters cleared.");
    }

    private void updateSelectionDetails() {
        Vehicle selectedVehicle = tableModel.getVehicleAt(selectedRow);
        if (selectedVehicle == null) {
            selectedVehicleLabel.setText("No vehicle selected");
            selectedVehicleMetaLabel.setText("Choose a vehicle to see rental details.");
            setDetailValues("-", "-", "-", "-", "-");
            updateRentButtonState(false);
            return;
        }

        selectedVehicleLabel.setText(selectedVehicle.getDisplayName());
        selectedVehicleMetaLabel.setText(selectedVehicle.getType() + " • " + selectedVehicle.getSeats() + " seats • " + selectedVehicle.getTransmission());
        setDetailValues(
                selectedVehicle.getBrand(),
                selectedVehicle.getType(),
                String.valueOf(selectedVehicle.getSeats()),
                selectedVehicle.getTransmission(),
                currencyFormat.format(selectedVehicle.getDailyRate()) + " / day"
        );
        updateRentButtonState(true);
    }

    private void rentSelectedVehicle() {
        Vehicle selectedVehicle = tableModel.getVehicleAt(selectedRow);
        if (selectedVehicle == null) {
            return;
        }

        if (!catalog.rentVehicle(selectedVehicle.getVehicleId())) {
            statusLabel.setText("This vehicle is no longer available.");
            applyFilters();
            return;
        }

        statusLabel.setText(selectedVehicle.getDisplayName() + " status changed to Rented.");
        applyFilters();
    }

    private void selectVehicleRow(int row) {
        if (row < 0 || row >= tableModel.getRowCount()) {
            return;
        }

        selectedRow = row;
        vehicleTable.repaint();
        updateSelectionDetails();
        statusLabel.setText("Selected " + tableModel.getVehicleAt(row).getDisplayName() + ".");
    }

    private void updateRentButtonState(boolean enabled) {
        if (rentButton != null) {
            rentButton.setEnabled(enabled);
        }
    }

    private void setDetailValues(String brand, String type, String seats, String transmission, String dailyRate) {
        brandValueLabel.setText(brand);
        typeValueLabel.setText(type);
        seatsValueLabel.setText(seats);
        transmissionValueLabel.setText(transmission);
        rateValueLabel.setText(dailyRate);
    }

    private String getFieldText(JTextField field, String placeholder) {
        if (field == null) {
            return "";
        }
        String text = field.getText();
        if (text == null || text.equals(placeholder)) {
            return "";
        }
        return text.trim();
    }

    private void openCustomerDashboard() {
        try {
            Class<?> dashboardClass = Class.forName("customer.CustomerDashboard");
            dashboardClass.getMethod("setActiveCustomer", Object.class).invoke(null, customer);
            JFrame dashboardFrame = (JFrame) dashboardClass.getConstructor().newInstance();
            dashboardFrame.setVisible(true);
            dispose();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException | InstantiationException ex) {
            statusLabel.setText("Unable to open the customer dashboard.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ignored) {
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
            label.setForeground(Color.WHITE);
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
            label.setForeground(isSelected ? Color.WHITE : TEXT);
            label.setBackground(isSelected ? ACCENT_DARK : CARD);
            label.setBorder(new EmptyBorder(10, 12, 10, 12));

            return label;
        }
    }

    private static class RowBodyRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 228, 235)));
            return label;
        }
    }

    private static class LeftAlignedRenderer extends RowBodyRenderer {
        private LeftAlignedRenderer() {
            setHorizontalAlignment(LEFT);
        }
    }

    private static class RoundedTextField extends JTextField {
        private RoundedTextField(String text) {
            super(text);
            setOpaque(false);
            setBorder(new EmptyBorder(0, 12, 0, 12));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(Color.WHITE);
            graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            graphics2D.setColor(new Color(203, 213, 225));
            graphics2D.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            graphics2D.dispose();
            super.paintComponent(graphics);
        }
    }

    private static class RoundedActionButton extends JButton {
        private final Color fillColor;
        private final Color outlineColor;

        private RoundedActionButton(String text, Color backgroundColor, Color foreground, Color outlineColor) {
            super(text);
            this.fillColor = backgroundColor;
            this.outlineColor = outlineColor;
            setForeground(foreground);
            setBackground(backgroundColor);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setBorder(new EmptyBorder(10, 16, 10, 16));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(fillColor);
            graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            graphics2D.setColor(outlineColor);
            graphics2D.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            graphics2D.dispose();
            super.paintComponent(graphics);
        }
    }

    private static class RoundedComboBox<E> extends JComboBox<E> {
        private RoundedComboBox(E[] items) {
            super(items);
            setOpaque(false);
            setBorder(new EmptyBorder(0, 12, 0, 12));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(Color.WHITE);
            graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            graphics2D.setColor(new Color(203, 213, 225));
            graphics2D.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            graphics2D.dispose();
            super.paintComponent(graphics);
        }
    }
}
