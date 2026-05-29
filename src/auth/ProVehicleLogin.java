package auth;

import customer.CustomerDashboard;
import data.CustomerData;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import model.Customer;
import utils.AppTheme;

public class ProVehicleLogin extends JFrame {
    private static final Color ACCENT = AppTheme.ACCENT;
    private static final Color ACCENT_HOVER = AppTheme.ACCENT_HOVER;
    private static final Color BACKGROUND = AppTheme.BACKGROUND;
    private static final Color CARD = AppTheme.CARD;
    private static final Color INPUT_BACKGROUND = AppTheme.INPUT_BACKGROUND;
    private static final Color TEXT_DARK = AppTheme.TEXT_PRIMARY;
    private static final Color TEXT_MUTED = AppTheme.TEXT_SECONDARY;
    private static final Color BORDER = AppTheme.BORDER;
    private static final int RADIUS_LARGE = 38;

    public ProVehicleLogin() {
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
                AppTheme.paintBackground(g2, getWidth(), getHeight());
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
        subtitle.setForeground(new Color(203, 213, 225));
        subtitle.setBounds(2, 150, 480, 28);
        heroPanel.add(subtitle);

        JLabel story = new JLabel("<html><div style='width:360px;'>Manage vehicles, customer accounts, and rental flow from a focused dashboard designed for speed.</div></html>");
        story.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        story.setForeground(new Color(148, 163, 184));
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
        JToggleButton passwordToggle = createPasswordToggleButton(passField);
        passwordToggle.setBounds(246, 176, 100, 22);
        loginCard.add(passwordToggle);
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
        nextBtn.setBounds(110, 400, 146, 24);
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
            launchCustomerPanel(user);
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

    private void launchCustomerPanel(AuthController.AuthRecord user) {
        Customer customer = CustomerData.findByEmail(user.email);
        if (customer == null) {
            JOptionPane.showMessageDialog(this, "Customer panel could not be opened.", "Launch Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new CustomerDashboard(customer).setVisible(true);
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
        field.setCaretColor(Color.WHITE);
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
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        field.setEchoChar('\u2022');
        return field;
    }

    private JToggleButton createPasswordToggleButton(JPasswordField passwordField) {
        JToggleButton toggleButton = new JToggleButton();
        toggleButton.setText("Show");
        toggleButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        toggleButton.setForeground(ACCENT);
        toggleButton.setBackground(new Color(0, 0, 0, 0));
        toggleButton.setOpaque(false);
        toggleButton.setBorder(BorderFactory.createEmptyBorder());
        toggleButton.setFocusPainted(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleButton.addActionListener(e -> {
            if (toggleButton.isSelected()) {
                passwordField.setEchoChar((char) 0);
                toggleButton.setText("Hide");
            } else {
                passwordField.setEchoChar('\u2022');
                toggleButton.setText("Show");
            }
        });
        return toggleButton;
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

}