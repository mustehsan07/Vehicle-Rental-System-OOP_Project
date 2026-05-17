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

        JButton exitBtn = createIconButton("X");
        exitBtn.setBounds(852, 10, 32, 32);
        exitBtn.addActionListener(e -> System.exit(0));
        mainPanel.add(exitBtn);

        JPanel heroPanel = new JPanel(null);
        heroPanel.setOpaque(false);
        heroPanel.setBounds(44, 150, 360, 230);

        JLabel headline = new JLabel("Join the Ride");
        headline.setFont(new Font("Segoe UI", Font.BOLD, 46));
        headline.setForeground(Color.WHITE);
        headline.setBounds(0, 0, 360, 60);
        heroPanel.add(headline);

        JLabel subtitle = new JLabel("Create a customer account in seconds");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(new Color(230, 235, 241));
        subtitle.setBounds(2, 58, 340, 24);
        heroPanel.add(subtitle);

        JLabel story = new JLabel("<html><div style='width:320px;'>Register once and keep your bookings, rentals, and history in one secure profile.</div></html>");
        story.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        story.setForeground(new Color(235, 240, 244));
        story.setBounds(2, 98, 330, 70);
        heroPanel.add(story);
        mainPanel.add(heroPanel);

        JPanel regCard = createCard(500, 50, 340, 530);
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
        nameField.setBounds(34, 122, 272, 34);
        regCard.add(nameField);

        JLabel emailLabel = createLabel("EMAIL");
        emailLabel.setBounds(34, 178, 100, 16);
        regCard.add(emailLabel);
        JTextField emailField = createField();
        emailField.setBounds(34, 196, 272, 34);
        regCard.add(emailField);

        JLabel passLabel = createLabel("PASSWORD");
        passLabel.setBounds(34, 252, 100, 16);
        regCard.add(passLabel);
        JPasswordField passField = createPasswordField();
        passField.setBounds(34, 270, 272, 34);
        regCard.add(passField);

        JButton registerBtn = createAccentButton("CREATE ACCOUNT");
        registerBtn.setBounds(34, 336, 272, 46);
        registerBtn.addActionListener(e -> handleRegistration(nameField, emailField, passField));
        regCard.add(registerBtn);

        JLabel instructions = new JLabel("<html><b>Instructions:</b><br>All fields are required.<br>Use a valid email address.</html>");
        instructions.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        instructions.setForeground(TEXT_MUTED);
        instructions.setBounds(34, 396, 280, 54);
        regCard.add(instructions);

        JButton backBtn = new JButton("← Back to Login");
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backBtn.setForeground(ACCENT);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setBounds(184, 466, 122, 22);
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