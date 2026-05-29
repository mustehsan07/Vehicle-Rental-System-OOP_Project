package admin;
import data.RentalData;
import data.RentalRequestData;
import data.VehicleData;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Car;
import model.Customer;
import model.Rental;
import model.RentalRequest;
import model.RentalVehicle;
import utils.AppTheme;
import utils.TableStyles;
import vehicle_display.VehicleCatalog;

public class RentalRequestsDialog extends JDialog {
    private final DefaultTableModel tableModel;
    private final JTable table;

    public RentalRequestsDialog(JFrame owner) {
        super(owner, "Rent Requests", true);
        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(1100, 520));
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 28, 28));
        setBackground(AppTheme.BACKGROUND);
        setLocationRelativeTo(owner);

        tableModel = new DefaultTableModel(new Object[]{"Request ID", "Customer", "Vehicle", "Days", "Total", "Status", "Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel) {
            @Override
            public java.awt.Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                java.awt.Component component = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row)) {
                    component.setBackground(AppTheme.ACCENT_SOFT);
                } else {
                    component.setBackground(row % 2 == 0 ? AppTheme.CARD : AppTheme.CARD_ALT);
                }
                component.setForeground(AppTheme.TEXT_PRIMARY);
                if (component instanceof javax.swing.JComponent jComponent) {
                    jComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER));
                }
                return component;
            }
        };
        table.setBackground(AppTheme.CARD);
        table.setOpaque(true);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setFont(AppTheme.BODY_FONT);
        table.getTableHeader().setDefaultRenderer(new TableStyles.HeaderRenderer());
        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getPreferredSize().width, 40));
        table.getTableHeader().setOpaque(true);
        table.setSelectionBackground(AppTheme.ACCENT);
        table.setSelectionForeground(Color.WHITE);
        table.getColumnModel().getColumn(0).setCellRenderer(new TableStyles.RowBodyRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(new TableStyles.RowBodyRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new TableStyles.RowBodyRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new TableStyles.RowBodyRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new TableStyles.RowBodyRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(new TableStyles.RowBodyRenderer());
        table.getColumnModel().getColumn(6).setCellRenderer(new TableStyles.RowBodyRenderer());

        JPanel root = new JPanel(new BorderLayout(0, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.BACKGROUND);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.setColor(AppTheme.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 28, 28);
                g2.dispose();
            }
        };
        root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        root.setOpaque(false);

        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setOpaque(false);
        JLabel title = new JLabel("Rent Requests");
        title.setFont(AppTheme.TITLE_FONT);
        title.setForeground(AppTheme.TEXT_PRIMARY);
        header.add(title, BorderLayout.WEST);

        JButton closeButton = button("Close", AppTheme.CARD, AppTheme.ACCENT_SOFT, AppTheme.TEXT_PRIMARY);
        closeButton.addActionListener(e -> dispose());
        header.add(closeButton, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(AppTheme.CARD);
        scrollPane.setOpaque(false);
        root.add(scrollPane, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        JButton approveButton = button("Approve", AppTheme.ACCENT, AppTheme.ACCENT_HOVER, Color.WHITE);
        JButton rejectButton = button("Reject", AppTheme.CARD, AppTheme.ACCENT_SOFT, AppTheme.TEXT_PRIMARY);
        JButton refreshButton = button("Refresh", AppTheme.CARD, AppTheme.ACCENT_SOFT, AppTheme.TEXT_PRIMARY);

        approveButton.addActionListener(e -> approveSelected());
        rejectButton.addActionListener(e -> rejectSelected());
        refreshButton.addActionListener(e -> refreshTable());

        actions.add(approveButton);
        actions.add(rejectButton);
        actions.add(refreshButton);
        root.add(actions, BorderLayout.SOUTH);

        setContentPane(root);
        refreshTable();
    }

    private JButton button(String text, Color base, Color hover, Color foreground) {
        RoundedButton button = new RoundedButton(text, base, hover, foreground, AppTheme.BORDER, AppTheme.RADIUS_SMALL);
        button.setFont(AppTheme.BUTTON_FONT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        return button;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<RentalRequest> requests = RentalRequestData.getRequests();
        for (RentalRequest request : requests) {
            tableModel.addRow(new Object[]{
                    request.getRequestId(),
                    request.getCustomerDisplayName(),
                    request.getVehicleDisplayName(),
                    request.getDays(),
                    String.format("PKR %,.2f", request.getTotalCost()),
                    request.getStatus(),
                    request.getRequestDate()
            });
        }
    }

    private void approveSelected() {
        RentalRequest request = getSelectedRequest();
        if (request == null) {
            return;
        }
        if (!"Pending".equalsIgnoreCase(request.getStatus())) {
            JOptionPane.showMessageDialog(this, "Only pending requests can be approved.");
            return;
        }

        Customer customer = new Customer(request.getCustomerId(), request.getCustomerName(), request.getCustomerEmail(), "", request.getCustomerPhone());
        RentalVehicle vehicle = new RentalVehicle(request.getVehicleId(), request.getVehicleBrand(), request.getVehicleModel(), request.getVehicleType(), request.getDailyRate(), false);
        Rental rental = new Rental(
                request.getRequestId(),
                customer,
                vehicle,
                request.getDays(),
                request.getTotalCost(),
                request.getRequestDate(),
                java.time.LocalDate.now().plusDays(request.getDays()).toString()
        );

        if (VehicleData.getVehicleById(request.getVehicleId()) != null) {
            VehicleData.updateVehicle(new Car(request.getVehicleId(), request.getVehicleBrand(), request.getVehicleModel(), request.getDailyRate(), false, 4));
        }
        RentalData.addRental(rental);
        RentalRequestData.markApproved(request.getRequestId());
        // notify customer about approval
        try {
            utils.EmailService.sendRequestStatusToCustomer(request, "approved");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        refreshTable();
        JOptionPane.showMessageDialog(this, "Request approved.");
    }

    private void rejectSelected() {
        RentalRequest request = getSelectedRequest();
        if (request == null) {
            return;
        }
        if (!"Pending".equalsIgnoreCase(request.getStatus())) {
            JOptionPane.showMessageDialog(this, "Only pending requests can be rejected.");
            return;
        }

        new VehicleCatalog().returnVehicle(request.getVehicleId());
        if (VehicleData.getVehicleById(request.getVehicleId()) != null) {
            VehicleData.updateVehicle(new Car(request.getVehicleId(), request.getVehicleBrand(), request.getVehicleModel(), request.getDailyRate(), true, 4));
        }
        RentalRequestData.markRejected(request.getRequestId());
        // notify customer about rejection
        try {
            utils.EmailService.sendRequestStatusToCustomer(request, "rejected");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        refreshTable();
        JOptionPane.showMessageDialog(this, "Request rejected.");
    }

    private RentalRequest getSelectedRequest() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a request first.");
            return null;
        }
        String requestId = String.valueOf(tableModel.getValueAt(table.convertRowIndexToModel(selectedRow), 0));
        return RentalRequestData.findById(requestId);
    }
}
