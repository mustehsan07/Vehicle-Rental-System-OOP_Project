package utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public final class AppTheme {
    // High-contrast dark premium theme
    public static final BufferedImage BACKGROUND_IMAGE = loadBackgroundImage();
    public static final Color BACKGROUND = Color.decode("#0B1220");
    public static final Color CARD = Color.decode("#111827");
    public static final Color CARD_ALT = Color.decode("#1F2937");
    public static final Color INPUT_BACKGROUND = Color.decode("#0F172A");
    public static final Color TABLE_HEADER_BACKGROUND = Color.decode("#334155");
    public static final Color TABLE_HEADER_FOREGROUND = Color.decode("#67E8F9");
    public static final Color ACCENT = Color.decode("#22D3EE");
    public static final Color ACCENT_HOVER = Color.decode("#06B6D4");
    public static final Color ACCENT_SOFT = Color.decode("#164E63");
    public static final Color TEXT_PRIMARY = Color.decode("#F8FAFC");
    public static final Color TEXT_SECONDARY = Color.decode("#94A3B8");
    public static final Color BORDER = Color.decode("#334155");
    public static final Color BORDER_SOFT = Color.decode("#1E293B");

    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font CHIP_FONT = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 13);

    public static final int RADIUS_LARGE = 38;
    public static final int RADIUS_MEDIUM = 26;
    public static final int RADIUS_SMALL = 18;

    private AppTheme() {
    }

    public static void paintBackground(Graphics2D g2, int width, int height) {
        if (g2 == null || width <= 0 || height <= 0) {
            return;
        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (BACKGROUND_IMAGE != null) {
            drawImageCover(g2, BACKGROUND_IMAGE, width, height);
        } else {
            g2.setPaint(new java.awt.GradientPaint(0, 0, BACKGROUND, width, height, CARD_ALT));
            g2.fillRect(0, 0, width, height);
        }

        g2.setComposite(java.awt.AlphaComposite.SrcOver.derive(0.62f));
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, width, height);
    }

    private static BufferedImage loadBackgroundImage() {
        File[] candidates = new File[] {
                new File(System.getProperty("user.dir"), "../assets/images/background.png"),
                new File(System.getProperty("user.dir"), "src/assets/images/background.png"),
                new File(System.getProperty("user.dir"), "../src/assets/images/background.png"),
                new File("src/assets/images/background.png"),
                new File("assets/images/background.png")
        };
        for (File imageFile : candidates) {
            if (imageFile.exists()) {
                try {
                    return ImageIO.read(imageFile);
                } catch (IOException ex) {
                    return null;
                }
            }
        }

        java.net.URL resource = AppTheme.class.getResource("/assets/images/background.png");
        if (resource != null) {
            try {
                return ImageIO.read(resource);
            } catch (IOException ex) {
                return null;
            }
        }
        return null;
    }

    private static void drawImageCover(Graphics2D g2, BufferedImage image, int targetWidth, int targetHeight) {
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
