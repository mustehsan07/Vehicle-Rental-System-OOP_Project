import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class VehiclesTabGUI extends RoundedPanel {
    private final VehicleManagementService service;
    private final DefaultTableModel tableModel;

    private final JTextField idField = new RoundedTextField(AdminTheme.RADIUS_SMALL);
    private final JTextField typeField = new RoundedTextField(AdminTheme.RADIUS_SMALL);
    private final JTextField modelField = new RoundedTextField(AdminTheme.RADIUS_SMALL);
    private final JTextField rateField = new RoundedTextField(AdminTheme.RADIUS_SMALL);
    private final JComboBox<String> statusBox = new RoundedComboBox<>(new String[]{"Available", "Rented"}, AdminTheme.RADIUS_SMALL);

    public VehiclesTabGUI(VehicleManagementService service) {
        super(AdminTheme.BACKGROUND, AdminTheme.RADIUS_LARGE, AdminTheme.BORDER, 1);
        this.service = service;
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        tableModel = new DefaultTableModel(new Object[]{"ID", "Type", "Model", "Status", "Rate/Day"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        add(createTableCard(), BorderLayout.CENTER);
        add(createFormCard(), BorderLayout.SOUTH);
        refreshTable();
    }

    private JPanel createTableCard() {
        RoundedPanel card = new RoundedPanel(AdminTheme.CARD, AdminTheme.RADIUS_LARGE, AdminTheme.BORDER, 1);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel heading = new JLabel("Vehicle Inventory");
        heading.setFont(AdminTheme.SUBTITLE_FONT);
        heading.setForeground(AdminTheme.TEXT_PRIMARY);
        card.add(heading, BorderLayout.NORTH);

        JTable table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    component.setBackground(row % 2 == 0 ? AdminTheme.CARD : AdminTheme.CARD_ALT);
                }
                return component;
            }
        };
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new java.awt.Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setRowMargin(0);
        table.getTableHeader().setBackground(AdminTheme.TABLE_HEADER_BACKGROUND);
        table.getTableHeader().setForeground(AdminTheme.TABLE_HEADER_FOREGROUND);
        table.getTableHeader().setFont(AdminTheme.TABLE_HEADER_FONT);
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AdminTheme.BORDER));
        table.setSelectionBackground(AdminTheme.ACCENT);
        table.setSelectionForeground(java.awt.Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(AdminTheme.CARD);
        scrollPane.setOpaque(false);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private JPanel createFormCard() {
        RoundedPanel card = new RoundedPanel(AdminTheme.CARD, AdminTheme.RADIUS_LARGE, AdminTheme.BORDER, 1);
        card.setLayout(new BorderLayout(8, 8));
        card.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel fields = new JPanel(new GridLayout(2, 5, 4, 4));
        fields.setBackground(AdminTheme.CARD);
        fields.add(label("ID"));
        fields.add(label("Type"));
        fields.add(label("Model"));
        fields.add(label("Rate"));
        fields.add(label("Status"));
        fields.add(idField);
        fields.add(typeField);
        fields.add(modelField);
        fields.add(rateField);
        fields.add(statusBox);
        statusBox.setPreferredSize(new java.awt.Dimension(0, 40));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setBackground(AdminTheme.CARD);

        JButton addBtn = button("Add");
        JButton updateBtn = button("Update");
        JButton deleteBtn = button("Delete", AdminTheme.ACCENT_HOVER, AdminTheme.ACCENT_HOVER, java.awt.Color.WHITE, null);
        JButton clearBtn = outlineButton("Clear");

        addBtn.addActionListener(e -> addVehicle());
        updateBtn.addActionListener(e -> updateVehicle());
        deleteBtn.addActionListener(e -> deleteVehicle());
        clearBtn.addActionListener(e -> clearForm());

        actions.add(addBtn);
        actions.add(updateBtn);
        actions.add(deleteBtn);
        actions.add(clearBtn);

        card.add(fields, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);
        return card;
    }

    private JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(AdminTheme.TEXT_SECONDARY);
        label.setFont(AdminTheme.BODY_FONT);
        return label;
    }

    private JButton button(String text) {
        return button(text, AdminTheme.ACCENT, AdminTheme.ACCENT_HOVER, java.awt.Color.WHITE, AdminTheme.ACCENT);
    }

    private JButton outlineButton(String text) {
        return button(text, AdminTheme.CARD, AdminTheme.ACCENT_SOFT, AdminTheme.TEXT_PRIMARY, AdminTheme.BORDER);
    }

    private JButton button(String text, java.awt.Color base, java.awt.Color hover, java.awt.Color foreground, java.awt.Color border) {
        RoundedButton b = new RoundedButton(text, base, hover, foreground, border, AdminTheme.RADIUS_SMALL);
        b.setFont(AdminTheme.BUTTON_FONT);
        b.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        return b;
    }

    private void addVehicle() {
        try {
            VehicleManagementService.VehicleRecord record = readForm();
            if (!service.add(record)) {
                JOptionPane.showMessageDialog(this, "Vehicle ID already exists.");
                return;
            }
            refreshTable();
            clearForm();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void updateVehicle() {
        try {
            VehicleManagementService.VehicleRecord record = readForm();
            if (!service.update(record)) {
                JOptionPane.showMessageDialog(this, "Vehicle ID not found.");
                return;
            }
            refreshTable();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void deleteVehicle() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter ID to delete.");
            return;
        }
        if (!service.remove(id)) {
            JOptionPane.showMessageDialog(this, "Vehicle ID not found.");
            return;
        }
        refreshTable();
        clearForm();
    }

    private VehicleManagementService.VehicleRecord readForm() {
        String id = idField.getText().trim();
        String type = typeField.getText().trim();
        String model = modelField.getText().trim();
        String rateText = rateField.getText().trim();
        if (id.isEmpty() || type.isEmpty() || model.isEmpty() || rateText.isEmpty()) {
            throw new IllegalArgumentException("Please fill all fields.");
        }
        double rate;
        try {
            rate = Double.parseDouble(rateText);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Rate must be numeric.");
        }
        boolean available = "Available".equals(statusBox.getSelectedItem());
        return new VehicleManagementService.VehicleRecord(id, type, model, available, rate);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (VehicleManagementService.VehicleRecord record : service.getAll()) {
            tableModel.addRow(new Object[]{
                    record.id,
                    record.type,
                    record.model,
                    record.available ? "Available" : "Rented",
                    String.format("%.2f", record.dailyRate)
            });
        }
    }

    private void clearForm() {
        idField.setText("");
        typeField.setText("");
        modelField.setText("");
        rateField.setText("");
        statusBox.setSelectedIndex(0);
    }
}
