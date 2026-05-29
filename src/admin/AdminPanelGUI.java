package admin;
import auth.ProVehicleLogin;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import utils.AppTheme;

public class AdminPanelGUI extends JPanel {

    public AdminPanelGUI() {
        setLayout(new BorderLayout());

        JPanel background = new JPanel(new BorderLayout(0, 18)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                AppTheme.paintBackground(g2, getWidth(), getHeight());
                g2.dispose();
            }
        };
        background.setOpaque(false);

        JPanel page = new JPanel(new BorderLayout(0, 18));
        page.setOpaque(false);
        page.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        page.add(createHeader(), BorderLayout.NORTH);
        page.add(createTabs(), BorderLayout.CENTER);
        background.add(page, BorderLayout.CENTER);
        add(background, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(16, 16));
        header.setLayout(new BorderLayout(16, 16));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JPanel accentStrip = new JPanel();
        accentStrip.setBackground(AppTheme.ACCENT);
        accentStrip.setPreferredSize(new Dimension(6, 0));
        accentStrip.setBorder(BorderFactory.createEmptyBorder());

        JPanel textWrap = new JPanel();
        textWrap.setOpaque(false);
        textWrap.setLayout(new BoxLayout(textWrap, BoxLayout.Y_AXIS));

        JLabel chip = new JLabel("Admin Dashboard");
        chip.setOpaque(true);
        chip.setBackground(AppTheme.ACCENT_SOFT);
        chip.setForeground(AppTheme.ACCENT);
        chip.setFont(AppTheme.CHIP_FONT);
        chip.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        chip.setAlignmentX(LEFT_ALIGNMENT);

        JLabel title = new JLabel("Admin Control Center");
        title.setFont(AppTheme.TITLE_FONT);
        title.setForeground(AppTheme.TEXT_PRIMARY);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Manage vehicles and customers in one premium dashboard", SwingConstants.LEFT);
        subtitle.setFont(AppTheme.SUBTITLE_FONT);
        subtitle.setForeground(AppTheme.TEXT_SECONDARY);
        subtitle.setAlignmentX(LEFT_ALIGNMENT);

        textWrap.add(chip);
        textWrap.add(javax.swing.Box.createVerticalStrut(10));
        textWrap.add(title);
        textWrap.add(javax.swing.Box.createVerticalStrut(6));
        textWrap.add(subtitle);

        RoundedButton logoutButton = new RoundedButton(
                "Logout",
                AppTheme.CARD,
                AppTheme.ACCENT_SOFT,
                AppTheme.ACCENT,
                AppTheme.BORDER,
                AppTheme.RADIUS_SMALL
        );
        logoutButton.setFont(AppTheme.BUTTON_FONT);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        logoutButton.addActionListener(e -> {
            new ProVehicleLogin().setVisible(true);
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        });

        RoundedButton notificationButton = new RoundedButton(
                "Notifications",
                AppTheme.ACCENT,
                AppTheme.ACCENT_HOVER,
                java.awt.Color.WHITE,
                AppTheme.ACCENT,
                AppTheme.RADIUS_SMALL
        );
        notificationButton.setFont(AppTheme.BUTTON_FONT);
        notificationButton.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        notificationButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            JFrame frame = window instanceof JFrame ? (JFrame) window : new JFrame();
            RentalRequestsDialog dialog = new RentalRequestsDialog(frame);
            dialog.setVisible(true);
        });

        JPanel actionWrap = new JPanel();
        actionWrap.setOpaque(false);
        actionWrap.add(notificationButton);
        actionWrap.add(logoutButton);

        header.add(accentStrip, BorderLayout.WEST);
        header.add(textWrap, BorderLayout.CENTER);
        header.add(actionWrap, BorderLayout.EAST);
        return header;
    }

    private JPanel createTabs() {
        VehicleManagementService vehicleService = new VehicleManagementService();
        CustomerManagementService customerService = new CustomerManagementService();

        JTabbedPane tabs = new JTabbedPane();
        tabs.setUI(new ModernTabbedPaneUI());
        tabs.setFont(AppTheme.SUBTITLE_FONT);
        tabs.setBackground(AppTheme.CARD);
        tabs.setForeground(AppTheme.TEXT_PRIMARY);
        tabs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tabs.setOpaque(false);
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        tabs.addTab("Vehicles", new VehiclesTabGUI(vehicleService));
        tabs.addTab("Customers", new CustomersTabGUI(customerService));

        RoundedPanel container = new RoundedPanel(AppTheme.CARD, AppTheme.RADIUS_LARGE, AppTheme.BORDER, 1);
        container.setLayout(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        container.add(tabs, BorderLayout.CENTER);
        return container;
    }
}
