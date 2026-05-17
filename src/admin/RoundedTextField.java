import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JTextField;

public class RoundedTextField extends JTextField {
    private final int radius;

    public RoundedTextField(int radius) {
        this.radius = radius;
        setOpaque(false);
        setBackground(AdminTheme.BACKGROUND);
        setForeground(AdminTheme.TEXT_PRIMARY);
        setFont(AdminTheme.BODY_FONT.deriveFont(Font.PLAIN, 13f));
        setCaretColor(AdminTheme.ACCENT);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
        setPreferredSize(new java.awt.Dimension(0, 40));
    }

    @Override
    public Insets getInsets() {
        return new Insets(8, 12, 8, 12);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // no border
    }
}