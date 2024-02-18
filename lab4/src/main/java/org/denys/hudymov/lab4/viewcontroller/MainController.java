package org.denys.hudymov.lab4.viewcontroller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.Data;
import org.denys.hudymov.lab4.entity.User;
import org.denys.hudymov.lab4.enums.Role;
import org.denys.hudymov.lab4.repository.UserRepository;
import org.denys.hudymov.lab4.repository.impl.UserRepositoryImpl;

@Data
public class MainController {
    private UserRepository userRepository = new UserRepositoryImpl();
    @FXML
    TableView<User> userTable;
    @FXML
    Button readUser;

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
        populateUserTable();
    }

    @FXML
    private void populateUserTable() {
        userTable.getItems().setAll(userRepository.read());
    }
}
