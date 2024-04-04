package org.denys.hudymov.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.denys.hudymov.entity.Room;
import org.denys.hudymov.repository.ClientRepository;
import org.denys.hudymov.repository.PeopleRepository;
import org.denys.hudymov.repository.RoomRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

@Data
@Service
public class RoomService {
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    private final RoomRepository roomRepository;

/*    public RoomService() {
        this.context = new AnnotationConfigApplicationContext();
        this.roomRepository = context.getBean(RoomRepository.class);
    }*/

    public Vector<Vector<Object>> displayRooms() {
        var clientsVector = new Vector<Vector<Object>>();
        var clients = roomRepository.findAll();
        clients.forEach(room -> {
            Vector<Object> row = new Vector<>();
            row.add(room.getRoomId());
            row.add(room.getRoomNumber());
            row.add(room.getSeatsNumber());
            row.add(room.getComfort());
            row.add(room.getPrice() != null ? room.getPrice() + "$" : "NULL");
            row.add(room.isOccupied());
            clientsVector.add(row);
        });
        return clientsVector;
    }

    public void addRoom(String roomNumber, Integer seatsNumber, String comfort, String price) throws SQLException {
        roomRepository.save(
                Room.builder()
                        .roomNumber(roomNumber)
                        .seatsNumber(seatsNumber)
                        .comfort(comfort)
                        .price(Integer.parseInt(price))
                        .occupied(false)
                        .build()
        );
    }

    public Room getRoomByNumber(String number) {
        return roomRepository.findByRoomNumber(number).get();
    }

    @Transactional
    public void updateRoom(Long id, String roomNumber, Integer seatsNumber, String comfort, String price,
                           Boolean occupied) {

        roomRepository.save(
                Room.builder()
                        .roomId(BigInteger.valueOf(id))
                        .roomNumber(roomNumber)
                        .seatsNumber(seatsNumber)
                        .comfort(comfort)
                        .price(Integer.parseInt(price))
                        .occupied(occupied)
                        .build()
        );
    }

    public List<Long> getId() {
        return roomRepository.findAll()
                .stream()
                .map(r -> r.getRoomId().longValue())
                .toList();
    }

    public List<String> getRoomNumbers() {
        return roomRepository.findAll()
                .stream()
                .map(Room::getRoomNumber)
                .toList();
    }

    public void deleteRoom(String id) throws SQLIntegrityConstraintViolationException {
        roomRepository.deleteById(BigInteger.valueOf(Long.parseLong(id)));
    }

    public List<String> getFreeRooms() {
        return roomRepository.findAllByOccupiedFalseOrderByRoomNumber()
                .stream()
                .map(Room::getRoomNumber)
                .toList();
    }
    @Transactional
    public void updateReservation(String roomNumber) {
        roomRepository.updateRoomOccupancy(roomNumber);
    }

    public Vector<Vector<Object>> computePopularity() {
        var popularRooms = new Vector<Vector<Object>>();
        var rooms = roomRepository.findPopularRoomsWithRank();
        for (var r : rooms) {
            Vector<Object> row = new Vector<>();
            row.add(r[0]);
            row.add(r[1]);
            row.add(r[2]);
            row.add(r[3]);
            row.add(r[4]);
            popularRooms.add(row);
        }

        return popularRooms;
    }

    public Vector<Vector<Object>> findSuitableRooms(Integer seats_number, String price,
                                                    Timestamp arrival, Integer days) {

        var suitableRooms = new Vector<Vector<Object>>();
        var rooms = roomRepository.findAvailableRoom(seats_number, price, days, arrival);

        for (var r : rooms) {
            Vector<Object> row = new Vector<>();
            row.add(r[0]);
            row.add(r[1]);
            row.add(r[2]);
            row.add(r[3]+ "$");
            suitableRooms.add(row);
        }

        return suitableRooms;
    }

    public Vector<Vector<Object>> computeAvgPriceForComfort() {

        var pairsComfortAndPrice = new Vector<Vector<Object>>();
        var rooms = roomRepository.findAveragePricePerComfortLevel();

        for (var r : rooms) {
            Vector<Object> row = new Vector<>();
            row.add(r[0]);
            row.add(r[1]);
            pairsComfortAndPrice.add(row);
        }

        return pairsComfortAndPrice;
    }

    public Vector<Vector<Object>> computePriceForComfortBetweenDate(Date start, Date end) {

        var priceForComfort = new Vector<Vector<Object>>();
        var rooms = roomRepository.findAccommodationsByDateRange(start, end);
        for (var r : rooms) {
            Vector<Object> row = new Vector<>();
            row.add(r[0]);
            row.add(r[1]);
            row.add(r[2]);
            row.add(r[3]);
            row.add(r[4]);
            row.add(r[5]);
            priceForComfort.add(row);
        }
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

        final var roomIncome = roomRepository.findRoomsIncomeInDateRange(start, end);

        for (var r : roomIncome) {
            tableCell.set(new PdfPCell(new Phrase(r[0].toString())));
            reportTable.addCell(tableCell.get());

            tableCell.set(new PdfPCell(new Phrase(r[1].toString())));
            reportTable.addCell(tableCell.get());

            tableCell.set(new PdfPCell(new Phrase(r[2] + "$")));
            reportTable.addCell(tableCell.get());

            tableCell.set(new PdfPCell(new Phrase(r[3] + "$")));
            reportTable.addCell(tableCell.get());
        }

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

        final var roomIncome = roomRepository.findRoomsComfortsAndNumberOfStayed(year);

        for (var r : roomIncome) {
            tableCell.set(new PdfPCell(new Phrase(r[0].toString())));
            reportTable.addCell(tableCell.get());

            tableCell.set(new PdfPCell(new Phrase(r[1].toString())));
            reportTable.addCell(tableCell.get());

            tableCell.set(new PdfPCell(new Phrase(r[2] + "$")));
            reportTable.addCell(tableCell.get());

            tableCell.set(new PdfPCell(new Phrase(r[3].toString())));
            reportTable.addCell(tableCell.get());
        }

        PDFReport.add(reportTable);
        PDFReport.close();
    }
}
