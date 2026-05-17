import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ProVehicleRegister extends JFrame {

    public ProVehicleRegister() {
        
        setTitle("Elite Vehicle Rentals - Registration");
        setSize(900, 650); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true); 
        setShape(new RoundRectangle2D.Double(0, 0, 900, 650, 30, 30));

       
        JPanel mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

               
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 31, 63), 900, 650, new Color(0, 102, 204));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
               
                g2d.setColor(new Color(255, 255, 255, 20));
                g2d.setFont(new Font("Arial", Font.BOLD, 100));
                g2d.drawString("SIGN UP", 50, 550);
            }
        };

        
        JPanel regCard = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 245)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        regCard.setOpaque(false);
        regCard.setBounds(480, 50, 370, 550); // Card position and size

        
        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(0, 51, 102));
        title.setBounds(40, 25, 260, 40);
        regCard.add(title);

        JLabel subTitle = new JLabel("Join our elite vehicle network today.");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subTitle.setForeground(Color.GRAY);
        subTitle.setBounds(40, 60, 300, 20);
        regCard.add(subTitle);

        
        int startY = 100;
        int spacing = 65;

        addLabelAndField(regCard, "FULL NAME", 40, startY);
        addLabelAndField(regCard, "EMAIL ADDRESS", 40, startY + spacing);
        addLabelAndField(regCard, "PHONE NUMBER", 40, startY + (spacing * 2));
        addLabelAndField(regCard, "USERNAME", 40, startY + (spacing * 3));
        
       
        JLabel passLabel = new JLabel("PASSWORD");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        passLabel.setBounds(40, startY + (spacing * 4), 100, 15);
        regCard.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(40, startY + (spacing * 4) + 15, 290, 30);
        passField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 102, 204)));
        regCard.add(passField);

      
        JButton registerBtn = new JButton("REGISTER NOW");
        registerBtn.setBounds(40, 440, 290, 45);
        registerBtn.setBackground(new Color(0, 102, 204));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerBtn.setFocusPainted(false);
        registerBtn.setBorderPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        regCard.add(registerBtn);

       
        JLabel instructions = new JLabel("<html>* All fields are mandatory. Use a strong password.</html>");
        instructions.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        instructions.setForeground(Color.GRAY);
        instructions.setBounds(40, 490, 290, 20);
        regCard.add(instructions);

        
        JButton backBtn = new JButton("← Back to Login");
        backBtn.setBounds(220, 515, 130, 25);
        backBtn.setForeground(new Color(0, 102, 204));
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        regCard.add(backBtn);

       
        JButton exitBtn = new JButton("X");
        exitBtn.setBounds(860, 10, 30, 30);
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setContentAreaFilled(false);
        exitBtn.setBorder(null);
        exitBtn.addActionListener(e -> System.exit(0));
        mainPanel.add(exitBtn);

       
        JLabel promoMsg = new JLabel("<html>Ready to<br><font color='#deff9a'>Drive?</font></html>");
        promoMsg.setFont(new Font("Segoe UI", Font.BOLD, 55));
        promoMsg.setForeground(Color.WHITE);
        promoMsg.setBounds(50, 180, 400, 150);
        mainPanel.add(promoMsg);

        mainPanel.add(regCard);
        add(mainPanel);
    }

    
    private void addLabelAndField(JPanel panel, String labelText, int x, int y) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setBounds(x, y, 150, 15);
        panel.add(lbl);

        JTextField txt = new JTextField();
        txt.setBounds(x, y + 15, 290, 30);
        txt.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 102, 204)));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(txt);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        SwingUtilities.invokeLater(() -> {
            new ProVehicleRegister().setVisible(true);
        });
    }
}