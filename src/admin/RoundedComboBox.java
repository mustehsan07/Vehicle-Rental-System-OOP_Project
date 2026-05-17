import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class RoundedComboBox<E> extends JComboBox<E> {
    private final int radius;

    public RoundedComboBox(E[] items, int radius) {
        super(items);
        this.radius = radius;
        setFont(AdminTheme.BODY_FONT.deriveFont(Font.PLAIN, 13f));
        setOpaque(false);
        setBackground(AdminTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder());
        setMaximumRowCount(6);
        setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                list.setSelectionBackground(AdminTheme.BACKGROUND);
                list.setSelectionForeground(AdminTheme.TEXT_PRIMARY);
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setOpaque(true);
                label.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10));
                if (index < 0) {
                    label.setBackground(AdminTheme.BACKGROUND);
                    label.setForeground(AdminTheme.TEXT_PRIMARY);
                } else if (isSelected) {
                    label.setBackground(AdminTheme.ACCENT_SOFT);
                    label.setForeground(AdminTheme.TEXT_PRIMARY);
                } else {
                    label.setBackground(AdminTheme.CARD);
                    label.setForeground(AdminTheme.TEXT_PRIMARY);
                }
                label.setForeground(AdminTheme.TEXT_PRIMARY);
                return label;
            }
        });
        setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new java.awt.Color(0, 0, 0, 0));
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.setColor(AdminTheme.TEXT_SECONDARY);
                        int midX = getWidth() / 2;
                        int midY = getHeight() / 2 - 1;
                        g2.drawLine(midX - 5, midY - 2, midX, midY + 3);
                        g2.drawLine(midX, midY + 3, midX + 5, midY - 2);
                        g2.dispose();
                    }
                };
                button.setPreferredSize(new Dimension(28, 28));
                button.setMinimumSize(new Dimension(28, 28));
                button.setMaximumSize(new Dimension(28, 28));
                button.setMargin(new Insets(0, 0, 0, 0));
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setContentAreaFilled(false);
                button.setOpaque(false);
                button.setFocusable(false);
                return button;
            }

            @Override
            protected Insets getInsets() {
                return new Insets(0, 10, 0, 10);
            }

            @Override
            public void paintCurrentValueBackground(Graphics g, java.awt.Rectangle bounds, boolean hasFocus) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AdminTheme.INPUT_BACKGROUND);
                g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, radius, radius);
                g2.dispose();
            }
        });
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

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.height = Math.max(0, size.height - 3);
        return size;
    }
}