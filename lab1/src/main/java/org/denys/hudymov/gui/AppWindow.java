package org.denys.hudymov.gui;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.denys.hudymov.controller.ClientController;
import org.denys.hudymov.controller.HotelAccommodationController;
import org.denys.hudymov.controller.RoomController;
import org.denys.hudymov.model.Validator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.table.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;


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

@Getter
enum Comfort {
    NO_ROOM("─"),
    SINGLE("Single Room"),
    STANDARD("Standard"),
    DELUXE("Deluxe"),
    PRESIDENT("President Room");
    private final String comfort;

    Comfort(String comfort) {
        this.comfort = comfort;
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
    private JComboBox comfortBox;
    private JTextField priceText;
    private JTextField seatsNumberText;
    private JButton addRoomButton;
    private JTextPane CommentTextPane;
    private JScrollPane CommentScrollPane;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField patronymicField;
    private JTextField PassportCodeField;
    private JComboBox roomNumberComBox;
    private JComboBox PassportComBox;
    private JComboBox accommodationComBox;
    private JTextField updateRoomNumberField;
    private JTextField updateSeatsNumberText;
    private JComboBox updateComfortBox;
    private JTextField updatePriceText;
    private JTextField updateOccupied;
    private JButton updateRoomBtn;
    private ClientController clientController = ClientController.builder().build();
    private RoomController roomController = RoomController.builder().build();
    private HotelAccommodationController hotelAccommodationController = HotelAccommodationController.builder().build();

    public AppWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        setContentPane(getPanel1());
        populateTables();
        populateComboBox();
        getRoomNumberField().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getRoomNumberField().getText().equals("Example: 3/10")) {
                    getRoomNumberField().setText("");
                    getRoomNumberField().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getRoomNumberField().getText().isEmpty()) {
                    getRoomNumberField().setForeground(Color.GRAY);
                    getRoomNumberField().setText("Example: 3/10");
                }
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        addRoomButton.addActionListener(e -> {
            StringBuilder exception = new StringBuilder();

            var roomNumber = getRoomNumberField().getText();
            var seatsNumber = getSeatsNumberText().getText();
            var comfort = getComfortBox().getSelectedItem().toString();
            var price = getPriceText().getText();
            getRoomNumberField().setText("");
            getSeatsNumberText().setText("");
            getComfortBox().setSelectedItem("─");
            getPriceText().setText("");

            try {
                Validator.validateRoomNumber(roomNumber);
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validateNumberOfSeats(seatsNumber);
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validateComfort(comfort);
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validatePrice(price);
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            if (!exception.isEmpty()) {
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, exception);
                return;
            }
            try {
                roomController.addRoom(roomNumber, Integer.parseInt(seatsNumber), comfort, price);
            } catch (SQLException sqlException) {
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, sqlException);
            }
            populateRoomsTable();
        });
        roomNumberComBox.addActionListener(e -> {
            var room = roomController.getRoomByNumber(getRoomNumberComBox().getSelectedItem().toString());
            getUpdateRoomNumberField().setText(room.getRoomNumber());
            getUpdateSeatsNumberText().setText(room.getSeatsNumber().toString());
            getUpdateComfortBox().setSelectedItem(room.getComfort());
            getUpdatePriceText().setText(room.getPrice());
            getUpdateOccupied().setText(room.getOccupied().toString());
        });
        updateRoomBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void populateTables() {
        populateClientsTable();
        populateRoomsTable();
        populateAccommodationTable();
        render();
        changeColorOfIdColumns();
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

        //add item for update query in combo box
        for (var c : clients) {
            getPassportComBox().addItem(c.get(4));
        }
    }

    private void populateRoomsTable() {
        String[] columns = {"Room ID", "Room Number", "Seats Number", "Comfort", "Price", "Occupied"};
        var rooms = roomController.displayRooms();
        /*disable editing in table cells*/
        DefaultTableModel columnModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };
        columnModel.setColumnIdentifiers(columns);
        rooms.forEach(columnModel::addRow);
        getRoomsTable().setModel(columnModel);

        //add item for update query in combo box
        for (var r : rooms) {
            getRoomNumberComBox().addItem(r.get(1));
        }
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

        //add item for update query in combo box
        for (var a : accommodationForAllTime) {
            getAccommodationComBox().addItem(a.get(0));
        }
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
            case "Accommodation ID" -> {
                getAccommodationTable().getColumn(columnName).setCellRenderer(cellRenderer);
            }
        }
    }

    private void populateComboBox() {
        for (var comfort : Comfort.values()) {
            getComfortBox().addItem(comfort.getComfort());
            getUpdateComfortBox().addItem(comfort.getComfort());
        }
    }
}
