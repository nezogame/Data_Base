package org.denys.hudymov.gui;

import com.itextpdf.text.DocumentException;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.denys.hudymov.validator.Validator;
import org.denys.hudymov.service.ClientService;
import org.denys.hudymov.service.HotelAccommodationService;
import org.denys.hudymov.service.RoomService;
import org.denys.hudymov.service.ServicesCategoryService;
import org.denys.hudymov.service.ServicesService;
import org.denys.hudymov.service.StaffService;
import org.springframework.stereotype.Component;

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

@EqualsAndHashCode(callSuper = true)
@Component
@Data
public class AppWindow extends JFrame {
    private final ClientService clientService;
    private final RoomService roomService;
    private final HotelAccommodationService hotelAccommodationService;
    private final ServicesService servicesService;
    private final ServicesCategoryService categoryService;
    private final StaffService staffService;
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JButton displayClientsBtn;
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
    private JTextField updatePatronymicText;
    private JTextField updateNameText;
    private JTextField updateSurnameText;
    private JTextField arrivalText;
    private JTextField updateArrivalText;
    private JTextField departText;
    private JTextField updateDepartText;
    private JTextPane noteTextPane;
    private JTextPane updateNoteTextPane;
    private JTable currentClientsTable;
    private JTable avgDaysHotelTable;
    private JButton clientHistoryBtn;
    private JButton roomsPopularityBtn;
    private JTextField findRoomsArrivalDateText;
    private JButton findSuitableRooms;
    private JComboBox clientPassportHistoryBox;
    private JTable clientHistoryTable;
    private JButton calculateAvgDaysBtn;
    private JTable ComfortPriceTable;
    private JTable clientsStaysTable;
    private JButton avgPriceComfortBtn;
    private JTable roomsPopularityTable;
    private JTextField findDaysOfStayText;
    private JTextField findByPriceText;
    private JTextField findBySeatsNumberText;
    private JTable suitableRooms;
    private JTable clientForLastYearTable;
    private JButton clientForLastYearButton;
    private JTable placingBetweenDateTable;
    private JButton percentRevenueButton;
    private JButton button4;
    private JTextField fromDateText;
    private JTextField toDateText;
    private JButton findAllPlacingBetweenBtn;
    private JButton clientsStaysBtn;
    private JTextField clientsStaysText;
    private JButton displayRoomsIncomeInButton;
    private JTextField incomeFromDateText;
    private JTextField incomeToDateText;
    private JButton displayPlacingInRoomsBtn;
    private JTextField yearText;
    private JPanel Manager;
    private JTextField employeeNameText;
    private JTextField updateEmployeeNameText;
    private JTextField employeeEmailText;
    private JTextField updateEmployeeEmailText;
    private JTextField updateEmploymentDateText;
    private JTextField employmentDateText;
    private JTextField salaryText;
    private JTextField updateSalaryText;
    private JComboBox employeeIdBox;
    private JComboBox deleteEmployeeIdBox;
    private JButton deleteEmployeeBtn;
    private JButton updateEmployeeBtn;
    private JButton addEmployeeBtn;
    private JTextField categoryText;
    private JTextField updateCategoryText;
    private JTextPane descriptionTextPane;
    private JTextPane updateDescriptionTextPane;
    private JComboBox categoryBox;
    private JComboBox deleteCategoryBox;
    private JButton deleteCategoryBtn;
    private JButton updateCategoryBtn;
    private JButton addCategoryBtn;
    private JTextField serviceText;
    private JTextField updateServiceText;
    private JTextField servicePriceText;
    private JTextField updateServicePriceText;
    private JComboBox serviceCategoryBox;
    private JComboBox updateServiceCategoryBox;
    private JButton addServiceBtn;
    private JButton updateServiceBtn;
    private JButton deleteServiceBtn;
    private JComboBox serviceBox;
    private JComboBox deleteServiceBox;
    private JTable servicesCategoryTable;
    private JTable staffTable;
    private JTable servicesTable;
    private JTable categoriesWithoutServicesTable;
    private JTable servicesInEachCategoryTable;
    private JTable pricesAboveAvgTable;
    private JTable hiredStaffTable;
    private JTable servicesWithinCategoryTable;
    private JTextField monthAgoText;
    private JButton hiredStaffBtn;
    private JButton servicesWithinCategoryBtn;
    private JButton categoriesWithoutServicesBtn;
    private JButton servicesInEachCategoryBtn;
    private JButton pricesAboveAvgBtn;
    private JButton findAllAccommodationBtn;
    private long clintId;
    private String clientPassport;
    private long roomId;
    private long accommodationId;
    private long staffId;

    public AppWindow(ClientService clientService,
                     RoomService roomService,
                     HotelAccommodationService hotelAccommodationService,
                     ServicesService servicesService,
                     ServicesCategoryService categoryService,
                     StaffService staffService) {

        this.clientService = clientService;
        this.roomService = roomService;
        this.hotelAccommodationService = hotelAccommodationService;
        this.servicesService = servicesService;
        this.categoryService = categoryService;
        this.staffService = staffService;

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
                this.roomService.addRoom(roomNumber, Integer.parseInt(seatsNumber), comfort, price);
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
                var room = AppWindow.this.roomService.getRoomByNumber(selectedRoomNumber.get().toString());
                getUpdateRoomNumberText().setText(room.getRoomNumber());
                getUpdateSeatsNumberText().setText(room.getSeatsNumber().toString());
                getUpdateComfortBox().setSelectedItem(room.getComfort());
                getUpdatePriceText().setText(String.valueOf(room.getPrice()));
                getUpdateOccupiedText().setText(String.valueOf(room.isOccupied()));
                setRoomId(room.getRoomId().longValue());
            }
        });

        updateRoomBtn.addActionListener(e -> {
            var selectedComfort = Optional.ofNullable(getUpdateComfortBox().getSelectedItem());
            if (selectedComfort.isEmpty()) {
                return;
            }
            var roomNumber = getUpdateRoomNumberText().getText();
            this.roomService.updateRoom(
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
                    AppWindow.this.clientService.addClient(name, surname, patronymic, passport, comment);
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
                var client = AppWindow.this.clientService.getClientByPassport(selectedPassport.get().toString());
                getUpdateSurnameText().setText(client.getPeople().getSurname());
                getUpdateNameText().setText(client.getPeople().getName());
                getUpdatePatronymicText().setText(client.getPeople().getPatronymic());
                setClientPassport(client.getPeople().getPassportData());
                getUpdateCommentTextPane().setText(client.getUserComment());
                setClintId(client.getClientId().longValue());
            }
        });
        updateClientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AppWindow.this.clientService.updateClient(
                        getClintId(),
                        getUpdateSurnameText().getText(),
                        getUpdateNameText().getText(),
                        getUpdatePatronymicText().getText(),
                        getClientPassport(),
                        getUpdateCommentTextPane().getText());

                populateAll();
            }
        });

        deleteClientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var id = Optional.ofNullable(getDeleteClientBox().getSelectedItem());
                if (id.isEmpty()) {
                    return;
                }
                try {
                    AppWindow.this.clientService.deleteClient(id.get().toString());
                } catch (SQLIntegrityConstraintViolationException constraintException) {
                    UIManager.put("OptionPane.messageForeground", Color.red);
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, constraintException.getMessage());
                }

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

                var clientPassportCode = getClientAccommodationBox().getSelectedItem().toString();
                var roomNumber = getRoomAccommodationBox().getSelectedItem().toString();
                var arrival = getArrivalText().getText();
                var depart = getDepartText().getText();
                var note = getNoteTextPane().getText();


                try {
                    Validator.validateTextField(clientPassportCode, "client ID");
                } catch (IllegalArgumentException argException) {
                    exception.append(argException.getMessage()).append("\n");
                }

                try {
                    Validator.validateTextField(roomNumber, "room ID");
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
                    hotelAccommodationService.addReservation(clientPassportCode,
                            roomNumber, Timestamp.valueOf(arrival.concat(":00.0")),
                            Timestamp.valueOf(depart.concat(":00.0")), note);
                    roomService.updateReservation(roomNumber);
                } catch (NoSuchElementException | SQLException addException) {
                    getArrivalText().setText(arrival);
                    getDepartText().setText(depart);
                    getNoteTextPane().setText(note);
                    UIManager.put("OptionPane.messageForeground", Color.red);
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, addException.getMessage());
                } catch (IllegalArgumentException argumentException) {
                    argumentException.printStackTrace();
                    getArrivalText().setText(arrival);
                    getDepartText().setText(depart);
                    getNoteTextPane().setText(note);
                    UIManager.put("OptionPane.messageForeground", Color.red);
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, "Format must be yyyy-mm-dd hh:mm");
                }

                populateAll();
            }
        });
        deleteRoomBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var id = Optional.ofNullable(getDeleteRoomBox().getSelectedItem());
                if (id.isEmpty()) {
                    return;
                }
                try {
                    AppWindow.this.roomService.deleteRoom(id.get().toString());
                } catch (SQLIntegrityConstraintViolationException constraintException) {
                    UIManager.put("OptionPane.messageForeground", Color.red);
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, constraintException.getMessage());
                }

                populateAll();
            }
        });
        deleteReservationBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var id = Optional.ofNullable(getDeleteReservationBox().getSelectedItem());
                if (id.isEmpty()) {
                    return;
                }
                AppWindow.this.hotelAccommodationService.deleteReservation(id.get().toString());

                populateAll();
            }
        });
        updateReservationBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AppWindow.this.hotelAccommodationService.updateReservation(
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
                var accommodation = AppWindow.this.hotelAccommodationService.getById(Long.parseLong(selectedId.get().toString()));
                getUpdateClientAccommodationBox().setSelectedItem(accommodation.getClient().getClientId());
                getUpdateRoomAccommodationBox().setSelectedItem(accommodation.getRoom().getRoomId());
                getUpdateArrivalText().setText(accommodation.getArrivalDate().toString());
                getUpdateDepartText().setText(accommodation.getDepartureDate().toString());
                getUpdateNoteTextPane().setText(accommodation.getNote());
                setAccommodationId(accommodation.getAccommodationId().longValue());
            }
        });

        clientHistoryBtn.addActionListener(e -> {
            var passportCode = Optional.ofNullable(getClientPassportHistoryBox().getSelectedItem());
            if (passportCode.isEmpty()) {
                return;
            }
            populateClientHistoryTable(passportCode.get().toString());
        });

        roomsPopularityBtn.addActionListener(e -> {
            populateRoomsPopularityTable();
        });

        findSuitableRooms.addActionListener(e -> {
            StringBuilder exception = new StringBuilder();
            var seats = getFindBySeatsNumberText().getText();
            var price = getFindByPriceText().getText();
            var arrivalDate = getFindRoomsArrivalDateText().getText();
            var daysOfStay = getFindDaysOfStayText().getText();
            try {
                Validator.validateNumberOfSeats(seats);
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validatePrice(price);
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validateTextField(arrivalDate, "arrival date");
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }
            try {
                Validator.validateNumberOfSeats(daysOfStay);
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            if (!exception.isEmpty()) {
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, exception);
                return;
            }

            getFindBySeatsNumberText().setText("");
            getFindByPriceText().setText("");
            getFindRoomsArrivalDateText().setText("Example: 2023-09-19 16:20");
            getFindDaysOfStayText().setText("");

            populateSuitableRooms(Integer.valueOf(seats), price, arrivalDate, Integer.valueOf(daysOfStay));
        });
        findRoomsArrivalDateText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getFindRoomsArrivalDateText().getText().equals("Example: 2023-09-19 16:20")) {
                    getFindRoomsArrivalDateText().setText("");
                    getFindRoomsArrivalDateText().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getFindRoomsArrivalDateText().getText().isEmpty()) {
                    getFindRoomsArrivalDateText().setForeground(Color.GRAY);
                    getFindRoomsArrivalDateText().setText("Example: 2023-09-19 16:20");
                }
            }
        });

        displayClientsBtn.addActionListener(e -> {
            populateCurrentClientsTable();
        });

        calculateAvgDaysBtn.addActionListener(e -> {
            populateAvgDaysTable();
        });

        avgPriceComfortBtn.addActionListener(e -> {
            populateAvgComfortPriceTable();
        });

        clientForLastYearButton.addActionListener(e -> {
            populateClientForLastYearTable();
        });
        fromDateText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getFromDateText().getText().equals("Example: 2023-09-19")) {
                    getFromDateText().setText("");
                    getFromDateText().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getFromDateText().getText().isEmpty()) {
                    getFromDateText().setForeground(Color.GRAY);
                    getFromDateText().setText("Example: 2023-09-19");
                }
            }
        });
        toDateText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getToDateText().getText().equals("Example: 2023-10-07")) {
                    getToDateText().setText("");
                    getToDateText().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getToDateText().getText().isEmpty()) {
                    getToDateText().setForeground(Color.GRAY);
                    getToDateText().setText("Example: 2023-10-07");
                }
            }
        });

        findAllPlacingBetweenBtn.addActionListener(e -> {
            StringBuilder exception = new StringBuilder();
            var fromDate = getFromDateText().getText();
            var toDate = getToDateText().getText();

            try {
                Validator.validateTextField(fromDate, "From Date");
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validateTextField(toDate, "To Date");
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            if (!exception.isEmpty()) {
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, exception);
                return;
            }

            getFromDateText().setText("Example: 2023-09-19");
            getToDateText().setText("Example: 2023-10-07");

            populateSuitableRooms(fromDate, toDate);
        });

        percentRevenueButton.addActionListener(e -> {
            try {
                this.hotelAccommodationService.percentRevenueGrowthToPdf();
            } catch (FileNotFoundException | DocumentException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });

        clientsStaysBtn.addActionListener(e -> {
            StringBuilder exception = new StringBuilder();
            var stays = getClientsStaysText().getText();

            try {
                Validator.validateStays(stays);
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            if (!exception.isEmpty()) {
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, exception);
                return;
            }

            getClientsStaysText().setText("");
            populateClientsByNumberOfHotelStays(Integer.parseInt(stays));
        });
        incomeFromDateText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getIncomeFromDateText().getText().equals("Example: 2023-09-19")) {
                    getIncomeFromDateText().setText("");
                    getIncomeFromDateText().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getIncomeFromDateText().getText().isEmpty()) {
                    getIncomeFromDateText().setForeground(Color.GRAY);
                    getIncomeFromDateText().setText("Example: 2023-09-19");
                }
            }
        });

        incomeToDateText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getIncomeToDateText().getText().equals("Example: 2023-10-07")) {
                    getIncomeToDateText().setText("");
                    getIncomeToDateText().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getIncomeToDateText().getText().isEmpty()) {
                    getIncomeToDateText().setForeground(Color.GRAY);
                    getIncomeToDateText().setText("Example: 2023-10-07");
                }
            }
        });

        displayRoomsIncomeInButton.addActionListener(e -> {
            StringBuilder exception = new StringBuilder();
            var start = getIncomeFromDateText().getText();
            var end = getIncomeToDateText().getText();

            try {
                Validator.validateTextField(start, "Income From Date");
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validateTextField(end, "Income To Date");
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            if (!exception.isEmpty()) {
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, exception);
                return;
            }

            getIncomeFromDateText().setText("Example: 2023-09-19");
            getIncomeToDateText().setText("Example: 2023-10-07");
            try {
                this.roomService.computeRoomIncomeInDateRange(Date.valueOf(start.trim()), Date.valueOf(end.trim()));
            } catch (FileNotFoundException | DocumentException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });

        yearText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getYearText().getText().equals("Example: 2023")) {
                    getYearText().setText("");
                    getYearText().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getYearText().getText().isEmpty()) {
                    getYearText().setForeground(Color.GRAY);
                    getYearText().setText("Example: 2023");
                }
            }
        });
        displayPlacingInRoomsBtn.addActionListener(e -> {
            StringBuilder exception = new StringBuilder();
            var year = getYearText().getText();

            try {
                Validator.validateTextField(year, "Searched Year");
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            if (!exception.isEmpty()) {
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, exception);
                return;
            }

            getYearText().setText("");
            try {
                this.roomService.computeRoomsComfortsAndNumberOfStayed(Integer.parseInt(year));
            } catch (FileNotFoundException | DocumentException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });
        employeeNameText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getEmployeeNameText().getText().equals("Example: John")) {
                    getEmployeeNameText().setText("");
                    getEmployeeNameText().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getEmployeeNameText().getText().isEmpty()) {
                    getEmployeeNameText().setForeground(Color.GRAY);
                    getEmployeeNameText().setText("Example: John");
                }
            }
        });
        employeeEmailText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getEmployeeEmailText().getText().equals("Example: John@gmail.com")) {
                    getEmployeeEmailText().setText("");
                    getEmployeeEmailText().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getEmployeeEmailText().getText().isEmpty()) {
                    getEmployeeEmailText().setForeground(Color.GRAY);
                    getEmployeeEmailText().setText("Example: John@gmail.com");
                }
            }
        });
        employmentDateText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getEmploymentDateText().getText().equals("Example: 2023-09-19")) {
                    getEmploymentDateText().setText("");
                    getEmploymentDateText().setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getEmploymentDateText().getText().isEmpty()) {
                    getEmploymentDateText().setForeground(Color.GRAY);
                    getEmploymentDateText().setText("Example: 2023-09-19");
                }
            }
        });

        addEmployeeBtn.addActionListener(e -> {
            StringBuilder exception = new StringBuilder();

            var name = getEmployeeNameText().getText();
            var email = getEmployeeEmailText().getText();
            var employmentDate = getEmploymentDateText().getText();
            var salary = getSalaryText().getText();

            try {
                Validator.validateTextField(name, "employee name");
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validateTextField(email, "employee email");
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validateTextField(employmentDate, "employment date");
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validatePrice(salary);
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
                getEmployeeNameText().setText("Example: John");
                getEmployeeEmailText().setText("Example: John@gmail.com");
                getEmploymentDateText().setText("Example: 2023-09-19");
                getSalaryText().setText("");
                this.staffService.addStaff(name, email, Date.valueOf(employmentDate.trim()), salary);

            } catch (NoSuchElementException | SQLException addException) {
                getEmployeeNameText().setText(name);
                getEmployeeEmailText().setText(email);
                getEmploymentDateText().setText(employmentDate);
                getSalaryText().setText(salary);
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, addException.getMessage());
            } catch (IllegalArgumentException argumentException) {
                getEmployeeNameText().setText(name);
                getEmployeeEmailText().setText(email);
                getEmploymentDateText().setText(employmentDate);
                getSalaryText().setText(salary);
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, "Format must be yyyy-mm-dd");
            }

            populateAll();
        });

        employeeIdBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var selectedStaffId = Optional.ofNullable(getEmployeeIdBox().getSelectedItem());
                if (selectedStaffId.isEmpty()) {
                    return;
                }
                var staff = AppWindow.this.staffService.getStaffById(Long.parseLong(selectedStaffId.get().toString()));
                getUpdateEmployeeNameText().setText(staff.getName());
                getUpdateEmployeeEmailText().setText(staff.getEmail());
                getUpdateEmploymentDateText().setText(staff.getEmploymentDate().toString());
                getUpdateSalaryText().setText(staff.getSalary().toString());
                setStaffId(staff.getStaffId().longValue());
            }
        });

        updateEmployeeBtn.addActionListener(e -> {
            StringBuilder exception = new StringBuilder();

            var name = getUpdateEmployeeNameText().getText();
            var email = getUpdateEmployeeEmailText().getText();
            var employmentDate = getUpdateEmploymentDateText().getText();
            var salary = getUpdateSalaryText().getText();

            try {
                Validator.validateTextField(name, "employee name");
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validateTextField(email, "employee email");
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validateTextField(employmentDate, "employment date");
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validatePrice(salary);
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
                this.staffService.updateStaff(getStaffId(), name, email, Date.valueOf(employmentDate.trim()), salary);

            } catch (NoSuchElementException addException) {
                getEmployeeNameText().setText(name);
                getEmployeeEmailText().setText(email);
                getEmploymentDateText().setText(employmentDate);
                getSalaryText().setText(salary);
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, addException.getMessage());
            } catch (IllegalArgumentException argumentException) {
                getEmployeeNameText().setText(name);
                getEmployeeEmailText().setText(email);
                getEmploymentDateText().setText(employmentDate);
                getSalaryText().setText(salary);
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, "Format must be yyyy-mm-dd");
            }

            populateAll();
        });

        deleteEmployeeBtn.addActionListener(e -> {
            var id = Optional.ofNullable(getDeleteEmployeeIdBox().getSelectedItem());
            if (id.isEmpty()) {
                return;
            }
            try {
                this.staffService.deleteStaff(id.get().toString());
            } catch (SQLIntegrityConstraintViolationException constraintException) {
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, constraintException.getMessage());
            }

            populateAll();
        });

        addCategoryBtn.addActionListener(e -> {
            StringBuilder exception = new StringBuilder();

            var category = getCategoryText().getText();
            var description = getDescriptionTextPane().getText();


            try {
                Validator.validateTextField(category, "category");
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validateTextField(description, "description");
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
                getCategoryText().setText("");
                getDescriptionTextPane().setText("");
                this.categoryService.addCategory(category, description);

            } catch (NoSuchElementException | SQLException addException) {
                getCategoryText().setText(category);
                getDescriptionTextPane().setText(description);
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, addException.getMessage());
            }

            populateAll();
        });

        updateCategoryBtn.addActionListener(e -> {
            StringBuilder exception = new StringBuilder();

            var category = getUpdateCategoryText().getText();
            var description = getUpdateDescriptionTextPane().getText();

            try {
                Validator.validateTextField(category, "category");
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validateTextField(description, "description");
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
                getUpdateCategoryText().setText("");
                getUpdateDescriptionTextPane().setText("");
                this.categoryService.updateCategory(category, description);

            } catch (NoSuchElementException addException) {
                getUpdateCategoryText().setText(category);
                getUpdateDescriptionTextPane().setText(description);
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, addException.getMessage());
            }

            populateAll();
        });

        deleteCategoryBtn.addActionListener(e -> {
            var id = Optional.ofNullable(getDeleteCategoryBox().getSelectedItem());
            if (id.isEmpty()) {
                return;
            }
            try {
                this.categoryService.deleteCategory(id.get().toString());
            } catch (SQLIntegrityConstraintViolationException constraintException) {
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, constraintException.getMessage());
            }

            populateAll();
        });

        addServiceBtn.addActionListener(e -> {
            StringBuilder exception = new StringBuilder();

            var service = getServiceText().getText();
            var price = getServicePriceText().getText();
            var category = getServiceCategoryBox().getSelectedItem().toString();


            try {
                Validator.validateTextField(service, "service name");
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validatePrice(price);
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validateTextField(category, "service category");
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
                getServiceText().setText("");
                getServicePriceText().setText("");
                getServiceCategoryBox().setSelectedItem("");
                this.servicesService.addService(service, price, category);

            } catch (NoSuchElementException | SQLException addException) {
                getServiceText().setText(service);
                getServicePriceText().setText(price);
                getServiceCategoryBox().setSelectedItem(category);
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, addException.getMessage());
            }

            populateAll();
        });

        updateServiceBtn.addActionListener(e -> {
            StringBuilder exception = new StringBuilder();

            var service = getUpdateServiceText().getText();
            var price = getUpdateServicePriceText().getText();
            var category = getUpdateServiceCategoryBox().getSelectedItem().toString();


            try {
                Validator.validateTextField(service, "service name");
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validatePrice(price);
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            try {
                Validator.validateTextField(category, "service category");
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
                getUpdateServiceText().setText("");
                getUpdateServicePriceText().setText("");
                getUpdateServiceCategoryBox().setSelectedItem("");
                this.servicesService.updateService(service, price, category);

            } catch (NoSuchElementException addException) {
                getUpdateServiceText().setText(service);
                getUpdateServicePriceText().setText(price);
                getUpdateServiceCategoryBox().setSelectedItem(category);
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, addException.getMessage());
            }

            populateAll();
        });

        deleteServiceBtn.addActionListener(e -> {
            var id = Optional.ofNullable(getDeleteServiceBox().getSelectedItem());
            if (id.isEmpty()) {
                return;
            }
            try {
                this.servicesService.deleteService(id.get().toString());
            } catch (SQLIntegrityConstraintViolationException constraintException) {
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, constraintException.getMessage());
            }

            populateAll();
        });

        categoryBox.addActionListener(e -> {
            var selectedCategory = Optional.ofNullable(getCategoryBox().getSelectedItem());
            if (selectedCategory.isEmpty()) {
                return;
            }
            var category = this.categoryService.getCategoryById(selectedCategory.get().toString());

            getUpdateCategoryText().setText(category.getCategory());
            getUpdateDescriptionTextPane().setText(category.getDescription());
        });

        serviceBox.addActionListener(e -> {
            var selectedService = Optional.ofNullable(getServiceBox().getSelectedItem());
            if (selectedService.isEmpty()) {
                return;
            }
            var service = this.servicesService.getServiceById(selectedService.get().toString());

            getUpdateServiceText().setText(service.getServiceName());
            getUpdateServicePriceText().setText(service.getPrice().toString());
            getUpdateServiceCategoryBox().setSelectedItem(service.getServicesCategory().getCategory());
        });

        servicesWithinCategoryBtn.addActionListener(e -> {
            populateServicesWithinCategoryTable();
        });

        pricesAboveAvgBtn.addActionListener(e -> {
            populatePricesAboveAvgTable();
        });
        servicesInEachCategoryBtn.addActionListener(e -> {
            populateServicesInEachCategoryTable();
        });

        categoriesWithoutServicesBtn.addActionListener(e -> {
            populateCategoriesWithoutServicesTable();
        });

        hiredStaffBtn.addActionListener(e -> {
            StringBuilder exception = new StringBuilder();
            var month = getMonthAgoText().getText();

            try {
                Validator.validatePositiveNumber(month);
            } catch (IllegalArgumentException argException) {
                exception.append(argException.getMessage()).append("\n");
            }

            if (!exception.isEmpty()) {
                UIManager.put("OptionPane.messageForeground", Color.red);
                JFrame jFrame = new JFrame();
                JOptionPane.showMessageDialog(jFrame, exception);
                return;
            }

            getClientsStaysText().setText("");
            populateHiredStaffTable(Integer.parseInt(month));
        });
    }

    private void populateTables() {
        populateClientsTable();
        populateRoomsTable();
        populateAccommodationTable();
        populateServicesTableTable();
        populateServicesCategoryTableTable();
        populateStaffTableTable();
        render();
        changeColorOfIdColumns();
    }

    private @NonNull DefaultTableModel disableCellsEditing(String[] columns) {
        /*disable editing in table cells*/
        DefaultTableModel columnModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };
        columnModel.setColumnIdentifiers(columns);
        return columnModel;
    }

    private void populateClientsTable() {
        String[] columns = {"Client ID", "Surname", "Name", "Patronymic", "Passport", "Comment"};
        var clients = clientService.displayClients();
        var columnModel = disableCellsEditing(columns);
        clients.forEach(columnModel::addRow);
        getClientsTable().setModel(columnModel);
    }

    private void populateRoomsTable() {
        String[] columns = {"Room ID", "Room Number", "Seats Number", "Comfort", "Price", "Occupied"};
        var rooms = roomService.displayRooms();
        var columnModel = disableCellsEditing(columns);
        rooms.forEach(columnModel::addRow);
        getRoomsTable().setModel(columnModel);
    }

    private void populateAccommodationTable() {
        String[] columns = {"Accommodation ID", "Client ID", "Room ID", "Arrival Date", "Departure Date", "Note"};
        var accommodationForAllTime = hotelAccommodationService.displayAccommodationForAllTime();
        var columnModel = disableCellsEditing(columns);
        accommodationForAllTime.forEach(columnModel::addRow);
        getAccommodationTable().setModel(columnModel);
    }

    private void populateServicesTableTable() {
        String[] columns = {"Service Name", "Price", "Category"};
        var categories = servicesService.displayServices();
        var columnModel = disableCellsEditing(columns);
        categories.forEach(columnModel::addRow);
        getServicesTable().setModel(columnModel);
    }

    private void populateServicesCategoryTableTable() {
        String[] columns = {"Category", "Description"};
        var categories = categoryService.displayCategory();
        var columnModel = disableCellsEditing(columns);
        categories.forEach(columnModel::addRow);
        getServicesCategoryTable().setModel(columnModel);
    }

    private void populateStaffTableTable() {
        String[] columns = {"Staff ID", "Name", "Email", "Salary", "Employment Date"};
        var categories = staffService.displayStaff();
        var columnModel = disableCellsEditing(columns);
        categories.forEach(columnModel::addRow);
        getStaffTable().setModel(columnModel);
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
            default -> {
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
        populateCategoryBox();
        populateClientHistoryBox();
        populateManagerUpdateBoxes();
        populateManagerDeleteBoxes();
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
        for (var p : clientService.getPassportCodes()) {
            getPassportComBox().addItem(p);
        }
        for (var r : roomService.getRoomNumbers()) {
            getRoomNumberComBox().addItem(r);
        }
        for (var id : hotelAccommodationService.getListOfId()) {
            getAccommodationComBox().addItem(id);
        }
    }

    private void populateDeleteBoxes() {
        getDeleteRoomBox().removeAllItems();
        getDeleteReservationBox().removeAllItems();
        getDeleteClientBox().removeAllItems();
        for (var id : clientService.getId()) {
            getDeleteClientBox().addItem(id);
        }
        for (var id : roomService.getId()) {
            getDeleteRoomBox().addItem(id);
        }
        for (var id : hotelAccommodationService.getListOfId()) {
            getDeleteReservationBox().addItem(id);
        }
    }

    private void populateManagerUpdateBoxes() {
        getEmployeeIdBox().removeAllItems();
        getCategoryBox().removeAllItems();
        getServiceBox().removeAllItems();
        for (var id : staffService.getIdList()) {
            getEmployeeIdBox().addItem(id);
        }
        for (var id : categoryService.getIdList()) {
            getCategoryBox().addItem(id);
        }
        for (var id : servicesService.getIdList()) {
            getServiceBox().addItem(id);
        }
    }

    private void populateManagerDeleteBoxes() {
        getDeleteEmployeeIdBox().removeAllItems();
        getDeleteCategoryBox().removeAllItems();
        getDeleteServiceBox().removeAllItems();
        for (var id : staffService.getIdList()) {
            getDeleteEmployeeIdBox().addItem(id);
        }
        for (var id : categoryService.getIdList()) {
            getDeleteCategoryBox().addItem(id);
        }
        for (var id : servicesService.getIdList()) {
            getDeleteServiceBox().addItem(id);
        }
    }

    private void populateReservationBox() {
        getClientAccommodationBox().removeAllItems();
        getRoomAccommodationBox().removeAllItems();
        getUpdateClientAccommodationBox().removeAllItems();
        getUpdateRoomAccommodationBox().removeAllItems();
        for (var passport : clientService.getPassportCodes()) {
            getClientAccommodationBox().addItem(passport);
        }
        for (var roomNumber : roomService.getFreeRooms()) {
            getRoomAccommodationBox().addItem(roomNumber);
        }
        for (var id : clientService.getId()) {
            getUpdateClientAccommodationBox().addItem(id);
        }
        for (var id : roomService.getId()) {
            getUpdateRoomAccommodationBox().addItem(id);
        }
    }

    private void populateCategoryBox() {
        for (var category : categoryService.getIdList()) {
            getServiceCategoryBox().addItem(category);
            getUpdateServiceCategoryBox().addItem(category);
        }
    }

    private void populateClientHistoryBox() {
        getClientPassportHistoryBox().removeAllItems();
        for (var passport : clientService.getPassportCodes()) {
            getClientPassportHistoryBox().addItem(passport);
        }
    }

    private void populateClientHistoryTable(String passportCode) {
        String[] columns = {"Passport", "Surname", "Name", "Patronymic", "Days Spend"};
        var clientHistory = clientService.getClientHistory(passportCode);
        var columnModel = disableCellsEditing(columns);
        clientHistory.forEach(columnModel::addRow);
        getClientHistoryTable().setModel(columnModel);
    }

    private void populateRoomsPopularityTable() {
        String[] columns = {"Room №", "Comfort", "Price", "Popularity", "Rank"};
        var roomsPopularity = roomService.computePopularity();
        var columnModel = disableCellsEditing(columns);
        roomsPopularity.forEach(columnModel::addRow);
        getRoomsPopularityTable().setModel(columnModel);
    }

    private void populateSuitableRooms(Integer seats_number, String price, String arrival, Integer days) {
        String[] columns = {"Room №", "Seat Number", "Comfort", "Price"};
        var arrivalDate = Timestamp.valueOf(arrival.concat(":00.0"));
        var suitableRooms = roomService.findSuitableRooms(seats_number, price, arrivalDate, days);
        var columnModel = disableCellsEditing(columns);
        suitableRooms.forEach(columnModel::addRow);
        getSuitableRooms().setModel(columnModel);
    }

    private void populateCurrentClientsTable() {
        String[] columns = {"Surname", "Name", "Patronymic", "Passport", "Comment"};
        var suitableRooms = clientService.displayGuests();
        var columnModel = disableCellsEditing(columns);
        suitableRooms.forEach(columnModel::addRow);
        getCurrentClientsTable().setModel(columnModel);
    }

    private void populateAvgDaysTable() {
        String[] columns = {"Passport", "Surname", "Name", "Patronymic", "Avg Days"};
        var suitableRooms = clientService.displayAvgStayDuration();
        var columnModel = disableCellsEditing(columns);
        suitableRooms.forEach(columnModel::addRow);
        getAvgDaysHotelTable().setModel(columnModel);
    }

    private void populateAvgComfortPriceTable() {
        String[] columns = {"Comfort", "Price"};
        var avgPriceForComfort = roomService.computeAvgPriceForComfort();
        var columnModel = disableCellsEditing(columns);
        avgPriceForComfort.forEach(columnModel::addRow);
        getComfortPriceTable().setModel(columnModel);
    }

    private void populateClientForLastYearTable() {
        String[] columns = {"Passport", "Surname", "Name", "Patronymic", "Active accommodation"};
        var avgPriceForComfort = clientService.displayClientsPlacementForLastYear();
        var columnModel = disableCellsEditing(columns);
        avgPriceForComfort.forEach(columnModel::addRow);
        getClientForLastYearTable().setModel(columnModel);
    }

    private void populateSuitableRooms(String startDate, String endDate) {
        String[] columns = {"Room №", "Seat Number", "Comfort", "Price", "Arrival Date", "Departure Date"};
        var suitableRooms = roomService.computePriceForComfortBetweenDate(
                Date.valueOf(startDate.trim()),
                Date.valueOf(endDate.trim())
        );
        var columnModel = disableCellsEditing(columns);
        suitableRooms.forEach(columnModel::addRow);
        getPlacingBetweenDateTable().setModel(columnModel);
    }

    private void populateClientsByNumberOfHotelStays(Integer minAccommodation) {
        String[] columns = {"Passport", "Surname", "Name", "Patronymic", "Comment"};
        var hotelStays = clientService.displayClientsByNumberOfHotelStays(minAccommodation);
        var columnModel = disableCellsEditing(columns);
        hotelStays.forEach(columnModel::addRow);
        getClientsStaysTable().setModel(columnModel);
    }

    private void populateHiredStaffTable(Integer month) {
        String[] columns = {"Staff id", "Name", "Salary", "Employment Date", "Email"};
        var hiredStaff = staffService.staffHiredMonthAgo(month);
        var columnModel = disableCellsEditing(columns);
        hiredStaff.forEach(columnModel::addRow);
        getHiredStaffTable().setModel(columnModel);
    }

    private void populateServicesWithinCategoryTable() {
        String[] columns = {"Category", "Service Name", "Price"};
        var suitableRooms = servicesService.displayServicesWithinCategory();
        var columnModel = disableCellsEditing(columns);
        suitableRooms.forEach(columnModel::addRow);
        getServicesWithinCategoryTable().setModel(columnModel);
    }

    private void populatePricesAboveAvgTable() {
        String[] columns = {"Service Name", "Price", "Category"};
        var suitableRooms = servicesService.displayPricesAboveAvg();
        var columnModel = disableCellsEditing(columns);
        suitableRooms.forEach(columnModel::addRow);
        getPricesAboveAvgTable().setModel(columnModel);
    }

    private void populateServicesInEachCategoryTable() {
        String[] columns = {"Category", "Service Count"};
        var suitableRooms = servicesService.displayServicesInEachCategory();
        var columnModel = disableCellsEditing(columns);
        suitableRooms.forEach(columnModel::addRow);
        getServicesInEachCategoryTable().setModel(columnModel);
    }

    private void populateCategoriesWithoutServicesTable() {
        String[] columns = {"Category", "Description"};
        var suitableRooms = categoryService.displayCategoriesWithoutServices();
        var columnModel = disableCellsEditing(columns);
        suitableRooms.forEach(columnModel::addRow);
        getCategoriesWithoutServicesTable().setModel(columnModel);
    }
}


