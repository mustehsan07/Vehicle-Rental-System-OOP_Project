package customer;
import admin.RoundedButton;
import admin.RoundedPanel;
import auth.ProVehicleLogin;
import data.CustomerData;
import data.RentalData;
import data.RentalHistoryData;
import data.RentalRequestData;
import data.SampleDataLoader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import model.RentalRequest;
import rental_history.RentalHistoryPanel;
import utils.AppTheme;
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
        JPanel root = new JPanel(new BorderLayout(0, 18)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                AppTheme.paintBackground(g2, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setOpaque(false);
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildCenter(), BorderLayout.CENTER);
        return root;
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(16, 0));
        header.setOpaque(false);
        header.setLayout(new BorderLayout(16, 0));
        header.setBorder(BorderFactory.createEmptyBorder(4, 6, 8, 6));

        JPanel accentStrip = new JPanel();
        accentStrip.setBackground(AppTheme.ACCENT);
        accentStrip.setPreferredSize(new Dimension(6, 0));

        JPanel textWrap = new JPanel();
        textWrap.setOpaque(false);
        textWrap.setLayout(new BoxLayout(textWrap, BoxLayout.Y_AXIS));

        JLabel chip = new JLabel("Customer Dashboard");
        chip.setOpaque(true);
        chip.setBackground(AppTheme.ACCENT_SOFT);
        chip.setForeground(AppTheme.ACCENT);
        chip.setFont(AppTheme.CHIP_FONT);
        chip.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        chip.setAlignmentX(LEFT_ALIGNMENT);

        JLabel title = new JLabel("Customer Experience Center");
        title.setFont(AppTheme.TITLE_FONT);
        title.setForeground(AppTheme.TEXT_PRIMARY);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("See your profile, returned vehicles, and rental actions in one premium dashboard");
        subtitle.setFont(AppTheme.SUBTITLE_FONT);
        subtitle.setForeground(AppTheme.TEXT_SECONDARY);
        subtitle.setAlignmentX(LEFT_ALIGNMENT);

        textWrap.add(chip);
        textWrap.add(javax.swing.Box.createVerticalStrut(10));
        textWrap.add(title);
        textWrap.add(javax.swing.Box.createVerticalStrut(6));
        textWrap.add(subtitle);

        header.add(accentStrip, BorderLayout.WEST);
        header.add(textWrap, BorderLayout.CENTER);
        header.add(buildHeaderActionsPanel(), BorderLayout.EAST);
        return header;
    }

    private JPanel buildHeaderActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setOpaque(false);
        panel.add(buildRequestedVehiclesButton());
        panel.add(buildLogoutButton());
        return panel;
    }

    private RoundedButton buildRequestedVehiclesButton() {
        RoundedButton requestedButton = new RoundedButton(
                "Requested Vehicles",
                AppTheme.ACCENT,
                AppTheme.ACCENT_HOVER,
                Color.WHITE,
                AppTheme.ACCENT,
                AppTheme.RADIUS_SMALL
        );
        requestedButton.setFont(AppTheme.BUTTON_FONT);
        requestedButton.setPreferredSize(new Dimension(170, 30));
        requestedButton.setMinimumSize(new Dimension(170, 30));
        requestedButton.setMaximumSize(new Dimension(170, 30));
        requestedButton.setBorder(BorderFactory.createEmptyBorder(2, 16, 2, 16));
        requestedButton.addActionListener(event -> openRequestedVehiclesPopup());
        return requestedButton;
    }

    private RoundedButton buildLogoutButton() {
        RoundedButton logoutButton = new RoundedButton(
                "Logout",
                AppTheme.CARD,
                AppTheme.ACCENT_SOFT,
                AppTheme.ACCENT,
                AppTheme.BORDER,
                AppTheme.RADIUS_SMALL
        );
        logoutButton.setFont(AppTheme.BUTTON_FONT);
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
        RoundedPanel card = new RoundedPanel(AppTheme.CARD, AppTheme.RADIUS_LARGE, AppTheme.BORDER, 1);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel heading = new JLabel("Customer Information");
        heading.setFont(AppTheme.SUBTITLE_FONT);
        heading.setForeground(AppTheme.TEXT_PRIMARY);
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
        RoundedPanel card = new RoundedPanel(AppTheme.CARD, AppTheme.RADIUS_LARGE, AppTheme.BORDER, 1);
        card.setLayout(new BorderLayout(0, 14));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel heading = new JLabel("Actions");
        heading.setFont(AppTheme.SUBTITLE_FONT);
        heading.setForeground(AppTheme.TEXT_PRIMARY);
        card.add(heading, BorderLayout.NORTH);

        JLabel helper = new JLabel("<html><body style='width:260px'>Rent a vehicle from the display screen or process a return using the rented vehicles table.</body></html>");
        helper.setFont(AppTheme.BODY_FONT);
        helper.setForeground(AppTheme.TEXT_SECONDARY);
        helper.setVerticalAlignment(SwingConstants.TOP);
        card.add(helper, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(2, 1, 0, 10));
        buttons.setOpaque(false);

        RoundedButton rentBtn = new RoundedButton(
                "Rent New Vehicle",
                AppTheme.ACCENT,
                AppTheme.ACCENT_HOVER,
                Color.WHITE,
                AppTheme.ACCENT,
                AppTheme.RADIUS_SMALL
        );
        rentBtn.setFont(AppTheme.BUTTON_FONT);
        rentBtn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        rentBtn.addActionListener(e -> openVehicleDisplay());

        RoundedButton returnBtn = new RoundedButton(
                "Return Vehicle",
                AppTheme.CARD_ALT,
                AppTheme.ACCENT_SOFT,
                AppTheme.TEXT_PRIMARY,
                null,
                AppTheme.RADIUS_SMALL
        );
        returnBtn.setFont(AppTheme.BUTTON_FONT);
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
        RoundedPanel card = new RoundedPanel(AppTheme.CARD, AppTheme.RADIUS_LARGE, AppTheme.BORDER, 1);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel heading = new JLabel("Rental History");
        heading.setFont(AppTheme.SUBTITLE_FONT);
        heading.setForeground(AppTheme.TEXT_PRIMARY);
        card.add(heading, BorderLayout.NORTH);

        // Delegated to RentalHistoryPanel (styled consistently)
        card.add(rentalHistoryPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildActiveRentalCard() {
        RoundedPanel card = new RoundedPanel(AppTheme.CARD, AppTheme.RADIUS_LARGE, AppTheme.BORDER, 1);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel heading = new JLabel("Rented Vehicles");
        heading.setFont(AppTheme.SUBTITLE_FONT);
        heading.setForeground(AppTheme.TEXT_PRIMARY);
        card.add(heading, BorderLayout.NORTH);

        JTable table = activeRentalTable;
        table.setRowHeight(40);
        table.setBackground(AppTheme.CARD);
        table.setOpaque(true);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setRowMargin(0);
        table.setFont(AppTheme.BODY_FONT);
        table.getTableHeader().setDefaultRenderer(new utils.TableStyles.HeaderRenderer());
        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getPreferredSize().width, 40));
        table.getTableHeader().setOpaque(true);
        table.setSelectionBackground(AppTheme.ACCENT);
        table.setSelectionForeground(Color.WHITE);

        // Use the shared row/body renderers so row dividers and alignment
        // match the Available Fleet table implementation.
        table.getColumnModel().getColumn(0).setCellRenderer(new utils.TableStyles.CenteredRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(new utils.TableStyles.RowBodyRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new utils.TableStyles.CenteredRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new utils.TableStyles.CenteredRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new utils.TableStyles.RightAlignedRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(AppTheme.CARD);
        scrollPane.setOpaque(false);
        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    private JPanel infoLine(String label, JLabel valueLabel) {
        JPanel line = new JPanel(new BorderLayout(0, 2));
        line.setOpaque(false);

        JLabel labelView = new JLabel(label);
        labelView.setFont(AppTheme.CHIP_FONT);
        labelView.setForeground(AppTheme.TEXT_SECONDARY);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueLabel.setForeground(AppTheme.TEXT_PRIMARY);

        line.add(labelView, BorderLayout.NORTH);
        line.add(valueLabel, BorderLayout.CENTER);
        return line;
    }

    private RoundedPanel metricCard(String label, JLabel value) {
        RoundedPanel card = new RoundedPanel(AppTheme.CARD, AppTheme.RADIUS_MEDIUM, AppTheme.BORDER_SOFT, 1);
        card.setLayout(new BorderLayout(0, 4));
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel labelView = new JLabel(label);
        labelView.setFont(AppTheme.CHIP_FONT);
        labelView.setForeground(AppTheme.TEXT_SECONDARY);

        value.setFont(new Font("Segoe UI", Font.BOLD, 18));
        value.setForeground(AppTheme.TEXT_PRIMARY);

        card.add(labelView, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        return card;
    }

    private JLabel createStaticValue(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(AppTheme.TEXT_PRIMARY);
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

    private void openRequestedVehiclesPopup() {
        RequestedVehiclesDialog dialog = new RequestedVehiclesDialog(this, customer);
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

    private static class RequestedVehiclesDialog extends JDialog {
        private final Customer customer;
        private final DefaultTableModel tableModel;
        private final JTable table;

            private RequestedVehiclesDialog(Window owner, Customer customer) {
                super(owner, "Requested Vehicles", ModalityType.APPLICATION_MODAL);
                // Hide the OS window header so we use our custom header
                setUndecorated(true);
            this.customer = customer;
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setResizable(false);
            setSize(new Dimension(980, 420));

                JPanel root = new JPanel(new BorderLayout(0, 14));
                root.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createLineBorder(AppTheme.BORDER),
                    javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)
                ));
                root.setBackground(AppTheme.BACKGROUND);

            // Custom header (title + draggable area)
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setOpaque(false);
            JLabel heading = new JLabel("Requested Vehicles");
            heading.setFont(AppTheme.TITLE_FONT);
            heading.setForeground(AppTheme.TEXT_PRIMARY);
            heading.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            headerPanel.add(heading, BorderLayout.WEST);
            // allow dragging the dialog by the header
            final java.awt.Point[] mouseDown = new java.awt.Point[1];
            heading.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    mouseDown[0] = e.getPoint();
                }
            });
            heading.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                @Override
                public void mouseDragged(java.awt.event.MouseEvent e) {
                    java.awt.Point curr = getLocation();
                    setLocation(curr.x + e.getX() - mouseDown[0].x, curr.y + e.getY() - mouseDown[0].y);
                }
            });
            root.add(headerPanel, BorderLayout.NORTH);

            tableModel = new DefaultTableModel(new Object[]{"Request ID", "Vehicle", "Days", "Total", "Status", "Date"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            table = new JTable(tableModel);
            table.setBackground(AppTheme.CARD);
            table.setOpaque(true);
            table.setRowHeight(30);
            table.setShowGrid(false);
            table.setIntercellSpacing(new Dimension(0, 0));
            table.setFillsViewportHeight(true);
            table.setRowMargin(0);
            table.setFont(AppTheme.BODY_FONT);
            // Use shared header renderer for consistent look (removes white default cells)
            table.getTableHeader().setDefaultRenderer(new utils.TableStyles.HeaderRenderer());
            table.getTableHeader().setPreferredSize(new java.awt.Dimension(table.getTableHeader().getPreferredSize().width, 40));
            table.getTableHeader().setOpaque(true);
            table.setSelectionBackground(AppTheme.ACCENT);
            table.setSelectionForeground(Color.WHITE);
            // Column renderers to match Available Fleet structure
            table.getColumnModel().getColumn(0).setCellRenderer(new utils.TableStyles.CenteredRenderer());
            table.getColumnModel().getColumn(1).setCellRenderer(new utils.TableStyles.RowBodyRenderer());
            table.getColumnModel().getColumn(2).setCellRenderer(new utils.TableStyles.CenteredRenderer());
            table.getColumnModel().getColumn(3).setCellRenderer(new utils.TableStyles.RightAlignedRenderer());
            table.getColumnModel().getColumn(4).setCellRenderer(new StatusBadgeRenderer());
            table.getColumnModel().getColumn(5).setCellRenderer(new utils.TableStyles.CenteredRenderer());

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getViewport().setBackground(AppTheme.CARD);
            scrollPane.setOpaque(false);
            root.add(scrollPane, BorderLayout.CENTER);

            JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
            footer.setOpaque(false);
            RoundedButton closeButton = new RoundedButton(
                    "Close",
                    AppTheme.ACCENT,
                    AppTheme.ACCENT_HOVER,
                    Color.WHITE,
                    AppTheme.ACCENT,
                    AppTheme.RADIUS_SMALL
            );
            closeButton.setFont(AppTheme.BUTTON_FONT);
            closeButton.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
            closeButton.addActionListener(e -> dispose());

            RoundedButton refreshButton = new RoundedButton(
                    "Refresh",
                    AppTheme.CARD,
                    AppTheme.ACCENT_SOFT,
                    AppTheme.TEXT_PRIMARY,
                    AppTheme.BORDER,
                    AppTheme.RADIUS_SMALL
            );
            refreshButton.setFont(AppTheme.BUTTON_FONT);
            refreshButton.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
            refreshButton.addActionListener(e -> refreshRequests());

            footer.add(refreshButton);
            footer.add(closeButton);
            root.add(footer, BorderLayout.SOUTH);

            setContentPane(root);
            refreshRequests();
        }

        private void refreshRequests() {
            tableModel.setRowCount(0);
            for (RentalRequest request : RentalRequestData.getRequests()) {
                if (customer.getId().equalsIgnoreCase(request.getCustomerId())) {
                    tableModel.addRow(new Object[]{
                            request.getRequestId(),
                            request.getVehicleDisplayName(),
                            request.getDays(),
                            String.format("PKR %,.2f", request.getTotalCost()),
                            request.getStatus(),
                            request.getRequestDate()
                    });
                }
            }
        }

        private class StatusBadgeRenderer extends javax.swing.table.DefaultTableCellRenderer {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.LEFT);
                label.setFont(new Font("Segoe UI", Font.BOLD, 12));
                label.setOpaque(true);

                String status = String.valueOf(value);
                if ("Pending".equalsIgnoreCase(status)) {
                    label.setBackground(new Color(133, 77, 14));
                    label.setForeground(new Color(254, 243, 199));
                } else if ("Approved".equalsIgnoreCase(status)) {
                    label.setBackground(new Color(22, 101, 52));
                    label.setForeground(new Color(220, 252, 231));
                } else if ("Rejected".equalsIgnoreCase(status)) {
                    label.setBackground(new Color(127, 29, 29));
                    label.setForeground(new Color(254, 226, 226));
                } else {
                    label.setBackground(AppTheme.CARD);
                    label.setForeground(AppTheme.TEXT_PRIMARY);
                }

                if (isSelected) {
                    label.setBackground(AppTheme.ACCENT);
                    label.setForeground(Color.WHITE);
                }

                return label;
            }
        }
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
            root.setBackground(AppTheme.CARD);

            JLabel heading = new JLabel("Return Vehicle");
            heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
            heading.setForeground(AppTheme.TEXT_PRIMARY);
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
                    AppTheme.ACCENT,
                    AppTheme.ACCENT_HOVER,
                    Color.WHITE,
                    AppTheme.ACCENT,
                    AppTheme.RADIUS_SMALL
            );
            payReturnButton.setFont(AppTheme.BUTTON_FONT);
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
            labelView.setFont(AppTheme.CHIP_FONT);
            labelView.setForeground(AppTheme.TEXT_SECONDARY);

            JLabel valueView = new JLabel(value);
            valueView.setFont(new Font("Segoe UI", Font.BOLD, 14));
            valueView.setForeground(AppTheme.TEXT_PRIMARY);

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
            root.setBackground(AppTheme.CARD);

            JLabel title = new JLabel("Enter Exact Payable Amount");
            title.setFont(new Font("Segoe UI", Font.BOLD, 18));
            title.setForeground(AppTheme.TEXT_PRIMARY);
            root.add(title, BorderLayout.NORTH);

            JTextField amountField = new JTextField();
            amountField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                amountField.setBackground(AppTheme.INPUT_BACKGROUND);
                amountField.setForeground(AppTheme.TEXT_PRIMARY);
                amountField.setCaretColor(Color.WHITE);
            amountField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AppTheme.BORDER),
                    new EmptyBorder(0, 10, 0, 10)
            ));

            JLabel note = new JLabel("Total payable: " + formatMoney(rental.getTotalCost()));
            note.setForeground(AppTheme.TEXT_SECONDARY);
            note.setFont(AppTheme.BODY_FONT);

            JPanel body = new JPanel(new BorderLayout(0, 8));
            body.setOpaque(false);
            body.add(note, BorderLayout.NORTH);
            body.add(amountField, BorderLayout.CENTER);
            root.add(body, BorderLayout.CENTER);

            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            actions.setOpaque(false);
            RoundedButton payButton = new RoundedButton(
                    "Pay",
                    AppTheme.ACCENT,
                    AppTheme.ACCENT_HOVER,
                    Color.WHITE,
                    AppTheme.ACCENT,
                    AppTheme.RADIUS_SMALL
            );
            payButton.setFont(AppTheme.BUTTON_FONT);
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
