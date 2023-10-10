package org.denys.hudymov.gui;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.denys.hudymov.controller.ClientController;
import org.denys.hudymov.controller.HotelAccommodationController;
import org.denys.hudymov.controller.RoomController;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;


@Getter
enum Column {
    ROOMS_ID("Room ID"),
    CLIENTS_ID("Client ID"),
    ACCOMMODATION_ID("Accommodation ID");
    private final String columnName;

    Column(String columnName) {
        this.columnName = columnName;
    }
}

@Data
@EqualsAndHashCode(callSuper = true)
public class AppWindow extends JFrame {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JButton button1;
    private JButton button3;
    private JButton button4;
    private JPanel crudPanel;
    private JButton button2;
    private JTable roomsTable;
    private JTable accommodationTable;
    private JTable clientsTable;
    private JTextField roomNumberField;
    private JComboBox comboBox1;
    private JTextField textField2;
    private JTextField textField1;
    private ClientController clientController = ClientController.builder().build();
    private RoomController roomController = RoomController.builder().build();
    private HotelAccommodationController hotelAccommodationController = HotelAccommodationController.builder().build();

    public AppWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        setContentPane(getPanel1());
        populateClientsTable();
        populateRoomsTable();
        populateAccommodationTable();
        render();
        changeColorOfIdColumns();

        getRoomNumberField().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getRoomNumberField().getText().equals("3/10")) {
                    getRoomNumberField().setText("");
                    getRoomNumberField().setForeground(Color.white);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (getRoomNumberField().getText().isEmpty()) {
                    getRoomNumberField().setForeground(Color.GRAY);
                    getRoomNumberField().setText("3/10");
                }
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void populateClientsTable() {
        String[] columns = {"Client ID", "Surname", "Name", "Patronymic", "Passport", "Comment"};
        var clients = clientController.displayClients();
        /*disable editing in table cells*/
        DefaultTableModel columnModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };
        columnModel.setColumnIdentifiers(columns);
        clients.forEach(columnModel::addRow);
        getClientsTable().setModel(columnModel);
    }

    private void populateRoomsTable() {
        String[] columns = {"Room ID", "Room Number", "Seats Number", "Comfort", "Price", "Occupied"};
        var clients = roomController.displayRooms();
        /*disable editing in table cells*/
        DefaultTableModel columnModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };
        columnModel.setColumnIdentifiers(columns);
        clients.forEach(columnModel::addRow);
        getRoomsTable().setModel(columnModel);
    }

    private void populateAccommodationTable() {
        String[] columns = {"Accommodation ID", "Client ID", "Room ID", "Arrival Date", "Departure Date", "Note"};
        var accommodationForAllTime = hotelAccommodationController.displayAccommodationForAllTime();
        /*disable editing in table cells*/
        DefaultTableModel columnModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }

        };
        columnModel.setColumnIdentifiers(columns);
        accommodationForAllTime.forEach(columnModel::addRow);
        getAccommodationTable().setModel(columnModel);
    }

    private void render() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Set the renderer for all columns (you can specify individual columns if needed)
        for (int i = 0; i < getClientsTable().getColumnCount(); i++) {
            getClientsTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        for (int i = 0; i < getRoomsTable().getColumnCount(); i++) {
            getRoomsTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        for (int i = 0; i < getAccommodationTable().getColumnCount(); i++) {
            getAccommodationTable().getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }


        getAccommodationTable().getParent().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                if (getClientsTable().getPreferredSize().width < getClientsTable().getParent().getWidth()) {
                    getClientsTable().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                } else {
                    getClientsTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                }
            }
        });
    }

    private void changeColorOfIdColumns() {
        changeColorOfIdColumns(new DefaultTableCellRenderer(),
                Column.CLIENTS_ID.getColumnName(),
                Color.getHSBColor(106, 186, 143));
        changeColorOfIdColumns(new DefaultTableCellRenderer(),
                Column.ROOMS_ID.getColumnName(),
                Color.getHSBColor(28, 80, 66.27f));
        changeColorOfIdColumns(new DefaultTableCellRenderer(),
                Column.ACCOMMODATION_ID.getColumnName(),
                Color.getHSBColor(11, 25, 26));

    }

    private void changeColorOfIdColumns(DefaultTableCellRenderer cellRenderer, String columnName, Color backgroundColor) {
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        cellRenderer.setBackground(backgroundColor);

        switch (columnName) {
            case "Client ID" -> {
                getClientsTable().getColumn(columnName).setCellRenderer(cellRenderer);
                getAccommodationTable().getColumn(columnName).setCellRenderer(cellRenderer);
            }
            case "Room ID" -> {
                getRoomsTable().getColumn(columnName).setCellRenderer(cellRenderer);
                getAccommodationTable().getColumn(columnName).setCellRenderer(cellRenderer);
            }
            case "Accommodation ID"->{
                getAccommodationTable().getColumn(columnName).setCellRenderer(cellRenderer);
            }
        }
    }
}
