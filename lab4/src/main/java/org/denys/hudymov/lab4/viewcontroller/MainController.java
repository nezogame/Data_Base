package org.denys.hudymov.lab4.viewcontroller;

import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLWarning;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import org.denys.hudymov.lab4.entity.UserHealth;
import org.denys.hudymov.lab4.entity.UsersGroup;
import org.denys.hudymov.lab4.enums.Role;
import org.denys.hudymov.lab4.repository.GroupRepository;
import org.denys.hudymov.lab4.repository.LogRepository;
import org.denys.hudymov.lab4.repository.PsychoHealthRepository;
import org.denys.hudymov.lab4.repository.UserRepository;
import org.denys.hudymov.lab4.repository.UsersGroupRepository;
import org.denys.hudymov.lab4.repository.impl.GroupRepositoryImpl;
import org.denys.hudymov.lab4.repository.impl.LogRepositoryImpl;
import org.denys.hudymov.lab4.repository.impl.PsychoHealthRepositoryImpl;
import org.denys.hudymov.lab4.repository.impl.UserRepositoryImpl;
import org.denys.hudymov.lab4.repository.impl.UsersGroupRepositoryImpl;
import org.denys.hudymov.lab4.utilities.FxUtilities;

import static org.denys.hudymov.lab4.utilities.FxUtilities.displayInformationPopup;


@Data
public class MainController {
    private UserRepository userRepository = new UserRepositoryImpl();

    private GroupRepository groupRepository = new GroupRepositoryImpl();

    private PsychoHealthRepository psychoHealthRepository = new PsychoHealthRepositoryImpl();

    private LogRepository logRepository = new LogRepositoryImpl();

    private UsersGroupRepository userGroupRepository = new UsersGroupRepositoryImpl();

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

    //Work tab
    @FXML
    private TableView<UsersGroup> workTable;

    @FXML
    private TableColumn<UsersGroup, Long> workUserId;

    @FXML
    private TableColumn<UsersGroup, Long> workGroupId;

    @FXML
    private TextField workGroupIdField;

    @FXML
    private TextField workUserIdField;

    @FXML
    private TextField workIdField;

    //Manage tab
    @FXML
    private TableView<Group> manageTable;

    @FXML
    private TableColumn<Group, Long> manageGroupId;

    @FXML
    private TableColumn<Group, String> manageGroupName;

    @FXML
    private TableColumn<Group, Integer> manageGroupSize;

    @FXML
    private Button findGroupByNameBtn;

    @FXML
    private TextField workNameField;

    @FXML
    private TextField workNameAndSizeField;

    @FXML
    private TextField workSizeField;

    @FXML
    private TableView<UserHealth> manageUserTable;

    @FXML
    private TableColumn<UserHealth, Long> manageUserId;

    @FXML
    private TableColumn<UserHealth, String> manageUserName;

    @FXML
    private TableColumn<UserHealth, String> manageUserLastName;

    @FXML
    private TableColumn<UserHealth, String> manageUserCharacteristic;

    @FXML
    private void initialize() {
        roleBox.getItems().setAll(Role.values());
        populateLogTable();
        populateUserTable();
        populateGroupTable();
        populatePsychoHealthTable();
        populateWorkTable();
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

    private void populateWorkTable() {
        Task<List<UsersGroup>> task = new Task<>() {
            @Override
            protected List<UsersGroup> call() throws Exception {
                return userGroupRepository.read(); // Data fetching in background thread
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    workTable.getItems().setAll(getValue()); // UI update on main thread
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
                    FxUtilities.setOptimisticLockHash(u.hashCode());
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
        } catch (IllegalArgumentException | SQLWarning e) {
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
        } catch (IllegalArgumentException | SQLIntegrityConstraintViolationException e) {
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
        var id = psychoIdField.getText();

        if (id.isEmpty()) {
            displayInformationPopup("Characteristic id is empty!", Alert.AlertType.ERROR);
            return;
        }

        try {
            psychoHealthRepository.deleteById(Long.valueOf(id));
        } catch (IllegalArgumentException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            FxUtilities.displayInformationPopup("Such user ID doesn't exist!", Alert.AlertType.ERROR);
            return;
        }
        FxUtilities.displayInformationPopup("The characteristic was deleted!", Alert.AlertType.INFORMATION);
        populatePsychoHealthTable();
    }

    @FXML
    void deleteGroupById() {
        var id = groupIdField.getText();

        if (id.isEmpty()) {
            displayInformationPopup("Group id is empty!", Alert.AlertType.ERROR);
            return;
        }

        try {
            groupRepository.deleteById(Long.valueOf(id));
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
        var id = psychoIdField.getText();
        Optional<PsychoHealth> characteristic;

        if (id.isEmpty()) {
            displayInformationPopup("Characteristic id is empty!", Alert.AlertType.ERROR);
            return;
        }

        try {
            characteristic = psychoHealthRepository.findById(Long.valueOf(id));
        } catch (IllegalArgumentException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        characteristic.ifPresentOrElse(c -> {
                    FxUtilities.setOptimisticLockHash(c.hashCode());
                    characteristicField.setText(c.getCharacteristic());
                    healthUserIdField.setText(c.getUserId().toString());
                    FxUtilities.displayInformationPopup("The characteristic was found!", Alert.AlertType.INFORMATION);
                },
                () -> FxUtilities.displayInformationPopup("Characteristic not found", Alert.AlertType.INFORMATION));
    }

    @FXML
    void findGroupById() {
        var id = groupIdField.getText();
        Optional<Group> group;

        if (id.isEmpty()) {
            displayInformationPopup("Group id is empty!", Alert.AlertType.ERROR);
            return;
        }

        try {
            group = groupRepository.findById(Long.valueOf(id));
        } catch (IllegalArgumentException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        group.ifPresentOrElse(g -> {
                    FxUtilities.setOptimisticLockHash(g.hashCode());
                    groupNameField.setText(g.getName());
                    groupSizeField.setText(g.getSize().toString());
                    FxUtilities.displayInformationPopup("The group was found!", Alert.AlertType.INFORMATION);
                },
                () -> FxUtilities.displayInformationPopup("Group not found", Alert.AlertType.INFORMATION));
    }

    /*@FXML
    void findWorkById() {
        var id = workIdField.getText();
        Optional<UsersGroup> work;

        if (id.isEmpty()) {
            displayInformationPopup("Group id is empty!", Alert.AlertType.ERROR);
            return;
        }

        try {
            work = userGroupRepository.findById(Long.valueOf(id));
        } catch (IllegalArgumentException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        work.ifPresentOrElse(w -> {
                    groupNameField.setText(w.getGroupId().toString());
                    groupSizeField.setText(w.getUserId().toString());
                    FxUtilities.displayInformationPopup("The work group was found!", Alert.AlertType.INFORMATION);
                },
                () -> FxUtilities.displayInformationPopup("Work group not found", Alert.AlertType.INFORMATION));
    }*/

    @FXML
    void deleteWorkById() {
        var id = workGroupIdField.getText();
        var userId = workUserIdField.getText();

        if (id.isEmpty()) {
            displayInformationPopup("Group id is empty!", Alert.AlertType.ERROR);
            return;
        }
        if (userId.isEmpty()) {
            displayInformationPopup("User id is empty!", Alert.AlertType.ERROR);
            return;
        }


        try {
            userGroupRepository.deleteById(Long.valueOf(id), Long.valueOf(userId));
        } catch (IllegalArgumentException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            FxUtilities.displayInformationPopup("Something went wrong with query to database!",
                    Alert.AlertType.ERROR);
            return;
        }
        FxUtilities.displayInformationPopup("The user was deleted from the group!", Alert.AlertType.INFORMATION);
        populateWorkTable();
    }


    @FXML
    void saveWork() {
        var groupId = workGroupIdField.getText();
        var userId = workUserIdField.getText();


        if (groupId.isEmpty()) {
            displayInformationPopup("Group ID is empty!", Alert.AlertType.ERROR);
            return;
        }
        if (userId.isEmpty()) {
            displayInformationPopup("User ID is empty!", Alert.AlertType.ERROR);
            return;
        }

        if (!groupRepository.isGroupNotCompleted(Long.valueOf(groupId))) {
            FxUtilities.displayInformationPopup("This !", Alert.AlertType.ERROR);
            return;
        }

        UsersGroup work = UsersGroup.builder()
                .groupId(Long.valueOf(groupId))
                .userId(Long.valueOf(userId))
                .build();
        try {
            userGroupRepository.create(work);
        } catch (IllegalArgumentException | SQLIntegrityConstraintViolationException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            FxUtilities.displayInformationPopup("Such user ID or group ID doesn't exist!", Alert.AlertType.ERROR);
            return;
        }
        FxUtilities.displayInformationPopup("The characteristic saved successfully!", Alert.AlertType.INFORMATION);
        populateWorkTable();
    }

    @FXML
    void saveCharacteristic() {
        var characteristic = characteristicField.getText();
        var userId = healthUserIdField.getText();


        if (characteristic.isEmpty()) {
            displayInformationPopup("Characteristic name is empty!", Alert.AlertType.ERROR);
            return;
        }
        if (userId.isEmpty()) {
            displayInformationPopup("User ID is empty!", Alert.AlertType.ERROR);
            return;
        }

        PsychoHealth psychoHealth = PsychoHealth.builder()
                .characteristic(characteristic)
                .userId(Long.valueOf(userId))
                .build();
        try {
            psychoHealthRepository.create(psychoHealth);
        } catch (IllegalArgumentException | SQLIntegrityConstraintViolationException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            FxUtilities.displayInformationPopup("Such user ID doesn't exist!", Alert.AlertType.ERROR);
            return;
        }
        FxUtilities.displayInformationPopup("The characteristic saved successfully!", Alert.AlertType.INFORMATION);
        populatePsychoHealthTable();
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
            displayInformationPopup("Group size is empty!", Alert.AlertType.ERROR);
            return;
        }

        Group group = Group.builder()
                .name(name)
                .size(Integer.valueOf(size))
                .build();
        try {
            groupRepository.create(group);
        } catch (IllegalArgumentException | SQLIntegrityConstraintViolationException e) {
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
        var id = psychoIdField.getText();
        var characteristic = characteristicField.getText();
        var userId = healthUserIdField.getText();

        if (id.isEmpty()) {
            displayInformationPopup("Characteristic ID is empty!", Alert.AlertType.ERROR);
            return;
        }
        if (characteristic.isEmpty()) {
            displayInformationPopup("Characteristic name is empty!", Alert.AlertType.ERROR);
            return;
        }
        if (userId.isEmpty()) {
            displayInformationPopup("User ID is empty!", Alert.AlertType.ERROR);
            return;
        }

        PsychoHealth psychoHealth = PsychoHealth.builder()
                .id(Long.valueOf(id))
                .characteristic(characteristic)
                .userId(Long.valueOf(userId))
                .build();
        try {
            psychoHealthRepository.update(psychoHealth);
        } catch (IllegalArgumentException | ConstraintViolationException | SQLWarning e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            FxUtilities.displayInformationPopup("Something went wrong with query to database!",
                    Alert.AlertType.ERROR);
            return;
        }
        FxUtilities.displayInformationPopup("The characteristic updated successfully!", Alert.AlertType.INFORMATION);
        populatePsychoHealthTable();
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
            displayInformationPopup("Group size is empty!", Alert.AlertType.ERROR);
            return;
        }

        Group group = Group.builder()
                .id(Long.valueOf(id))
                .name(name)
                .size(Integer.valueOf(size))
                .build();
        try {
            groupRepository.update(group);
        } catch (IllegalArgumentException | ConstraintViolationException | SQLWarning e) {
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

    @FXML
    void updateWorkTable() {
        populateWorkTable();
        FxUtilities.displayInformationPopup("The Work table reloaded!", Alert.AlertType.INFORMATION);
    }

    @FXML
    void findGroupByName() {
        var name = workNameField.getText();
        Optional<Group> group;

        if (name.isEmpty()) {
            displayInformationPopup("Group Name is empty!", Alert.AlertType.ERROR);
            return;
        }

        try {
            group = groupRepository.findGroupByName(name);
        } catch (IllegalArgumentException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
            return;
        }

        group.ifPresentOrElse(g -> {
                    manageTable.setItems(FXCollections.observableArrayList(g));
                    FxUtilities.displayInformationPopup("The group was found!", Alert.AlertType.INFORMATION);
                },
                () -> FxUtilities.displayInformationPopup("Group not found!", Alert.AlertType.INFORMATION)
        );
    }

    @FXML
    void findGroupByNameAndSize() {
        var name = workNameAndSizeField.getText();
        var size = workSizeField.getText();
        Optional<Group> group;

        if (name.isEmpty()) {
            displayInformationPopup("Group Name is empty!", Alert.AlertType.ERROR);
            return;
        }
        if (size.isEmpty()) {
            displayInformationPopup("Group Size is empty!", Alert.AlertType.ERROR);
            return;
        }

        try {
            group = groupRepository.findGroupByNameAndWorkerQuantity(name, Integer.valueOf(size));
        } catch (IllegalArgumentException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
            return;
        }

        group.ifPresentOrElse(g -> {
                    manageTable.setItems(FXCollections.observableArrayList(g));
                    FxUtilities.displayInformationPopup("The group was found!", Alert.AlertType.INFORMATION);
                },
                () -> FxUtilities.displayInformationPopup("Group not found!", Alert.AlertType.INFORMATION)
        );
    }

    @FXML
    void findUnfinishedGroup() {
        List<Group> group;

        try {
            group = groupRepository.findIncompleteGroups();
        } catch (SQLException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
            return;
        }

        manageTable.setItems(FXCollections.observableArrayList(group));
        FxUtilities.displayInformationPopup("Query completed!", Alert.AlertType.INFORMATION);
    }

    @FXML
    void findUserWithPsychoHealth() {
        List<UserHealth> users;

        try {
            users = userRepository.userWithPsychoHealth();
        } catch (SQLException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        manageUserTable.setItems(FXCollections.observableArrayList(users));
        FxUtilities.displayInformationPopup("Query completed!", Alert.AlertType.INFORMATION);
    }

    @FXML
    void usersToPdf() {
        try {
            FxUtilities.saveAsPdf(manageUserTable, "manageUserTable");
        } catch (IllegalArgumentException | FileNotFoundException | DocumentException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void groupsToPdf() {
        try {
            FxUtilities.saveAsPdf(groupTable, "groupTable");
        } catch (IllegalArgumentException | FileNotFoundException | DocumentException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void incompleteGroupsToPdf() {
        try {
            FxUtilities.saveAsPdf(manageTable, "manageTable");
        } catch (IllegalArgumentException | FileNotFoundException | DocumentException e) {
            FxUtilities.displayInformationPopup(e.getMessage(), Alert.AlertType.INFORMATION);
        }
    }
}
