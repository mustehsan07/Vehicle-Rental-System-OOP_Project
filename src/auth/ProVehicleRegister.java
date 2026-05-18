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

public class ProVehicleRegister extends JFrame {
    private static final Color ACCENT = new Color(0x0F766E);
    private static final Color BACKGROUND = new Color(0xE9EEF3);
    private static final Color TEXT_DARK = new Color(0x0F172A);
    private static final Color TEXT_MUTED = new Color(0x64748B);

    private final BufferedImage backgroundImage;

    public ProVehicleRegister() {
        backgroundImage = loadBackgroundImage();

        setTitle("Customer Signup");
        setSize(900, 650);
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
                    g2.setPaint(new java.awt.GradientPaint(0, 0, new Color(8, 31, 61), getWidth(), getHeight(), new Color(15, 118, 110)));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                g2.setComposite(AlphaComposite.SrcOver.derive(0.62f));
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        JPanel heroPanel = new JPanel(null);
        heroPanel.setOpaque(false);
        heroPanel.setBounds(150, 220, 880, 320);

        JLabel headline = new JLabel("<html><div style='width:100%; line-height:0.5;'>Create Customer Account</div></html>");
        headline.setFont(new Font("Segoe UI", Font.BOLD, 54));
        headline.setForeground(Color.WHITE);
        headline.setBounds(0, 0, 720, 150);
        heroPanel.add(headline);

        JLabel subtitle = new JLabel("Customer account creation in one clean portal");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitle.setForeground(new Color(230, 235, 241));
        subtitle.setBounds(2, 150, 480, 28);
        heroPanel.add(subtitle);

        JLabel story = new JLabel("<html><div style='width:360px;'>Register once and keep your bookings, rentals, and history in one secure profile.</div></html>");
        story.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        story.setForeground(new Color(235, 240, 244));
        story.setBounds(2, 180, 480, 76);
        heroPanel.add(story);
        mainPanel.add(heroPanel);

        JPanel regCard = createCard(900, 150, 380, 530);
        regCard.setLayout(null);

        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_DARK);
        title.setBounds(34, 24, 260, 36);
        regCard.add(title);

        JLabel subTitle = new JLabel("Customer signup only");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subTitle.setForeground(TEXT_MUTED);
        subTitle.setBounds(34, 58, 260, 18);
        regCard.add(subTitle);

        JLabel nameLabel = createLabel("FULL NAME");
        nameLabel.setBounds(34, 104, 100, 16);
        regCard.add(nameLabel);
        JTextField nameField = createField();
        nameField.setBounds(34, 122, 312, 36);
        regCard.add(nameField);

        JLabel emailLabel = createLabel("EMAIL");
        emailLabel.setBounds(34, 188, 100, 16);
        regCard.add(emailLabel);
        JTextField emailField = createField();
        emailField.setBounds(34, 206, 312, 36);
        regCard.add(emailField);

        JLabel passLabel = createLabel("PASSWORD");
        passLabel.setBounds(34, 272, 100, 16);
        regCard.add(passLabel);
        JPasswordField passField = createPasswordField();
        passField.setBounds(34, 290, 312, 36);
        regCard.add(passField);

        JButton registerBtn = createRoundedAccentButton("CREATE ACCOUNT");
        registerBtn.setBounds(34, 370, 312, 48);
        registerBtn.addActionListener(e -> handleRegistration(nameField, emailField, passField));
        regCard.add(registerBtn);

        JLabel instructions = new JLabel("<html><b>Instructions:</b><br>All fields are required.<br>Use a valid email address.</html>");
        instructions.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        instructions.setForeground(TEXT_MUTED);
        instructions.setBounds(34, 434, 312, 54);
        regCard.add(instructions);

        JButton backBtn = createRoundedLinkButton("Back to Login");
        backBtn.setBounds(115, 490, 146, 24);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.addActionListener(e -> {
            new ProVehicleLogin().setVisible(true);
            dispose();
        });
        regCard.add(backBtn);

        mainPanel.add(regCard);
        add(mainPanel);
    }

    private void handleRegistration(JTextField nameField, JTextField emailField, JPasswordField passField) {
        String fullName = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passField.getPassword());

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Signup Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Enter a valid email address.", "Signup Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!AuthController.registerCustomer(fullName, email, password)) {
            JOptionPane.showMessageDialog(this, "Email already exists or fields are invalid.", "Signup Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Customer account created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new ProVehicleLogin().setVisible(true);
    }

    private JPanel createCard(int x, int y, int width, int height) {
        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
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
        field.setBackground(BACKGROUND);
        field.setForeground(TEXT_DARK);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(BACKGROUND);
        field.setForeground(TEXT_DARK);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT));
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
                g2.setColor(new Color(15, 118, 110));
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
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(ACCENT);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
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
            if (!imageFile.exists()) {
                continue;
            }
            try {
                return ImageIO.read(imageFile);
            } catch (IOException ex) {
                return null;
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