package vehicle_display;

import customer.CustomerDashboard;
import data.RentalRequestData;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
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
import model.Customer;
import model.RentalRequest;
import utils.AppTheme;

public class VehicleRentalModuleApp extends JFrame {
    private static final Color CARD = AppTheme.CARD;
    private static final Color CARD_ALT = AppTheme.CARD_ALT;
    private static final Color SURFACE_LIGHT = AppTheme.TABLE_HEADER_BACKGROUND;
    private static final Color TEXT = AppTheme.TEXT_PRIMARY;
    private static final Color MUTED_TEXT = AppTheme.TEXT_SECONDARY;
    private static final Color ACCENT = AppTheme.ACCENT;
    private static final Color ACCENT_DARK = AppTheme.ACCENT_HOVER;
    private static final Color ACCENT_SOFT = AppTheme.ACCENT_SOFT;
    private static final Color BORDER = AppTheme.BORDER;
    private static final String SEARCH_PLACEHOLDER = "Search by ID, name, or brand";

    private static Customer currentCustomer;

    private final VehicleCatalog catalog = new VehicleCatalog();
    private final VehicleTableModel tableModel = new VehicleTableModel();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-PK"));
    private final Customer customer;

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

    public static void setCurrentCustomer(Customer customer) {
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
                AppTheme.paintBackground(g2, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setBorder(new EmptyBorder(22, 24, 22, 24));
        root.setOpaque(false);
        root.add(createHeaderPanel(), BorderLayout.NORTH);
        root.add(createContentPanel(), BorderLayout.CENTER);
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
                    jComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
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
        rentButton.addActionListener(event -> openRentPopup());
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 0, 0);
        card.add(rentButton, gbc);

        statusLabel = createMutedLabel("Select a vehicle to begin.");
        gbc.gridy = 5;
        gbc.insets = new Insets(12, 0, 0, 0);
        card.add(statusLabel, gbc);

        gbc.gridy = 6;
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

    private JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 34, 34);
                g2.setColor(BORDER);
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
        field.setPreferredSize(new Dimension(260, 42));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(AppTheme.INPUT_BACKGROUND);
        field.setForeground(TEXT);
        field.setCaretColor(Color.WHITE);
        field.setSelectionColor(AppTheme.ACCENT_SOFT);
        field.setSelectedTextColor(Color.WHITE);
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
        JButton button = new RoundedActionButton(text, CARD_ALT, ACCENT, BORDER);
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
        comboBox.setPreferredSize(new Dimension(180, 42));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setForeground(TEXT);
        comboBox.setBackground(AppTheme.INPUT_BACKGROUND);
        comboBox.setOpaque(false);
        comboBox.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        comboBox.setFocusable(false);
        comboBox.setRenderer(new DarkComboBoxRenderer());
        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("v") {
                    @Override
                    protected void paintComponent(Graphics graphics) {
                        Graphics2D graphics2D = (Graphics2D) graphics.create();
                        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        graphics2D.setColor(AppTheme.INPUT_BACKGROUND);
                        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                        graphics2D.dispose();
                        super.paintComponent(graphics);
                    }
                };
                button.setPreferredSize(new Dimension(34, 42));
                button.setForeground(TEXT);
                button.setBackground(AppTheme.INPUT_BACKGROUND);
                button.setHorizontalAlignment(SwingConstants.CENTER);
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setFocusPainted(false);
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
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
        setStatusMessage("Filters cleared.");
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

    private void openRentPopup() {
        Vehicle selectedVehicle = tableModel.getVehicleAt(selectedRow);
        if (selectedVehicle == null) {
            return;
        }

        RentPopupDialog dialog = new RentPopupDialog(this, selectedVehicle, customer, this::afterRentalCreated);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void selectVehicleRow(int row) {
        if (row < 0 || row >= tableModel.getRowCount()) {
            return;
        }

        selectedRow = row;
        vehicleTable.repaint();
        updateSelectionDetails();
        setStatusMessage("Selected " + tableModel.getVehicleAt(row).getDisplayName() + ".");
    }

    private void afterRentalCreated() {
        setStatusMessage("Vehicle rented successfully.");
        applyFilters();
    }

    private void setStatusMessage(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
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
        CustomerDashboard.setActiveCustomer(customer);
        new CustomerDashboard().setVisible(true);
        dispose();
    }

    private class RentPopupDialog extends javax.swing.JDialog {
        private final Vehicle selectedVehicle;
        private final Customer customer;
        private final Runnable onCompleted;
        private final JTextField durationField = new RoundedTextField("1");
        private final JLabel totalLabel = new JLabel();

        private RentPopupDialog(Window owner, Vehicle selectedVehicle, Customer customer, Runnable onCompleted) {
            super(owner, "Rent Vehicle", Dialog.ModalityType.APPLICATION_MODAL);
            this.selectedVehicle = selectedVehicle;
            this.customer = customer;
            this.onCompleted = onCompleted;
            buildUi();
            updateTotal();
        }

        private void buildUi() {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setResizable(false);
            setSize(new Dimension(520, 430));

            JPanel root = new JPanel(new BorderLayout(0, 14));
            root.setBorder(new EmptyBorder(18, 18, 18, 18));
            root.setBackground(CARD);

            JLabel heading = new JLabel("Rent This Vehicle");
            heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
            heading.setForeground(AppTheme.TEXT_PRIMARY);
            root.add(heading, BorderLayout.NORTH);

            JPanel body = new JPanel();
            body.setLayout(new javax.swing.BoxLayout(body, javax.swing.BoxLayout.Y_AXIS));
            body.setOpaque(false);
            body.add(infoRow("Vehicle", selectedVehicle.getDisplayName()));
            body.add(infoRow("Rate / Day", currencyFormat.format(selectedVehicle.getDailyRate())));
            body.add(infoRow("Customer", getCustomerDisplayName()));
            body.add(infoFieldRow("Duration (days)", durationField));
            body.add(infoRow("Total", totalLabel));

            durationField.setColumns(12);
            durationField.setMaximumSize(new Dimension(260, 40));
            durationField.setPreferredSize(new Dimension(260, 40));
            durationField.setMinimumSize(new Dimension(180, 40));
            durationField.setAlignmentX(LEFT_ALIGNMENT);
            durationField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                durationField.setBackground(CARD_ALT);
            durationField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    new EmptyBorder(0, 10, 0, 10)
            ));
            durationField.getDocument().addDocumentListener(new SimpleDocumentListener(this::updateTotal));

            totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            totalLabel.setForeground(ACCENT);

            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            actions.setOpaque(false);
            JButton rentButton = new RoundedActionButton("Rent", ACCENT, Color.WHITE, ACCENT_DARK);
            rentButton.setPreferredSize(new Dimension(120, 42));
            rentButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            rentButton.addActionListener(event -> submitRent());
            actions.add(rentButton);

            root.add(body, BorderLayout.CENTER);
            root.add(actions, BorderLayout.SOUTH);
            setContentPane(root);
        }

        private JPanel infoRow(String label, String value) {
            JPanel row = new JPanel(new BorderLayout(0, 4));
            row.setOpaque(false);
            row.setBorder(new EmptyBorder(0, 0, 8, 0));
            JLabel labelView = new JLabel(label);
            labelView.setForeground(MUTED_TEXT);
            labelView.setFont(new Font("Segoe UI", Font.BOLD, 12));
            JLabel valueView = new JLabel(value);
            valueView.setForeground(TEXT);
            valueView.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            row.add(labelView, BorderLayout.NORTH);
            row.add(valueView, BorderLayout.CENTER);
            return row;
        }

        private JPanel infoFieldRow(String label, JTextField field) {
            JPanel row = new JPanel(new java.awt.GridBagLayout());
            row.setOpaque(false);
            row.setBorder(new EmptyBorder(0, 0, 8, 0));

            JLabel labelView = new JLabel(label);
            labelView.setForeground(MUTED_TEXT);
            labelView.setFont(new Font("Segoe UI", Font.BOLD, 12));

            java.awt.GridBagConstraints labelConstraints = new java.awt.GridBagConstraints();
            labelConstraints.gridx = 0;
            labelConstraints.gridy = 0;
            labelConstraints.anchor = java.awt.GridBagConstraints.WEST;
            labelConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            labelConstraints.weightx = 1.0;

            java.awt.GridBagConstraints fieldConstraints = new java.awt.GridBagConstraints();
            fieldConstraints.gridx = 0;
            fieldConstraints.gridy = 1;
            fieldConstraints.anchor = java.awt.GridBagConstraints.WEST;
            fieldConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            fieldConstraints.weightx = 1.0;
            fieldConstraints.insets = new java.awt.Insets(4, 0, 0, 0);

            row.add(labelView, labelConstraints);
            row.add(field, fieldConstraints);
            return row;
        }

        private JPanel infoRow(String label, JLabel valueLabel) {
            JPanel row = new JPanel(new BorderLayout(0, 4));
            row.setOpaque(false);
            row.setBorder(new EmptyBorder(0, 0, 8, 0));
            JLabel labelView = new JLabel(label);
            labelView.setForeground(MUTED_TEXT);
            labelView.setFont(new Font("Segoe UI", Font.BOLD, 12));
            row.add(labelView, BorderLayout.NORTH);
            row.add(valueLabel, BorderLayout.CENTER);
            return row;
        }

        private void updateTotal() {
            int days = parseDays();
            totalLabel.setText(days > 0 ? currencyFormat.format(days * selectedVehicle.getDailyRate()) : currencyFormat.format(0));
        }

        private int parseDays() {
            try {
                return Integer.parseInt(durationField.getText().trim());
            } catch (NumberFormatException ex) {
                return -1;
            }
        }

        private String getCustomerDisplayName() {
            return customer == null ? "Customer" : customer.getFullName();
        }

        private void submitRent() {
            int days = parseDays();
            if (days <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid rental duration.", "Input Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                double totalCost = days * selectedVehicle.getDailyRate();
                RentalRequest request = new RentalRequest(
                        "REQ-" + System.currentTimeMillis(),
                        customer.getId(),
                        customer.getFullName(),
                        customer.getEmail(),
                        customer.getPhone(),
                        selectedVehicle.getVehicleId(),
                        selectedVehicle.getBrand(),
                        selectedVehicle.getName(),
                        selectedVehicle.getType(),
                        selectedVehicle.getDailyRate(),
                        days,
                        totalCost,
                        LocalDate.now().toString(),
                        "Pending"
                );

                if (!catalog.rentVehicle(selectedVehicle.getVehicleId())) {
                    JOptionPane.showMessageDialog(this, "This vehicle is no longer available.", "Rental Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                RentalRequestData.addRequest(request);
                // notify admins asynchronously about the new request
                try {
                    utils.EmailService.sendNewRequestNotification(request);
                } catch (Throwable t) {
                    t.printStackTrace();
                }

                if (onCompleted != null) {
                    onCompleted.run();
                }
                dispose();
                JOptionPane.showMessageDialog(getOwner(), selectedVehicle.getDisplayName() + " sent for admin approval.", "Request Submitted", JOptionPane.INFORMATION_MESSAGE);
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, "Unable to create the request.", "Request Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class SimpleDocumentListener implements javax.swing.event.DocumentListener {
        private final Runnable callback;

        private SimpleDocumentListener(Runnable callback) {
            this.callback = callback;
        }

        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            callback.run();
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            callback.run();
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            callback.run();
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
                        BorderFactory.createMatteBorder(0, 0, 1, 1, BORDER),
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

            label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
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
            setBackground(AppTheme.INPUT_BACKGROUND);
            setForeground(TEXT);
            setCaretColor(Color.WHITE);
            setBorder(new EmptyBorder(0, 12, 0, 12));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(getBackground());
            graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            graphics2D.setColor(BORDER);
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
            setBackground(AppTheme.INPUT_BACKGROUND);
            setForeground(TEXT);
            setBorder(new EmptyBorder(0, 12, 0, 12));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(getBackground());
            graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            graphics2D.setColor(BORDER);
            graphics2D.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            graphics2D.dispose();
            super.paintComponent(graphics);
        }
    }
}
