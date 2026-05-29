package utils;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Shared table renderers/styles used across the app to keep tables consistent.
 */
public final class TableStyles {
    private TableStyles() {}

    public static class HeaderRenderer extends DefaultTableCellRenderer {
        public HeaderRenderer() {
            setOpaque(true);
            setBackground(AppTheme.TABLE_HEADER_BACKGROUND);
            setForeground(AppTheme.TABLE_HEADER_FOREGROUND);
            setFont(AppTheme.TABLE_HEADER_FONT);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 1, AppTheme.BORDER),
                    new EmptyBorder(0, 10, 0, 10)
            ));
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(LEFT);
            return label;
        }
    }

    /**
     * Default body cell renderer that draws the horizontal divider and uses
     * the app text color. Tables should still manage alternating row
     * backgrounds via `prepareRenderer` on the `JTable`.
     */
    public static class RowBodyRenderer extends DefaultTableCellRenderer {
        public RowBodyRenderer() {
            setOpaque(true);
            setForeground(AppTheme.TEXT_PRIMARY);
            setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER));
            setHorizontalAlignment(LEFT);
            setFont(AppTheme.BODY_FONT);
        }
    }

    public static class LeftAlignedRenderer extends RowBodyRenderer {
        public LeftAlignedRenderer() {
            setHorizontalAlignment(LEFT);
        }
    }

    public static class CenteredRenderer extends RowBodyRenderer {
        public CenteredRenderer() {
            setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        }
    }

    public static class RightAlignedRenderer extends RowBodyRenderer {
        public RightAlignedRenderer() {
            setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        }
    }
}
