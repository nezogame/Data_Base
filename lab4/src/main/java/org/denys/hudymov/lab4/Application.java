package org.denys.hudymov.lab4;

import java.io.IOException;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.denys.hudymov.lab4.entity.Log;
import org.denys.hudymov.lab4.repository.LogRepository;
import org.denys.hudymov.lab4.repository.impl.LogRepositoryImpl;
import org.denys.hudymov.lab4.utilities.FxUtilities;

public class Application extends javafx.application.Application {
    LogRepository log = new LogRepositoryImpl();

    @Override
    public void start(Stage stage) throws IOException {
        stage.setOnCloseRequest(event -> {
            /** Log data to database here
             */
            var user = FxUtilities.getCurrentUser();
            if (user == null) {
                return;
            }

            var logEntity = Log.builder()
                    .dateTime(ZonedDateTime.now())
                    .userId(user.getId())
                    .action("Log out")
                    .build();

            try {
                log.create(logEntity);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 512, 269);
        stage.setTitle("Log in");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}