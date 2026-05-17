import javax.swing.JOptionPane;
import java.util.HashMap;

public class AuthController {


    private static HashMap<String, String> userDatabase = new HashMap<>();

    static {
       
        userDatabase.put("admin", "1234");
        userDatabase.put("zain", "9250059");
    }

 
    public static boolean handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please fill all fields!");
            return false;
        }

        if (userDatabase.containsKey(username)) {
            if (userDatabase.get(username).equals(password)) {
                showSuccess("Login Successful! Welcome " + username);
                return true;
            } else {
                showError("Invalid Password!");
            }
        } else {
            showError("User not found!");
        }
        return false;
    }

   
    public static boolean handleRegistration(String name, String email, String username, String password) {
      
        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showError("All fields are mandatory!");
            return false;
        }

        if (userDatabase.containsKey(username)) {
            showError("Username already exists! Try another one.");
            return false;
        }

       
        userDatabase.put(username, password);
        showSuccess("Account Created Successfully for " + name);
        return true;
    }

    
    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Authentication Error", JOptionPane.ERROR_MESSAGE);
    }

    private static void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}