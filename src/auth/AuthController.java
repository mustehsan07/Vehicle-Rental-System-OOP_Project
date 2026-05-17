package auth;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public final class AuthController {
    private AuthController() {
    }

    public static final class AuthRecord {
        public final String id;
        public final String fullName;
        public final String email;
        public final String password;
        public final String status;
        public final String role;

        public AuthRecord(String id, String fullName, String email, String password, String status, String role) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.status = status;
            this.role = role;
        }
    }

    public static AuthRecord authenticate(String identifier, String password) {
        if (identifier == null || password == null) {
            return null;
        }

        String normalizedIdentifier = identifier.trim();
        String normalizedPassword = password;
        if (normalizedIdentifier.isEmpty() || normalizedPassword.isEmpty()) {
            return null;
        }

        for (Object user : getUsers()) {
            if (matchesIdentifier(user, normalizedIdentifier) && normalizedPassword.equals(readField(user, "password"))) {
                return new AuthRecord(
                        readField(user, "id"),
                        readField(user, "fullName"),
                        readField(user, "email"),
                        readField(user, "password"),
                        readField(user, "status"),
                        readField(user, "role")
                );
            }
        }
        return null;
    }

    public static boolean registerCustomer(String fullName, String email, String password) {
        String trimmedName = safeTrim(fullName);
        String trimmedEmail = safeTrim(email);
        String trimmedPassword = safeTrim(password);

        if (trimmedName.isEmpty() || trimmedEmail.isEmpty() || trimmedPassword.isEmpty()) {
            return false;
        }

        for (Object user : getUsers()) {
            if (trimmedEmail.equalsIgnoreCase(readField(user, "email"))) {
                return false;
            }
        }

        addUser(createUserRow(nextUserId(), trimmedName, trimmedEmail, trimmedPassword, "Active", "customer"));
        return true;
    }

    private static boolean matchesIdentifier(Object user, String identifier) {
        return identifier.equalsIgnoreCase(readField(user, "email"));
    }

    private static String nextUserId() {
        int nextNumber = getUsers().size() + 1;
        while (containsUserId(String.format("U%03d", nextNumber))) {
            nextNumber++;
        }
        return String.format("U%03d", nextNumber);
    }

    private static boolean containsUserId(String userId) {
        for (Object user : getUsers()) {
            if (userId.equalsIgnoreCase(readField(user, "id"))) {
                return true;
            }
        }
        return false;
    }

    private static List<?> getUsers() {
        try {
            Class<?> mainClass = Class.forName("app.MainClass");
            Method method = mainClass.getMethod("getUsers");
            return (List<?>) method.invoke(null);
        } catch (ReflectiveOperationException ex) {
            return Collections.emptyList();
        }
    }

    private static void addUser(Object userRow) {
        try {
            Class<?> mainClass = Class.forName("app.MainClass");
            Class<?> userRowClass = Class.forName("app.MainClass$UserRow");
            Method method = mainClass.getMethod("addUser", userRowClass);
            method.invoke(null, userRow);
        } catch (ReflectiveOperationException ignored) {
        }
    }

    private static Object createUserRow(String id, String fullName, String email, String password, String status, String role) {
        try {
            Class<?> userRowClass = Class.forName("app.MainClass$UserRow");
            Constructor<?> constructor = userRowClass.getConstructor(String.class, String.class, String.class, String.class, String.class, String.class);
            return constructor.newInstance(id, fullName, email, password, status, role);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Unable to access shared user store.", ex);
        }
    }

    private static String readField(Object target, String fieldName) {
        try {
            Field field = target.getClass().getField(fieldName);
            Object value = field.get(target);
            return value == null ? "" : String.valueOf(value);
        } catch (ReflectiveOperationException ex) {
            return "";
        }
    }

    private static String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }
}