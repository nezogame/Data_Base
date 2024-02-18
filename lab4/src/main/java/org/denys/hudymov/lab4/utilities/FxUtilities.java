package org.denys.hudymov.lab4.utilities;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javax.validation.constraints.NotNull;


public class FxUtilities {

    public static void displayInformationPopup(String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setHeaderText(null); // Set to null to hide the header text
        alert.setContentText(message);

        alert.showAndWait(); // Wait for the user to close the dialog
    }

    public static void switchScene(ActionEvent event, @NotNull String fxml, String title) {
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(FxUtilities.class.getResource("/org/denys/hudymov/lab4/" + fxml));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }

}
