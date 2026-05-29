package utils;

/**
 * Central SMTP configuration for email notifications.
 * Update these values before enabling mail sending in the app.
 */
public final class SmtpConfig {
    private SmtpConfig() {}

    // Read from environment variables with safe fallbacks.
    public static final String SMTP_HOST = getenvOrDefault("SMTP_HOST", "smtp-relay.brevo.com");
    public static final int SMTP_PORT = getenvIntOrDefault("SMTP_PORT", 587);
    // Do NOT keep real credentials here. Fall back to empty so environment must provide them.
    public static final String SMTP_USERNAME = getenvOrDefault("SMTP_USERNAME", "");
    public static final String SMTP_PASSWORD = getenvOrDefault("SMTP_PASSWORD", "");
    public static final String SMTP_FROM = getenvOrDefault("SMTP_FROM", "noreply@example.com");

    public static final boolean SMTP_AUTH = getenvBoolOrDefault("SMTP_AUTH", true);
    public static final boolean SMTP_STARTTLS = getenvBoolOrDefault("SMTP_STARTTLS", true);

    /**
     * Comma-separated list of admin notification emails in env var `ADMIN_NOTIFICATION_EMAILS`.
     * Falls back to the single default address if not set.
     */
    public static final String[] ADMIN_NOTIFICATION_EMAILS = getenvArrayOrDefault("ADMIN_NOTIFICATION_EMAILS", new String[0]);

    private static String getenvOrDefault(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.isEmpty()) ? def : v;
    }

    private static int getenvIntOrDefault(String key, int def) {
        String v = System.getenv(key);
        if (v == null || v.isEmpty()) return def;
        try { return Integer.parseInt(v.trim()); } catch (NumberFormatException ex) { return def; }
    }

    private static boolean getenvBoolOrDefault(String key, boolean def) {
        String v = System.getenv(key);
        return (v == null || v.isEmpty()) ? def : Boolean.parseBoolean(v.trim());
    }

    private static String[] getenvArrayOrDefault(String key, String[] def) {
        String v = System.getenv(key);
        if (v == null || v.isEmpty()) return def;
        String[] parts = v.split(",");
        for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();
        return parts;
    }
}