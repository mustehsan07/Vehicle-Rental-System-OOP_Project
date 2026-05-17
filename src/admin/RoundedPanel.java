import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class RoundedPanel extends JPanel {
    private final int radius;
    private final Color borderColor;
    private final int borderWidth;

    public RoundedPanel(Color background, int radius) {
        this(background, radius, null, 0);
    }

    public RoundedPanel(Color background, int radius, Color borderColor, int borderWidth) {
        this.radius = radius;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
        setBackground(background);
        setOpaque(false);
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
        if (borderColor == null || borderWidth <= 0) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(borderColor);
        g2.drawRoundRect(borderWidth / 2, borderWidth / 2,
                getWidth() - borderWidth, getHeight() - borderWidth, radius, radius);
        g2.dispose();
    }
}