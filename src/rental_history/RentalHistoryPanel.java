package rental_history;

import admin.AdminTheme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.RentalHistory;

/**
 * Reusable rental history panel. Receives history data from caller and renders
 * it using the same look-and-feel as CustomerDashboard's history table.
 */
public class RentalHistoryPanel extends JPanel {
    private final JTable table;
    private final DefaultTableModel tableModel;

    public RentalHistoryPanel() {
        super(new BorderLayout());
        String[] cols = {"Rental ID", "Vehicle", "Rent Date", "Return Date", "Duration", "Bill"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel) {
            @Override
            public java.awt.Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                java.awt.Component component = super.prepareRenderer(renderer, row, column);
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
        table.getColumnModel().getColumn(5).setCellRenderer(centered);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(AdminTheme.CARD);
        scrollPane.setOpaque(false);

        add(scrollPane, BorderLayout.CENTER);
        setPreferredSize(new Dimension(800, 400));
    }

    /**
     * Refresh the table with a list of RentalHistory objects provided by caller.
     */
    public void refreshWithData(List<RentalHistory> list) {
        tableModel.setRowCount(0);
        if (list == null) return;
        for (RentalHistory rh : list) {
            Object[] row = new Object[]{
                rh.getHistoryId(),
                rh.getVehicleDisplayName(),
                rh.getRentDate(),
                rh.getReturnDate(),
                rh.getDays() + " days",
                String.format("%.2f", rh.getTotalCost())
            };
            tableModel.addRow(row);
        }
    }

    // Quick test harness (optional)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Rental History Test");
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            RentalHistoryPanel panel = new RentalHistoryPanel();
            f.getContentPane().add(panel);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
