package admin;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class RoundedComboBox<E> extends JComboBox<E> {
    public RoundedComboBox(E[] items, int radius) {
        super(items);
        setFont(AdminTheme.BODY_FONT.deriveFont(Font.PLAIN, 13f));
        setOpaque(false);
        setBackground(AdminTheme.BACKGROUND);
        setForeground(AdminTheme.TEXT_PRIMARY);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, AdminTheme.ACCENT),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        setMaximumRowCount(6);
        setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                list.setSelectionBackground(null);
                list.setSelectionForeground(AdminTheme.TEXT_PRIMARY);
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setOpaque(true);
                label.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10));
                label.setBackground(!isSelected ? new java.awt.Color(255, 255, 255) : AdminTheme.BACKGROUND);
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
                button.setMargin(new java.awt.Insets(0, 0, 0, 0));
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setContentAreaFilled(false);
                button.setOpaque(false);
                button.setFocusable(false);
                return button;
            }

            @Override
            protected java.awt.Insets getInsets() {
                return new java.awt.Insets(0, 10, 0, 10);
            }

            @Override
            public void paintCurrentValueBackground(Graphics g, java.awt.Rectangle bounds, boolean hasFocus) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AdminTheme.BACKGROUND);
                g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
                g2.dispose();
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.height = Math.max(0, size.height - 1);
        return size;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(AdminTheme.BACKGROUND);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}