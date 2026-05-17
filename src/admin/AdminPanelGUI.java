import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

public class AdminPanelGUI extends JPanel {

    public AdminPanelGUI() {
        setLayout(new BorderLayout());
        setBackground(AdminTheme.BACKGROUND);

        JPanel page = new JPanel(new BorderLayout(0, 18));
        page.setOpaque(false);
        page.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        page.add(createHeader(), BorderLayout.NORTH);
        page.add(createTabs(), BorderLayout.CENTER);
        add(page, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        RoundedPanel header = new RoundedPanel(AdminTheme.CARD, AdminTheme.RADIUS_LARGE, AdminTheme.BORDER, 1);
        header.setLayout(new BorderLayout(16, 16));
        header.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JPanel accentStrip = new JPanel();
        accentStrip.setBackground(AdminTheme.ACCENT);
        accentStrip.setPreferredSize(new Dimension(6, 0));
        accentStrip.setBorder(BorderFactory.createEmptyBorder());

        JPanel textWrap = new JPanel();
        textWrap.setOpaque(false);
        textWrap.setLayout(new BoxLayout(textWrap, BoxLayout.Y_AXIS));

        JLabel chip = new JLabel("Admin Dashboard");
        chip.setOpaque(true);
        chip.setBackground(AdminTheme.ACCENT_SOFT);
        chip.setForeground(AdminTheme.ACCENT);
        chip.setFont(AdminTheme.CHIP_FONT);
        chip.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        chip.setAlignmentX(LEFT_ALIGNMENT);

        JLabel title = new JLabel("Admin Control Center");
        title.setFont(AdminTheme.TITLE_FONT);
        title.setForeground(AdminTheme.TEXT_PRIMARY);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Manage vehicles and customers in one premium dashboard", SwingConstants.LEFT);
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
        return header;
    }

    private JPanel createTabs() {
        VehicleManagementService vehicleService = new VehicleManagementService();
        CustomerManagementService customerService = new CustomerManagementService();

        JTabbedPane tabs = new JTabbedPane();
        tabs.setUI(new ModernTabbedPaneUI());
        tabs.setFont(AdminTheme.SUBTITLE_FONT);
        tabs.setBackground(AdminTheme.CARD);
        tabs.setForeground(AdminTheme.TEXT_PRIMARY);
        tabs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tabs.setOpaque(false);
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        tabs.addTab("Vehicles", new VehiclesTabGUI(vehicleService));
        tabs.addTab("Customers", new CustomersTabGUI(customerService));

        RoundedPanel container = new RoundedPanel(AdminTheme.CARD, AdminTheme.RADIUS_LARGE, AdminTheme.BORDER, 1);
        container.setLayout(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        container.add(tabs, BorderLayout.CENTER);
        return container;
    }
}
