package org.denys.hudymov.lab4.viewcontroller;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.Data;
import org.denys.hudymov.lab4.entity.User;
import org.denys.hudymov.lab4.enums.Role;
import org.denys.hudymov.lab4.repository.UserRepository;
import org.denys.hudymov.lab4.repository.impl.UserRepositoryImpl;
import org.denys.hudymov.lab4.utilities.FxUtilities;

import static org.denys.hudymov.lab4.utilities.FxUtilities.displayInformationPopup;

@Data
public class MainController {
    private UserRepository userRepository = new UserRepositoryImpl();

    @FXML
    private TextField pwField;

    @FXML
    private TextField surnameField;

    @FXML
    private Button deleteUserBtn;

    @FXML
    private Button findUserBtn;

    @FXML
    private Button saveUserBtn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField userIdField;

    @FXML
    private ComboBox<Role> roleBox;

    @FXML
    private Button updateUserBtn;

    @FXML
    TableView<User> userTable;

    @FXML
    private TableColumn<User, Long> userId;

    @FXML
    private TableColumn<User, String> userName;

    @FXML
    private TableColumn<User, String> userPassword;

    @FXML
    private TableColumn<User, Role> userRole;

    @FXML
    private TableColumn<User, String> userSurname;

    @FXML
    private void initialize() {
        roleBox.getItems().setAll(Role.values());
        populateUserTable();
    }

    @FXML
    private void populateUserTable() {
        Task<List<User>> task = new Task<>() {
            @Override
            protected List<User> call() throws Exception {
                return userRepository.read(); // Data fetching in background thread
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    userTable.getItems().setAll(getValue()); // UI update on main thread
                });
            }
        };

        new Thread(task).start(); // Launch the task on a separate thread
    }

    @FXML
    void deleteUserById() {
        var id = userIdField.getText();

        if (id.isEmpty()) {
            displayInformationPopup("User id is empty!", Alert.AlertType.ERROR);
            return;
        }

        try {
            userRepository.deleteById(Long.valueOf(id));
        } catch (IllegalArgumentException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            FxUtilities.displayInformationPopup("Something went wrong with query to database!",
                    Alert.AlertType.ERROR);
            return;
        }
        FxUtilities.displayInformationPopup("The user was deleted!", Alert.AlertType.INFORMATION);
        populateUserTable();
    }

    @FXML
    void findUserById() {
        var id = userIdField.getText();
        Optional<User> user;

        if (id.isEmpty()) {
            displayInformationPopup("User id is empty!", Alert.AlertType.ERROR);
            return;
        }

        try {
            user = userRepository.findById(Long.valueOf(id));
        } catch (IllegalArgumentException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        user.ifPresentOrElse(u -> {
                    nameField.setText(u.getName());
                    surnameField.setText(u.getLastName());
                    pwField.setText(u.getPassword());
                    roleBox.getSelectionModel().select(u.getRole());
                },
                () -> FxUtilities.displayInformationPopup("User not found", Alert.AlertType.INFORMATION));
    }

    @FXML
    void updateUser() {
        var id = userIdField.getText();
        var name = nameField.getText();
        var lastName = surnameField.getText();
        var password = pwField.getText();
        var role = roleBox.getValue();

        if (id.isEmpty()) {
            displayInformationPopup("User id is empty!", Alert.AlertType.ERROR);
            return;
        }
        if (name.isEmpty()) {
            displayInformationPopup("User name is empty!", Alert.AlertType.ERROR);
            return;
        }
        if (lastName.isEmpty()) {
            displayInformationPopup("User surname is empty!", Alert.AlertType.ERROR);
            return;
        }
        if (password.isEmpty()) {
            displayInformationPopup("User password is empty!", Alert.AlertType.ERROR);
            return;
        }
        if (role == null) {
            displayInformationPopup("User Role not specified!", Alert.AlertType.ERROR);
            return;
        }

        User user = User.builder()
                .id(Long.valueOf(id))
                .name(name)
                .lastName(lastName)
                .password(password)
                .role(role)
                .build();
        try {
            userRepository.update(user);
        } catch (IllegalArgumentException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            FxUtilities.displayInformationPopup("Something went wrong with query to database!", Alert.AlertType.ERROR);
            return;
        }
        FxUtilities.displayInformationPopup("The user updated successfully!", Alert.AlertType.INFORMATION);
        populateUserTable();
    }

    @FXML
    void saveUser() {
        var name = nameField.getText();
        var lastName = surnameField.getText();
        var password = pwField.getText();
        var role = roleBox.getValue();

        if (name.isEmpty()) {
            displayInformationPopup("User name is empty!", Alert.AlertType.ERROR);
            return;
        }
        if (lastName.isEmpty()) {
            displayInformationPopup("User surname is empty!", Alert.AlertType.ERROR);
            return;
        }
        if (password.isEmpty()) {
            displayInformationPopup("User password is empty!", Alert.AlertType.ERROR);
            return;
        }
        if (role == null) {
            displayInformationPopup("User Role not specified!", Alert.AlertType.ERROR);
            return;
        }

        User user = User.builder()
                .name(name)
                .lastName(lastName)
                .password(password)
                .role(role)
                .build();
        try {
            userRepository.create(user);
        } catch (IllegalArgumentException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            FxUtilities.displayInformationPopup("Something went wrong with query to database!",
                    Alert.AlertType.ERROR);
            return;
        }
        FxUtilities.displayInformationPopup("The user saved successfully!", Alert.AlertType.INFORMATION);
        populateUserTable();
    }
}
