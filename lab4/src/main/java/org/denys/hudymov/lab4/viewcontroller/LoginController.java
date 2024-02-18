package org.denys.hudymov.lab4.viewcontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.Data;
import net.synedra.validatorfx.Validator;
import org.denys.hudymov.lab4.repository.UserRepository;
import org.denys.hudymov.lab4.repository.impl.UserRepositoryImpl;

import static org.denys.hudymov.lab4.utilities.FxUtilities.displayInformationPopup;
import static org.denys.hudymov.lab4.utilities.FxUtilities.switchScene;

@Data
public class LoginController {

    private UserRepository userRepository = new UserRepositoryImpl();

    private Validator validator = new Validator();

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
    private void initialize() {
        // Validate username
        validator.createCheck()
                .dependsOn("username", usernameLoginText.textProperty())
                .withMethod(field -> {
                    var userName = usernameLoginText.getText();
                    if (userName.isEmpty()) {
                        field.error("username can't be empty!");
                    }
                })
                .decorates(usernameLoginText)
                .immediate();

        // Validate password
        validator.createCheck()
                .dependsOn("password", passwordLoginText.textProperty())
                .withMethod(field -> {
                    var userName = passwordLoginText.getText();
                    if (userName.isEmpty()) {
                        field.error("password can't be empty!");
                    }
                })
                .decorates(passwordLoginText)
                .immediate();
    }


    @FXML
    private void toggleVisiblePassword() {
        if (passwordToggle.isSelected()) {
            password = passwordLoginText.getText();
            passwordLoginText.clear();
            passwordLoginText.setPromptText(password);
            return;
        }

        passwordLoginText.setText(password);
        passwordLoginText.setPromptText("Password");
    }


    /**
     * Authentication by the name and password from log in page
     */
    @FXML
    private void authenticate(ActionEvent event) {
        var password = passwordLoginText.getText();
        var username = usernameLoginText.getText();
        if (password.isEmpty() && username.isEmpty()) {
            displayInformationPopup("you violated text fields constraints!", Alert.AlertType.ERROR);
            return;
        }
        if (userRepository.findUserRoleByNameAndPassword(username, password).isPresent()) {
            switchScene(event, "main-view.fxml", "Main");
        }
    }

}
