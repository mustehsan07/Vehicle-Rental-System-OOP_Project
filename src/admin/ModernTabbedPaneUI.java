package admin;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class ModernTabbedPaneUI extends BasicTabbedPaneUI {

    @Override
    protected void installDefaults() {
        super.installDefaults();
        tabAreaInsets.bottom = 12;
        contentBorderInsets.top = 0;
        contentBorderInsets.left = 0;
        contentBorderInsets.right = 0;
        contentBorderInsets.bottom = 0;
        selectedTabPadInsets = new Insets(0, 0, 0, 0);
        tabRunOverlay = 0;
        tabInsets.left = 18;
        tabInsets.right = 18;
        tabInsets.top = 10;
        tabInsets.bottom = 10;
    }

    @Override
    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
        // no default square border
    }

    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        return 140;
    }

    @Override
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        return 40;
    }

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
                                      int x, int y, int w, int h, boolean isSelected) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(isSelected ? AdminTheme.ACCENT_SOFT : AdminTheme.CARD_ALT);
        g2.fillRoundRect(x + 2, y + 2, w - 4, h - 4, AdminTheme.RADIUS_SMALL, AdminTheme.RADIUS_SMALL);
        g2.dispose();
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                                  int x, int y, int w, int h, boolean isSelected) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(isSelected ? AdminTheme.ACCENT : AdminTheme.BORDER);
        g2.drawRoundRect(x + 2, y + 2, w - 5, h - 5, AdminTheme.RADIUS_SMALL, AdminTheme.RADIUS_SMALL);
        g2.dispose();
    }

    @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex,
                                       Rectangle iconRect, Rectangle textRect, boolean isSelected) {
        // no focus ring
    }

    @Override
    protected void setRolloverTab(int index) {
        super.setRolloverTab(index);
    }

    @Override
    protected void paintText(Graphics g, int tabPlacement, java.awt.Font font, java.awt.FontMetrics metrics,
                             int tabIndex, String title, java.awt.Rectangle textRect, boolean isSelected) {
        g.setColor(isSelected ? AdminTheme.ACCENT : AdminTheme.TEXT_PRIMARY);
        super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
    }
}