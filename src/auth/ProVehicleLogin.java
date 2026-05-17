import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ProVehicleLogin extends JFrame {
    private static final Color ACCENT = new Color(0x0F766E);
    private static final Color TEXT_DARK = new Color(0x0F172A);
    private static final Color TEXT_MUTED = new Color(0x64748B);

    private final BufferedImage backgroundImage;

    public ProVehicleLogin() {
        backgroundImage = loadBackgroundImage();

        setTitle("Vehicle Rental Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setResizable(false);
        setShape(new RoundRectangle2D.Double(0, 0, 900, 600, 30, 30));

        JPanel mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (backgroundImage != null) {
                    g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
                } else {
                    g2.setPaint(new java.awt.GradientPaint(0, 0, new Color(8, 31, 61), getWidth(), getHeight(), new Color(15, 118, 110)));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                g2.setComposite(AlphaComposite.SrcOver.derive(0.62f));
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        JButton exitBtn = createIconButton("X");
        exitBtn.setBounds(852, 10, 32, 32);
        exitBtn.addActionListener(e -> System.exit(0));
        mainPanel.add(exitBtn);

        JPanel heroPanel = new JPanel(null);
        heroPanel.setOpaque(false);
        heroPanel.setBounds(44, 120, 360, 240);

        JLabel headline = new JLabel("Elite Vehicle Rentals");
        headline.setFont(new Font("Segoe UI", Font.BOLD, 42));
        headline.setForeground(Color.WHITE);
        headline.setBounds(0, 0, 360, 60);
        heroPanel.add(headline);

        JLabel subtitle = new JLabel("Customer and admin access in one clean portal");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(new Color(230, 235, 241));
        subtitle.setBounds(2, 58, 340, 24);
        heroPanel.add(subtitle);

        JLabel story = new JLabel("<html><div style='width:320px;'>Manage vehicles, customer accounts, and rental flow from a focused dashboard designed for speed.</div></html>");
        story.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        story.setForeground(new Color(235, 240, 244));
        story.setBounds(2, 98, 330, 70);
        heroPanel.add(story);

        mainPanel.add(heroPanel);

        JPanel loginCard = createCard(500, 78, 340, 440);
        loginCard.setLayout(null);

        JLabel welcome = new JLabel("Welcome Back!");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcome.setForeground(TEXT_DARK);
        welcome.setBounds(34, 28, 250, 36);
        loginCard.add(welcome);

        JLabel subTitle = new JLabel("Vehicle Rental Management System");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subTitle.setForeground(TEXT_MUTED);
        subTitle.setBounds(34, 62, 260, 18);
        loginCard.add(subTitle);

        JLabel userLabel = createLabel("USERNAME");
        userLabel.setBounds(34, 108, 100, 16);
        loginCard.add(userLabel);

        JTextField userField = createField();
        userField.setBounds(34, 126, 272, 34);
        loginCard.add(userField);

        JLabel passLabel = createLabel("PASSWORD");
        passLabel.setBounds(34, 182, 100, 16);
        loginCard.add(passLabel);

        JPasswordField passField = createPasswordField();
        passField.setBounds(34, 200, 272, 34);
        loginCard.add(passField);

        JButton loginBtn = createAccentButton("LOGIN SYSTEM");
        loginBtn.setBounds(34, 268, 272, 46);
        loginBtn.addActionListener(e -> handleLogin(userField, passField));
        loginCard.add(loginBtn);

        JLabel instructions = new JLabel("<html><b>Instructions:</b><br>Use your email or username to sign in.<br>Customer accounts can be created from signup.</html>");
        instructions.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        instructions.setForeground(TEXT_MUTED);
        instructions.setBounds(34, 330, 280, 58);
        loginCard.add(instructions);

        JButton nextBtn = new JButton("Next →");
        nextBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nextBtn.setForeground(ACCENT);
        nextBtn.setContentAreaFilled(false);
        nextBtn.setBorderPainted(false);
        nextBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextBtn.setBounds(230, 396, 80, 22);
        nextBtn.addActionListener(e -> {
            new ProVehicleRegister().setVisible(true);
            dispose();
        });
        loginCard.add(nextBtn);

        mainPanel.add(loginCard);
        add(mainPanel);
    }

    private void handleLogin(JTextField userField, JPasswordField passField) {
        String identifier = userField.getText().trim();
        String password = new String(passField.getPassword());
        AuthController.AuthRecord user = AuthController.authenticate(identifier, password);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid login details.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        dispose();
        if ("admin".equalsIgnoreCase(user.role)) {
            launchAdminPanel();
        } else {
            JOptionPane.showMessageDialog(null, "Welcome, " + user.fullName + ".", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void launchAdminPanel() {
        try {
            Class<?> adminTestClass = Class.forName("admin.Test");
            adminTestClass.getMethod("main", String[].class).invoke(null, (Object) new String[0]);
        } catch (ReflectiveOperationException ex) {
            JOptionPane.showMessageDialog(this, "Admin panel could not be opened.", "Launch Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createCard(int x, int y, int width, int height) {
        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 242));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBounds(x, y, width, height);
        return card;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(Color.BLACK);
        return label;
    }

    private JTextField createField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_DARK);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_DARK);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT));
        return field;
    }

    private JButton createAccentButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(ACCENT);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createIconButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private BufferedImage loadBackgroundImage() {
        File imageFile = new File("src/assets/images/background.png");
        if (!imageFile.exists()) {
            return null;
        }
        try {
            return ImageIO.read(imageFile);
        } catch (IOException ex) {
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ignored) {
        }

        SwingUtilities.invokeLater(() -> new ProVehicleLogin().setVisible(true));
    }
}