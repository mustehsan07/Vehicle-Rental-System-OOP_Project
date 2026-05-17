import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ProVehicleLogin extends JFrame {

    public ProVehicleLogin() {
        setTitle("Elite Vehicle Rentals - Access Portal");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true); 
        setShape(new RoundRectangle2D.Double(0, 0, 900, 600, 30, 30)); // Rounded Corners for Frame

        
        JPanel mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 31, 63), 900, 600, new Color(0, 102, 204));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
              
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.setFont(new Font("Arial", Font.BOLD, 120));
                g2d.drawString("RENTAL", 50, 500);
            }
        };

      
        JPanel loginCard = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 240)); // Solid White with tiny transparency
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        loginCard.setOpaque(false);
        loginCard.setBounds(500, 80, 340, 440);

     
        JLabel welcome = new JLabel("Welcome Back!");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcome.setForeground(new Color(0, 51, 102));
        welcome.setBounds(40, 30, 260, 40);
        loginCard.add(welcome);

        JLabel subTitle = new JLabel("Vehicle Rental Management System");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subTitle.setForeground(Color.GRAY);
        subTitle.setBounds(40, 65, 260, 20);
        loginCard.add(subTitle);

      
        JLabel userLabel = new JLabel("USERNAME");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        userLabel.setBounds(40, 110, 100, 20);
        loginCard.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(40, 130, 260, 35);
        userField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 102, 204)));
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginCard.add(userField);

        JLabel passLabel = new JLabel("PASSWORD");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        passLabel.setBounds(40, 190, 100, 20);
        loginCard.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(40, 210, 260, 35);
        passField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 102, 204)));
        loginCard.add(passField);

       
        JButton loginBtn = new JButton("LOGIN SYSTEM");
        loginBtn.setBounds(40, 280, 260, 45);
        loginBtn.setBackground(new Color(0, 102, 204));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginCard.add(loginBtn);

      
        JLabel instructions = new JLabel("<html><body style='width: 200px;'>"
                + "<b>Instructions:</b><br>"
                + "1. Use Admin ID for fleet access.<br>"
                + "2. Contact IT for password reset."
                + "</body></html>");
        instructions.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        instructions.setForeground(new Color(80, 80, 80));
        instructions.setBounds(40, 340, 260, 60);
        loginCard.add(instructions);

        
        JButton nextBtn = new JButton("Next →");
        nextBtn.setBounds(240, 400, 80, 25);
        nextBtn.setForeground(new Color(0, 102, 204));
        nextBtn.setContentAreaFilled(false);
        nextBtn.setBorderPainted(false);
        nextBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginCard.add(nextBtn);

       
        JButton exitBtn = new JButton("X");
        exitBtn.setBounds(860, 10, 30, 30);
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setContentAreaFilled(false);
        exitBtn.setBorder(null);
        exitBtn.addActionListener(e -> System.exit(0));
        mainPanel.add(exitBtn);

      
        JLabel promoMsg = new JLabel("<html>Elite Fleet<br>Management</html>");
        promoMsg.setFont(new Font("Segoe UI", Font.BOLD, 48));
        promoMsg.setForeground(Color.WHITE);
        promoMsg.setBounds(50, 150, 400, 150);
        mainPanel.add(promoMsg);

        mainPanel.add(loginCard);
        add(mainPanel);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        SwingUtilities.invokeLater(() -> {
            new ProVehicleLogin().setVisible(true);
        });
    }
}