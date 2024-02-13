package org.denys.hudymov.lab4.viewcontroller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private PasswordField passwordLoginText;
    @FXML
    private CheckBox passwordToggle;
    @FXML
    private Button loginBtn;
    @FXML
    private TextField usernameLoginText;

    private String password;

    @FXML
    private void toggleVisiblePassword(){
        if(passwordToggle.isSelected()) {
            password = passwordLoginText.getText();
            passwordLoginText.clear();
            passwordLoginText.setPromptText(password);
            return;
        }

        passwordLoginText.setText(password);
        passwordLoginText.setPromptText("Password");

    }

}
