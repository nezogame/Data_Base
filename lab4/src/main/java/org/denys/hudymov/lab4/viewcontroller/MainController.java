package org.denys.hudymov.lab4.viewcontroller;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.ZonedDateTime;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.validation.ConstraintViolationException;
import lombok.Data;
import org.denys.hudymov.lab4.entity.Group;
import org.denys.hudymov.lab4.entity.Log;
import org.denys.hudymov.lab4.entity.PsychoHealth;
import org.denys.hudymov.lab4.entity.User;
import org.denys.hudymov.lab4.enums.Role;
import org.denys.hudymov.lab4.repository.GroupRepository;
import org.denys.hudymov.lab4.repository.LogRepository;
import org.denys.hudymov.lab4.repository.PsychoHealthRepository;
import org.denys.hudymov.lab4.repository.UserRepository;
import org.denys.hudymov.lab4.repository.impl.GroupRepositoryImpl;
import org.denys.hudymov.lab4.repository.impl.LogRepositoryImpl;
import org.denys.hudymov.lab4.repository.impl.PsychoHealthRepositoryImpl;
import org.denys.hudymov.lab4.repository.impl.UserRepositoryImpl;
import org.denys.hudymov.lab4.utilities.FxUtilities;

import static org.denys.hudymov.lab4.utilities.FxUtilities.displayInformationPopup;

@Data
public class MainController {
    private UserRepository userRepository = new UserRepositoryImpl();

    private GroupRepository groupRepository = new GroupRepositoryImpl();

    private PsychoHealthRepository psychoHealthRepository = new PsychoHealthRepositoryImpl();

    private LogRepository logRepository = new LogRepositoryImpl();

    //User tab
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

    // Psycho health tab
    @FXML
    private TextArea characteristicField;

    @FXML
    private Button deleteCharacteristicBtn;

    @FXML
    private Button findCharacteristicBtn;

    @FXML
    private TextField healthUserIdField;

    @FXML
    private Button updateCharacteristicBtn;

    @FXML
    private Button saveCharacteristicBtn;

    @FXML
    private TableColumn<PsychoHealth, Long> psychoUserId;

    @FXML
    private TableColumn<PsychoHealth, Long> psychoId;

    @FXML
    private TableColumn<PsychoHealth, String> characteristic;

    @FXML
    private TextField psychoIdField;

    @FXML
    private TableView<PsychoHealth> psychoHealthTable;

    //Log tab
    @FXML
    private TableColumn<Log, String> logAction;

    @FXML
    private TableColumn<Log, ZonedDateTime> logDate;

    @FXML
    private TableColumn<Log, Long> logId;

    @FXML
    private TableColumn<Log, Long> logUserId;

    @FXML
    private TableView<Log> logTable;

    @FXML
    private Button updateLogs;

    //Group tab
    @FXML
    private Button deleteGroupBtn;

    @FXML
    private Button findGroupBtn;

    @FXML
    private TableColumn<Group, Long> groupId;

    @FXML
    private TextField groupIdField;

    @FXML
    private TableColumn<Group, String> groupName;

    @FXML
    private TextField groupNameField;

    @FXML
    private TableColumn<Group, Integer> groupSize;

    @FXML
    private TextField groupSizeField;

    @FXML
    private TableView<Group> groupTable;

    @FXML
    private Button saveGroupBtn;

    @FXML
    private Button updateGroupBtn;

    @FXML
    private void initialize() {
        roleBox.getItems().setAll(Role.values());
        populateLogTable();
        populateUserTable();
        populateGroupTable();
        populatePsychoHealthTable();
    }

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


    private void populateLogTable() {
        Task<List<Log>> task = new Task<>() {
            @Override
            protected List<Log> call() throws Exception {
                return logRepository.read(); // Data fetching in background thread
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    logTable.getItems().setAll(getValue()); // UI update on main thread
                });
            }
        };

        new Thread(task).start(); // Launch the task on a separate thread
    }

    private void populatePsychoHealthTable() {
        Task<List<PsychoHealth>> task = new Task<>() {
            @Override
            protected List<PsychoHealth> call() throws Exception {
                return psychoHealthRepository.read(); // Data fetching in background thread
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    psychoHealthTable.getItems().setAll(getValue()); // UI update on main thread
                });
            }
        };

        new Thread(task).start(); // Launch the task on a separate thread
    }

    private void populateGroupTable() {
        Task<List<Group>> task = new Task<>() {
            @Override
            protected List<Group> call() throws Exception {
                return groupRepository.read(); // Data fetching in background thread
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    groupTable.getItems().setAll(getValue()); // UI update on main thread
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
                    FxUtilities.displayInformationPopup("The user was found!", Alert.AlertType.INFORMATION);
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
        } catch (IllegalArgumentException | ConstraintViolationException e) {
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

    @FXML
    void deleteCharacteristicById() {

    }

    @FXML
    void deleteGroupById() {
        var id = groupIdField.getText();

        if (id.isEmpty()) {
            displayInformationPopup("Group id is empty!", Alert.AlertType.ERROR);
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
        FxUtilities.displayInformationPopup("The group was deleted!", Alert.AlertType.INFORMATION);
        populateGroupTable();
    }

    @FXML
    void findCharacteristicById() {

    }

    @FXML
    void findGroupById() {
        var id = groupIdField.getText();
        Optional<Group> user;

        if (id.isEmpty()) {
            displayInformationPopup("Group id is empty!", Alert.AlertType.ERROR);
            return;
        }

        try {
            user = groupRepository.findById(Long.valueOf(id));
        } catch (IllegalArgumentException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        user.ifPresentOrElse(u -> {
                    groupNameField.setText(u.getName());
                    groupSizeField.setText(u.getSize().toString());
                    FxUtilities.displayInformationPopup("The user was found!", Alert.AlertType.INFORMATION);
                },
                () -> FxUtilities.displayInformationPopup("User not found", Alert.AlertType.INFORMATION));
    }

    @FXML
    void saveCharacteristic() {

    }

    @FXML
    void saveGroup() {
        var name = groupNameField.getText();
        var size = groupSizeField.getText();


        if (name.isEmpty()) {
            displayInformationPopup("Group name is empty!", Alert.AlertType.ERROR);
            return;
        }
        if (size.isEmpty()) {
            displayInformationPopup("User surname is empty!", Alert.AlertType.ERROR);
            return;
        }

        Group group = Group.builder()
                .name(name)
                .size(Integer.valueOf(size))
                .build();
        try {
            groupRepository.create(group);
        } catch (IllegalArgumentException | ConstraintViolationException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            FxUtilities.displayInformationPopup("Something went wrong with query to database!",
                    Alert.AlertType.ERROR);
            return;
        }
        FxUtilities.displayInformationPopup("The group saved successfully!", Alert.AlertType.INFORMATION);
        populateGroupTable();
    }

    @FXML
    void updateCharacteristic() {

    }

    @FXML
    void updateGroup() {
        var id = groupIdField.getText();
        var name = groupNameField.getText();
        var size = groupSizeField.getText();

        if (id.isEmpty()) {
            displayInformationPopup("Group id is empty!", Alert.AlertType.ERROR);
            return;
        }
        if (name.isEmpty()) {
            displayInformationPopup("Group name is empty!", Alert.AlertType.ERROR);
            return;
        }
        if (size.isEmpty()) {
            displayInformationPopup("User surname is empty!", Alert.AlertType.ERROR);
            return;
        }

        Group group = Group.builder()
                .id(Long.valueOf(id))
                .name(name)
                .size(Integer.valueOf(size))
                .build();
        try {
            groupRepository.update(group);
        } catch (IllegalArgumentException | ConstraintViolationException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            FxUtilities.displayInformationPopup("Something went wrong with query to database!",
                    Alert.AlertType.ERROR);
            return;
        }
        FxUtilities.displayInformationPopup("The group updated successfully!", Alert.AlertType.INFORMATION);
        populateGroupTable();
    }

    @FXML
    void updateUserTable() {
        populateUserTable();
        FxUtilities.displayInformationPopup("The Users table reloaded!", Alert.AlertType.INFORMATION);
    }

    @FXML
    void updatePsychoHealthTable() {
        populatePsychoHealthTable();
        FxUtilities.displayInformationPopup("The Psycho Health table reloaded!", Alert.AlertType.INFORMATION);
    }

    @FXML
    void updateGroupTable() {
        populateGroupTable();
        FxUtilities.displayInformationPopup("The Groups table reloaded!", Alert.AlertType.INFORMATION);
    }

    @FXML
    void updateLogTable() {
        populateLogTable();
        FxUtilities.displayInformationPopup("The Logs table reloaded!", Alert.AlertType.INFORMATION);
    }
}
