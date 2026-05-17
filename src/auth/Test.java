package auth;

public class Test {
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ignored) {
        }

        ensureAuthClassesCompiled();

        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                java.net.URL parentDirectory = new java.io.File("..").toURI().toURL();
                try (java.net.URLClassLoader loader = new java.net.URLClassLoader(new java.net.URL[]{parentDirectory}, Test.class.getClassLoader())) {
                    Class<?> loginClass = Class.forName("auth.ProVehicleLogin", true, loader);
                    javax.swing.JFrame frame = (javax.swing.JFrame) loginClass.getConstructor().newInstance();
                    frame.setVisible(true);
                }
            } catch (ReflectiveOperationException | java.io.IOException ex) {
                throw new IllegalStateException("Unable to start login screen.", ex);
            }
        });
    }

    private static void ensureAuthClassesCompiled() {
        javax.tools.JavaCompiler compiler = javax.tools.ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("A JDK is required to launch the auth screens.");
        }

        int exitCode = compiler.run(
                null,
                null,
                null,
                "-d",
                "..",
                "AuthController.java",
                "ProVehicleLogin.java",
                "ProVehicleRegister.java"
        );

        if (exitCode != 0) {
            throw new IllegalStateException("Unable to compile the auth screens.");
        }
    }
}