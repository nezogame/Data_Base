package org.denys.hudymov.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.denys.hudymov.entity.Room;
import org.denys.hudymov.repository.RoomDao;

@Data
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class RoomService {
    private static final RoomDao ROOM_DAO = RoomDao.builder().build();

    public Vector<Vector<Object>> displayRooms() {
        var clientsVector = new Vector<Vector<Object>>();
        var clients = ROOM_DAO.read();
        clients.forEach(room -> {
            Vector<Object> row = new Vector<>();
            row.add(room.getRoomId());
            row.add(room.getRoomNumber());
            row.add(room.getSeatsNumber());
            row.add(room.getComfort());
            row.add(room.getPrice() != null ? room.getPrice() + "$" : "NULL");
            row.add(room.getOccupied());
            clientsVector.add(row);
        });
        return clientsVector;
    }

    public void addRoom(String roomNumber, Integer seatsNumber, String comfort, String price) throws SQLException {
        ROOM_DAO.create(
                Room.builder()
                        .roomNumber(roomNumber)
                        .seatsNumber(seatsNumber)
                        .comfort(comfort)
                        .price(price)
                        .occupied(false)
                        .build()
        );
    }

    public Room getRoomByNumber(String number) {
        return ROOM_DAO.getByRoomNumber(number).get();
    }

    public void updateRoom(Long id, String roomNumber, Integer seatsNumber, String comfort, String price,
                           Boolean occupied) {

        ROOM_DAO.update(
                Room.builder()
                        .roomId(id)
                        .roomNumber(roomNumber)
                        .seatsNumber(seatsNumber)
                        .comfort(comfort)
                        .price(price)
                        .occupied(occupied)
                        .build()
        );
    }

    public List<Long> getId() {
        return ROOM_DAO.getAllId();
    }

    public List<String> getRoomNumbers() {
        return ROOM_DAO.getAllRoomNumber();
    }

    public void deleteRoom(String id) throws SQLIntegrityConstraintViolationException {
        ROOM_DAO.delete(Integer.parseInt(id));
    }

    public List<String> getFreeRooms() {
        return ROOM_DAO.getAllFreeRoomNumber();
    }

    public void updateReservation(String roomNumber) {
        ROOM_DAO.updateRoomOccupancy(roomNumber);
    }

    public Vector<Vector<Object>> computePopularity() {
        var popularRooms = new Vector<Vector<Object>>();
        var rooms = ROOM_DAO.computePopularity();
        rooms.forEach((room, pair) -> {
            Vector<Object> row = new Vector<>();
            row.add(room.getRoomNumber());
            row.add(room.getComfort());
            row.add(room.getPrice() + "$");
            row.add(pair.getKey());
            row.add(pair.getValue());


            popularRooms.add(row);
        });
        return popularRooms;
    }

    public Vector<Vector<Object>> findSuitableRooms(Integer seats_number, String price,
                                                    Timestamp arrival, Integer days) {

        var suitableRooms = new Vector<Vector<Object>>();
        var rooms = ROOM_DAO.findAvailable(seats_number, price, arrival, days);
        rooms.forEach((room) -> {
            Vector<Object> row = new Vector<>();
            row.add(room.getRoomNumber());
            row.add(room.getSeatsNumber());
            row.add(room.getComfort());
            row.add(room.getPrice() + "$");
            suitableRooms.add(row);
        });
        return suitableRooms;
    }

    public Vector<Vector<Object>> computeAvgPriceForComfort() {

        var pairsComfortAndPrice = new Vector<Vector<Object>>();
        var rooms = ROOM_DAO.findAvgPriceForComfort();
        rooms.forEach((comfort, price) -> {
            Vector<Object> row = new Vector<>();
            row.add(comfort);
            row.add(price);
            pairsComfortAndPrice.add(row);
        });
        return pairsComfortAndPrice;
    }

    public Vector<Vector<Object>> computePriceForComfortBetweenDate(Date start, Date end) {

        var priceForComfort = new Vector<Vector<Object>>();
        var rooms = ROOM_DAO.findRoomsOccupiedBetweenDate(start, end);
        rooms.forEach((room) -> {
            Vector<Object> row = new Vector<>();
            row.add(room.getRoomNumber());
            row.add(room.getSeatsNumber());
            row.add(room.getComfort());
            row.add(room.getPrice());
            row.add(room.getAccommodations().get(0).getArrivalDate());
            row.add(room.getAccommodations().get(0).getDepartureDate());
            priceForComfort.add(row);
        });
        return priceForComfort;
    }

    public void computeRoomIncomeInDateRange(Date start, Date end) throws FileNotFoundException, DocumentException {

        Document PDFReport = new Document();
        PdfWriter.getInstance(PDFReport, new FileOutputStream("pdf_room_income_report_from_sql_using_java.pdf"));
        PDFReport.open();

        PdfPTable reportTable = new PdfPTable(4);
        AtomicReference<PdfPCell> tableCell = new AtomicReference<>();
        PdfPCell headerCell;

        headerCell = new PdfPCell(new Phrase("Room Number"));
        reportTable.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Comfort"));
        reportTable.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Price"));
        reportTable.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Room Income"));
        reportTable.addCell(headerCell);

        final var roomIncome = ROOM_DAO.findRoomsIncomeInDateRange(start, end);
        roomIncome.forEach((room, income) -> {

            tableCell.set(new PdfPCell(new Phrase(room.getRoomNumber())));
            reportTable.addCell(tableCell.get());

            tableCell.set(new PdfPCell(new Phrase(room.getComfort())));
            reportTable.addCell(tableCell.get());

            tableCell.set(new PdfPCell(new Phrase(room.getPrice() + "$")));
            reportTable.addCell(tableCell.get());

            tableCell.set(new PdfPCell(new Phrase(income + "$")));
            reportTable.addCell(tableCell.get());
        });
        PDFReport.add(reportTable);
        PDFReport.close();
    }

    public void computeRoomsComfortsAndNumberOfStayed(Integer year) throws FileNotFoundException, DocumentException {

        Document PDFReport = new Document();
        PdfWriter.getInstance(PDFReport,
                new FileOutputStream("pdf_rooms_booking_count_report_from_sql_using_java.pdf")
        );
        PDFReport.open();

        PdfPTable reportTable = new PdfPTable(4);
        AtomicReference<PdfPCell> tableCell = new AtomicReference<>();
        PdfPCell headerCell;

        headerCell = new PdfPCell(new Phrase("Room Number"));
        reportTable.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Comfort"));
        reportTable.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Price"));
        reportTable.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Booking Count"));
        reportTable.addCell(headerCell);

        final var roomIncome = ROOM_DAO.findRoomsComfortsAndNumberOfStayed(year);
        roomIncome.forEach((room, booking) -> {

            tableCell.set(new PdfPCell(new Phrase(room.getRoomNumber())));
            reportTable.addCell(tableCell.get());

            tableCell.set(new PdfPCell(new Phrase(room.getComfort())));
            reportTable.addCell(tableCell.get());

            tableCell.set(new PdfPCell(new Phrase(room.getPrice() + "$")));
            reportTable.addCell(tableCell.get());

            tableCell.set(new PdfPCell(new Phrase(booking.toString())));
            reportTable.addCell(tableCell.get());
        });
        PDFReport.add(reportTable);
        PDFReport.close();
    }
}
