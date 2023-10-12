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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;


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
    private JTable roomsTable;
    private JTable accommodationTable;
    private JTable clientsTable;
    private JTextField roomNumberText;
    private JComboBox comfortBox;
    private JTextField priceText;
    private JTextField seatsNumberText;
    private JButton addRoomButton;
    private JTextPane commentTextPane;
    private JScrollPane CommentScrollPane;
    private JTextField nameText;
    private JTextField surnameText;
    private JTextField patronymicText;
    private JTextField passportCodeText;
    private JComboBox roomNumberComBox;
    private JComboBox PassportComBox;
    private JComboBox accommodationComBox;
    private JTextField updateRoomNumberText;
    private JTextField updateSeatsNumberText;
    private JComboBox updateComfortBox;
    private JTextField updatePriceText;
    private JTextField updateOccupiedText;
    private JButton updateRoomBtn;
    private JComboBox deleteRoomBox;
    private JComboBox deleteClientBox;
    private JButton deleteClientBtn;
    private JButton deleteRoomBtn;
    private JButton deleteReservationBtn;
    private JComboBox clientAccommodationBox;
    private JComboBox updateClientAccommodationBox;
    private JComboBox roomAccommodationBox;
    private JComboBox updateRoomAccommodationBox;
    private JComboBox deleteReservationBox;
    private JButton addClientBtn;
    private JButton updateClientBtn;
    private JButton updateReservationBtn;
    private JButton addReservationBtn;
    private JTextPane updateCommentTextPane;
    private JTextField updatePassportCodeText;
    private JTextField updatePatronymicText;
    private JTextField updateNameText;
    private JTextField updateSurnameText;
    private JTextField arrivalText;
    private JTextField updateArrivalText;
    private JTextField departText;
    private JTextField updateDepartText;
    private JTextPane noteTextPane;
    private JTextPane updateNoteTextPane;
    private ClientController clientController = ClientController.builder().build();
    private RoomController roomController = RoomController.builder().build();
    private HotelAccommodationController hotelAccommodationController = HotelAccommodationController.builder().build();
    private long clintId;
    private long roomId;
    private long accommodationId;

    public AppWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        setContentPane(getPanel1());
        populateAll();

        getRoomNumberText().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getRoomNumberText().getText().equals("Example: 3/10")) {
                    getRoomNumberText().setText("");
                    getRoomNumberText().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getRoomNumberText().getText().isEmpty()) {
                    getRoomNumberText().setForeground(Color.GRAY);
                    getRoomNumberText().setText("Example: 3/10");
                }
            }
        });

        addRoomButton.addActionListener(e -> {
            StringBuilder exception = new StringBuilder();
            var selectedComfort = Optional.ofNullable(getComfortBox().getSelectedItem());
            if (selectedComfort.isEmpty()) {
                return;
            }
            var roomNumber = getRoomNumberText().getText();
            var seatsNumber = getSeatsNumberText().getText();
            var comfort = selectedComfort.get().toString();
            var price = getPriceText().getText();


            try {
                Validator.validateTextField(roomNumber, "roomNumber");
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
                getRoomNumberText().setText("Example: 3/10");
                getSeatsNumberText().setText("");
                getComfortBox().setSelectedItem("─");
                getPriceText().setText("");
                roomController.addRoom(roomNumber, Integer.parseInt(seatsNumber), comfort, price);
            } catch (SQLException sqlException) {
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, sqlException);
            }
            populateAll();
        });

        roomNumberComBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var selectedRoomNumber = Optional.ofNullable(getRoomNumberComBox().getSelectedItem());
                if (selectedRoomNumber.isEmpty()) {
                    return;
                }
                var room = roomController.getRoomByNumber(selectedRoomNumber.get().toString());
                getUpdateRoomNumberText().setText(room.getRoomNumber());
                getUpdateSeatsNumberText().setText(room.getSeatsNumber().toString());
                getUpdateComfortBox().setSelectedItem(room.getComfort());
                getUpdatePriceText().setText(room.getPrice());
                getUpdateOccupiedText().setText(room.getOccupied().toString());
                setRoomId(room.getRoomId());
            }
        });

        updateRoomBtn.addActionListener(e -> {
            var selectedComfort = Optional.ofNullable(getUpdateComfortBox().getSelectedItem());
            if (selectedComfort.isEmpty()) {
                return;
            }
            var roomNumber = getUpdateRoomNumberText().getText();
            roomController.updateRoom(
                    getRoomId(),
                    roomNumber,
                    Integer.parseInt(getUpdateSeatsNumberText().getText()),
                    selectedComfort.get().toString(),
                    getUpdatePriceText().getText(),
                    Boolean.parseBoolean(getUpdateOccupiedText().getText()));

            populateAll();
        });

        surnameText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getSurnameText().getText().equals("Example: Smith")) {
                    getSurnameText().setText("");
                    getSurnameText().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getSurnameText().getText().isEmpty()) {
                    getSurnameText().setForeground(Color.GRAY);
                    getSurnameText().setText("Example: Smith");
                }
            }
        });
        nameText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getNameText().getText().equals("Example: John")) {
                    getNameText().setText("");
                    getNameText().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getNameText().getText().isEmpty()) {
                    getNameText().setForeground(Color.GRAY);
                    getNameText().setText("Example: John");
                }
            }
        });
        patronymicText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getPatronymicText().getText().equals("Optional field")) {
                    getPatronymicText().setText("");
                    getPatronymicText().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getPatronymicText().getText().isEmpty()) {
                    getPatronymicText().setForeground(Color.GRAY);
                    getPatronymicText().setText("Optional field");
                }
            }
        });
        passportCodeText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getPassportCodeText().getText().equals("Example: GF123456")) {
                    getPassportCodeText().setText("");
                    getPassportCodeText().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getPassportCodeText().getText().isEmpty()) {
                    getPassportCodeText().setForeground(Color.GRAY);
                    getPassportCodeText().setText("Example: GF123456");
                }
            }
        });
        addClientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder exception = new StringBuilder();

                var surname = getSurnameText().getText();
                var name = getNameText().getText();
                var patronymic = Objects.equals(getPatronymicText().getText(), "Optional field") ? "" :
                        getPatronymicText().getText();
                var passport = getPassportCodeText().getText();
                var comment = getCommentTextPane().getText();


                try {
                    Validator.validateTextField(surname, "surname");
                } catch (IllegalArgumentException argException) {
                    exception.append(argException.getMessage()).append("\n");
                }

                try {
                    Validator.validateTextField(name, "name");
                } catch (IllegalArgumentException argException) {
                    exception.append(argException.getMessage()).append("\n");
                }

                try {
                    Validator.validateTextField(passport, "passport");
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
                    getSurnameText().setText("Example: Smith");
                    getNameText().setText("Example: John");
                    getPatronymicText().setText("Optional field");
                    getPassportCodeText().setText("Example: GF123456");
                    getCommentTextPane().setText("");
                    clientController.addClient(name, surname, patronymic, passport, comment);
                } catch (SQLException sqlException) {
                    UIManager.put("OptionPane.messageForeground", Color.red);
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, sqlException);
                }

                populateAll();
            }
        });

        PassportComBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var selectedPassport = Optional.ofNullable(getPassportComBox().getSelectedItem());
                if (selectedPassport.isEmpty()) {
                    return;
                }
                var client = clientController.getClientByPassport(selectedPassport.get().toString());
                getUpdateSurnameText().setText(client.getSurname());
                getUpdateNameText().setText(client.getName());
                getUpdatePatronymicText().setText(client.getPatronymic());
                getUpdatePassportCodeText().setText(client.getPassportData());
                getUpdateCommentTextPane().setText(client.getComment());
                setClintId(client.getClientId());
            }
        });
        updateClientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                clientController.updateClient(
                        getClintId(),
                        getUpdateSurnameText().getText(),
                        getUpdateNameText().getText(),
                        getUpdatePatronymicText().getText(),
                        getUpdatePassportCodeText().getText(),
                        getUpdateCommentTextPane().getText());

                populateAll();
            }
        });

        deleteClientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var id = Optional.ofNullable(getDeleteClientBox().getSelectedItem());
                if (id.isEmpty()){
                    return;
                }
                clientController.deleteClient(id.get().toString());

                populateAll();
            }
        });
        arrivalText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getArrivalText().getText().equals("Example: 2023-09-19 16:20")) {
                    getArrivalText().setText("");
                    getArrivalText().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getArrivalText().getText().isEmpty()) {
                    getArrivalText().setForeground(Color.GRAY);
                    getArrivalText().setText("Example: 2023-09-19 16:20");
                }
            }
        });
        departText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getDepartText().getText().equals("Example: 2023-10-07 18:45")) {
                    getDepartText().setText("");
                    getDepartText().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getDepartText().getText().isEmpty()) {
                    getDepartText().setForeground(Color.GRAY);
                    getDepartText().setText("Example: 2023-10-07 18:45");
                }
            }
        });
        addReservationBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder exception = new StringBuilder();

                var clientId = getClientAccommodationBox().getSelectedItem().toString();
                var roomId = getRoomAccommodationBox().getSelectedItem().toString();
                var arrival = getArrivalText().getText();
                var depart = getDepartText().getText();
                var note = getNoteTextPane().getText();


                try {
                    Validator.validateTextField(clientId, "client ID");
                } catch (IllegalArgumentException argException) {
                    exception.append(argException.getMessage()).append("\n");
                }

                try {
                    Validator.validateTextField(roomId, "room ID");
                } catch (IllegalArgumentException argException) {
                    exception.append(argException.getMessage()).append("\n");
                }

                try {
                    Validator.validateTextField(arrival, "arrival date");
                } catch (IllegalArgumentException argException) {
                    exception.append(argException.getMessage()).append("\n");
                }

                try {
                    Validator.validateTextField(depart, "depart date");
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
                    getArrivalText().setText("Example: 2023-09-19 16:20");
                    getDepartText().setText("Example: 2023-10-07 18:45");
                    getNoteTextPane().setText("");
                    hotelAccommodationController.addReservation(Long.parseLong(clientId),
                            Long.parseLong(roomId), Timestamp.valueOf(arrival.concat(":00.0")),
                            Timestamp.valueOf(depart.concat(":00.0")), note);
                    roomController.updateReservation(Long.parseLong(roomId));
                } catch (SQLException sqlException) {
                    UIManager.put("OptionPane.messageForeground", Color.red);
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, sqlException);
                }

                populateAll();
            }
        });
        deleteRoomBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var id = Optional.ofNullable(getDeleteRoomBox().getSelectedItem());
                if (id.isEmpty()){
                    return;
                }
                roomController.deleteRoom(id.get().toString());

                populateAll();
            }
        });
        deleteReservationBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var id = Optional.ofNullable(getDeleteReservationBox().getSelectedItem());
                if (id.isEmpty()){
                    return;
                }
                hotelAccommodationController.deleteReservation(id.get().toString());

                populateAll();
            }
        });
        updateReservationBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hotelAccommodationController.updateReservation(
                        getAccommodationId(),
                        Long.parseLong(getUpdateClientAccommodationBox().getSelectedItem().toString()),
                        Long.parseLong(getUpdateRoomAccommodationBox().getSelectedItem().toString()),
                        getUpdateArrivalText().getText(),
                        getUpdateDepartText().getText(),
                        getUpdateNoteTextPane().getText());

                populateAll();
            }
        });
        accommodationComBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var selectedId = Optional.ofNullable(getAccommodationComBox().getSelectedItem());
                if (selectedId.isEmpty()) {
                    return;
                }
                var accommodation = hotelAccommodationController.getById(Long.parseLong(selectedId.get().toString()));
                getUpdateClientAccommodationBox().setSelectedItem(accommodation.getClientId());
                getUpdateRoomAccommodationBox().setSelectedItem(accommodation.getRoomId());
                getUpdateArrivalText().setText(accommodation.getArrivalDate().toString());
                getUpdateDepartText().setText(accommodation.getDepartureDate().toString());
                getUpdateNoteTextPane().setText(accommodation.getNote());
                setAccommodationId(accommodation.getAccommodationId());
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
            case "Accommodation ID" -> {
                getAccommodationTable().getColumn(columnName).setCellRenderer(cellRenderer);
            }
        }
    }

    private void populateAll() {
        populateTables();
        populateComboBoxes();
    }

    private void populateComboBoxes() {
        populateComfortBox();
        populateUpdateBoxes();
        populateDeleteBoxes();
        populateReservationBox();
    }

    private void populateComfortBox() {
        for (var comfort : Comfort.values()) {
            getComfortBox().addItem(comfort.getComfort());
            getUpdateComfortBox().addItem(comfort.getComfort());
        }
    }

    /**
     * add item for update query in combo box
     */
    private void populateUpdateBoxes() {
        getPassportComBox().removeAllItems();
        getRoomNumberComBox().removeAllItems();
        getAccommodationComBox().removeAllItems();
        for (var p : clientController.getPassportCodes()) {
            getPassportComBox().addItem(p);
        }
        for (var r : roomController.getRoomNumbers()) {
            getRoomNumberComBox().addItem(r);
        }
        for (var id : hotelAccommodationController.getListOfId()) {
            getAccommodationComBox().addItem(id);
        }
    }

    private void populateDeleteBoxes() {
        getDeleteRoomBox().removeAllItems();
        getDeleteReservationBox().removeAllItems();
        getDeleteClientBox().removeAllItems();
        for (var id : clientController.getId()) {
            getDeleteClientBox().addItem(id);
        }
        for (var id : roomController.getId()) {
            getDeleteRoomBox().addItem(id);
        }
        for (var id : hotelAccommodationController.getListOfId()) {
            getDeleteReservationBox().addItem(id);
        }
    }

    private void populateReservationBox(){
        getClientAccommodationBox().removeAllItems();
        getRoomAccommodationBox().removeAllItems();
        getUpdateClientAccommodationBox().removeAllItems();
        getUpdateRoomAccommodationBox().removeAllItems();
        for (var id : clientController.getId()) {
            getClientAccommodationBox().addItem(id);
        }
        for (var id : roomController.getFreeRooms()) {
            getRoomAccommodationBox().addItem(id);
        }
        for (var id : clientController.getId()) {
            getUpdateClientAccommodationBox().addItem(id);
        }
        for (var id : roomController.getId()) {
            getUpdateRoomAccommodationBox().addItem(id);
        }
    }
}
