loginBtn.addActionListener {
    String user = userField.getText();
    String pass = new String(passField.getPassword());

    
    if (AuthController.handleLogin(user, pass)) {
        this.dispose();
    }
};