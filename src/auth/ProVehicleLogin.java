package auth;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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

public class ProVehicleLogin extends JFrame {
    private static final Color ACCENT = Color.decode("#0F766E");
    private static final Color ACCENT_HOVER = Color.decode("#115E59");
    private static final Color BACKGROUND = Color.decode("#E9EEF3");
    private static final Color CARD = Color.WHITE;
    private static final Color INPUT_BACKGROUND = Color.decode("#F1F7F7");
    private static final Color TEXT_DARK = Color.decode("#0F172A");
    private static final Color TEXT_MUTED = Color.decode("#64748B");
    private static final Color BORDER = Color.decode("#DCE3E7");
    private static final Color CARD_ALT = Color.decode("#F8FAFB");
    private static final int RADIUS_LARGE = 38;

    private final BufferedImage backgroundImage;

    public ProVehicleLogin() {
        backgroundImage = loadBackgroundImage();

        setTitle("Vehicle Rental Management System");
        setSize(1200, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        JPanel mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (backgroundImage != null) {
                    drawImageCover(g2, backgroundImage, getWidth(), getHeight());
                } else {
                    g2.setPaint(new java.awt.GradientPaint(0, 0, BACKGROUND, getWidth(), getHeight(), CARD_ALT));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                g2.setComposite(AlphaComposite.SrcOver.derive(0.62f));
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        mainPanel.setBackground(BACKGROUND);

        JPanel heroPanel = new JPanel(null);
        heroPanel.setOpaque(false);
        heroPanel.setBounds(150, 220, 880, 320);

        JLabel headline = new JLabel("<html><div style='width:100%; line-height:0.5;'>Vehicle Rental Management System</div></html>");
        headline.setFont(new Font("Segoe UI", Font.BOLD, 54));
        headline.setForeground(Color.WHITE);
        headline.setBounds(0, 0, 720, 150);
        heroPanel.add(headline);

        JLabel subtitle = new JLabel("Customer and admin access in one clean portal");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitle.setForeground(new Color(230, 235, 241));
        subtitle.setBounds(2, 150, 480, 28);
        heroPanel.add(subtitle);

        JLabel story = new JLabel("<html><div style='width:360px;'>Manage vehicles, customer accounts, and rental flow from a focused dashboard designed for speed.</div></html>");
        story.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        story.setForeground(new Color(235, 240, 244));
        story.setBounds(2, 180, 480, 76);
        heroPanel.add(story);

        mainPanel.add(heroPanel);

        JPanel loginCard = createCard(900, 150, 380, 450);
        loginCard.setLayout(null);

        JLabel welcome = new JLabel("Welcome Back!");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcome.setForeground(TEXT_DARK);
        welcome.setBounds(34, 28, 280, 40);
        loginCard.add(welcome);

        JLabel subTitle = new JLabel("Vehicle Rental Management System");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitle.setForeground(TEXT_MUTED);
        subTitle.setBounds(34, 62, 260, 18);
        loginCard.add(subTitle);

        JLabel emailLabel = createLabel("EMAIL");
        emailLabel.setBounds(34, 108, 100, 16);
        loginCard.add(emailLabel);

        JTextField emailField = createField();
        emailField.setBounds(34, 126, 312, 36);
        loginCard.add(emailField);

        JLabel passLabel = createLabel("PASSWORD");
        passLabel.setBounds(34, 182, 100, 16);
        loginCard.add(passLabel);

        JPasswordField passField = createPasswordField();
        passField.setBounds(34, 200, 312, 36);
        loginCard.add(passField);

        JButton loginBtn = createRoundedAccentButton("LOGIN");
        loginBtn.setBounds(34, 270, 312, 48);
        loginBtn.addActionListener(e -> handleLogin(emailField, passField));
        loginCard.add(loginBtn);

        JLabel instructions = new JLabel("<html><b>Instructions:</b><br>Use your email to sign in.<br>Customer accounts can be created from signup.</html>");
        instructions.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        instructions.setForeground(TEXT_MUTED);
        instructions.setBounds(34, 330, 312, 54);
        loginCard.add(instructions);

        JButton nextBtn = createRoundedLinkButton("Create Account");
        nextBtn.setBounds(100, 400, 146, 24);
        nextBtn.addActionListener(e -> {
            new ProVehicleRegister().setVisible(true);
            dispose();
        });
        loginCard.add(nextBtn);

        mainPanel.add(loginCard);
        add(mainPanel);
    }

    private void handleLogin(JTextField emailField, JPasswordField passField) {
        String identifier = emailField.getText().trim();
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
                g2.setColor(CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS_LARGE, RADIUS_LARGE);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS_LARGE, RADIUS_LARGE);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBounds(x, y, width, height);
        return card;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_DARK);
        return label;
    }

    private JTextField createField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(INPUT_BACKGROUND);
        field.setForeground(TEXT_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(INPUT_BACKGROUND);
        field.setForeground(TEXT_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        return field;
    }

    private JButton createRoundedAccentButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ACCENT);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ACCENT_HOVER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
            }
        };
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createRoundedLinkButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(ACCENT);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setBackground(new Color(0, 0, 0, 0));
        button.setMargin(new java.awt.Insets(0, 0, 0, 0));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private BufferedImage loadBackgroundImage() {
        File[] candidates = new File[] {
                new File(System.getProperty("user.dir"), "../assets/images/background.png"),
                new File(System.getProperty("user.dir"), "src/assets/images/background.png"),
                new File(System.getProperty("user.dir"), "../src/assets/images/background.png"),
                new File("src/assets/images/background.png"),
                new File("assets/images/background.png")
        };
        for (File imageFile : candidates) {
            if (imageFile.exists()) {
                try {
                    return ImageIO.read(imageFile);
                } catch (IOException ex) {
                    return null;
                }
            }
        }

        java.net.URL resource = getClass().getResource("/assets/images/background.png");
        if (resource != null) {
            try {
                return ImageIO.read(resource);
            } catch (IOException ex) {
                return null;
            }
        }
        return null;
    }

    private void drawImageCover(Graphics2D g2, BufferedImage image, int targetWidth, int targetHeight) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        if (imageWidth <= 0 || imageHeight <= 0 || targetWidth <= 0 || targetHeight <= 0) {
            return;
        }

        double scale = Math.max((double) targetWidth / imageWidth, (double) targetHeight / imageHeight);
        int scaledWidth = (int) Math.round(imageWidth * scale);
        int scaledHeight = (int) Math.round(imageHeight * scale);
        int x = (targetWidth - scaledWidth) / 2;
        int y = (targetHeight - scaledHeight) / 2;
        g2.drawImage(image, x, y, scaledWidth, scaledHeight, null);
    }

}