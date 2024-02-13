module org.denys.hudymov.lab4 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires static lombok;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires java.validation;

    opens org.denys.hudymov.lab4 to javafx.fxml;
    exports org.denys.hudymov.lab4;
    exports org.denys.hudymov.lab4.viewcontroller;
    opens org.denys.hudymov.lab4.viewcontroller to javafx.fxml;
}