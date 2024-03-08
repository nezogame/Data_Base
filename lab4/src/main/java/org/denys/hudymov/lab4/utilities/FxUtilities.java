package org.denys.hudymov.lab4.utilities;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.denys.hudymov.lab4.entity.User;


public class FxUtilities {

    @Getter
    @Setter
    private static User currentUser;

    @Getter
    @Setter
    private static Integer optimisticLockHash;

    public static void displayInformationPopup(String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setHeaderText(null); // Set to null to hide the header text
        alert.setContentText(message);

        alert.showAndWait(); // Wait for the user to close the dialog
    }

    public static Alert displayInformationAboutTransaction(String message, AlertType type) {
        Alert alert = new Alert(type, message);
        alert.setTitle(message);
        alert.setHeaderText("Wait please");

        return alert;
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

    public static void saveAsPdf(TableView<?> table, String fileName) throws IllegalArgumentException,
            FileNotFoundException, DocumentException {

        int rowsCount = table.getItems().size();
        ObservableList<? extends TableColumn<?, ?>> columns = table.getColumns();

        if (rowsCount == 0) {
            throw new IllegalArgumentException("Table is empty!");
        }

        Document report = new Document();
        PdfWriter.getInstance(report, new FileOutputStream(fileName.concat(".pdf")));

        report.open();
        //create n columns in table
        PdfPTable pdfTable = new PdfPTable(columns.size());
        //create a cell object
        PdfPCell tableCell;

        // Add column headers to PDF table
        for (TableColumn<?, ?> column : columns) {
            pdfTable.addCell(column.getText());
        }
        List<String> cells = new ArrayList<>();
        for (TableColumn column : columns) {
            for (int i = 0; i < rowsCount; i++) {
                cells.add(column.getCellData(i).toString());
            }
        }

        int k = 0;
        for (int i = 0; k < rowsCount; i++) {
            if (i == columns.size()) {
                k++;
                i = 0;
            }
            pdfTable.addCell(cells.get(k + rowsCount * i));
        }

        report.add(pdfTable);
        report.close();
    }

}
