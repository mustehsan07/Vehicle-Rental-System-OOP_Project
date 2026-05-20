package admin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class CustomersTabGUI extends RoundedPanel {
    private final CustomerManagementService service;
    private final DefaultTableModel tableModel;
    private JTable table;

    private final JTextField idField = createField();
    private final JTextField nameField = createField();
    private final JTextField emailField = createField();
    private final JTextField phoneField = createField();

    public CustomersTabGUI(CustomerManagementService service) {
        super(AdminTheme.BACKGROUND, AdminTheme.RADIUS_LARGE, AdminTheme.BORDER, 1);
        this.service = service;
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Email", "Phone", "Active Rentals"}, 0) {
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

        JLabel heading = new JLabel("Customer Directory");
        heading.setFont(AdminTheme.SUBTITLE_FONT);
        heading.setForeground(AdminTheme.TEXT_PRIMARY);
        card.add(heading, BorderLayout.NORTH);

        table = new JTable(tableModel) {
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
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    populateFormFromSelectedRow();
                }
            }
        });

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

        JPanel fields = new JPanel(new GridLayout(2, 4, 4, 4));
        fields.setBackground(AdminTheme.CARD);
        fields.add(label("ID"));
        fields.add(label("Name"));
        fields.add(label("Email"));
        fields.add(label("Phone"));
        fields.add(idField);
        fields.add(nameField);
        fields.add(emailField);
        fields.add(phoneField);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setBackground(AdminTheme.CARD);

        JButton addBtn = button("Add");
        JButton updateBtn = button("Update");
        JButton deleteBtn = button("Delete", AdminTheme.ACCENT_HOVER, AdminTheme.ACCENT_HOVER, java.awt.Color.WHITE, null);
        JButton clearBtn = outlineButton("Clear");

        addBtn.addActionListener(e -> addCustomer());
        updateBtn.addActionListener(e -> updateCustomer());
        deleteBtn.addActionListener(e -> deleteCustomer());
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

    private void addCustomer() {
        try {
            CustomerManagementService.CustomerRecord record = readForm();
            if (!service.add(record)) {
                JOptionPane.showMessageDialog(this, "Customer ID already exists.");
                return;
            }
            refreshTable();
            clearForm();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void updateCustomer() {
        try {
            CustomerManagementService.CustomerRecord record = readForm();
            if (!service.update(record)) {
                JOptionPane.showMessageDialog(this, "Customer ID not found.");
                return;
            }
            refreshTable();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void deleteCustomer() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter ID to delete.");
            return;
        }
        if (!service.remove(id)) {
            JOptionPane.showMessageDialog(this, "Customer ID not found.");
            return;
        }
        refreshTable();
        clearForm();
    }

    private CustomerManagementService.CustomerRecord readForm() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        if (id.isEmpty() || name.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException("Please fill all fields.");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Enter a valid email.");
        }
        return new CustomerManagementService.CustomerRecord(id, name, email, phone, 0);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (CustomerManagementService.CustomerRecord record : service.getAll()) {
            tableModel.addRow(new Object[]{record.id, record.name, record.email, record.phone, record.activeRentals});
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
    }

    private JTextField createField() {
        JTextField field = new JTextField();
        field.setBackground(AdminTheme.BACKGROUND);
        field.setForeground(AdminTheme.TEXT_PRIMARY);
        field.setCaretColor(AdminTheme.ACCENT);
        field.setFont(AdminTheme.BODY_FONT.deriveFont(java.awt.Font.PLAIN, 13f));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, AdminTheme.ACCENT),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setPreferredSize(new java.awt.Dimension(0, 38));
        return field;
    }

    private void populateFormFromSelectedRow() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        idField.setText(String.valueOf(tableModel.getValueAt(modelRow, 0)));
        nameField.setText(String.valueOf(tableModel.getValueAt(modelRow, 1)));
        emailField.setText(String.valueOf(tableModel.getValueAt(modelRow, 2)));
        phoneField.setText(String.valueOf(tableModel.getValueAt(modelRow, 3)));
    }
}
