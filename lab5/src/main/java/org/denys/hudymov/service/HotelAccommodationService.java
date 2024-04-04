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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.denys.hudymov.entity.Client;
import org.denys.hudymov.entity.HotelAccommodation;
import org.denys.hudymov.entity.Room;
import org.denys.hudymov.repository.ClientRepository;
import org.denys.hudymov.repository.HotelAccommodationRepository;
import org.denys.hudymov.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Data
@Builder
@RequiredArgsConstructor
@Service
public class HotelAccommodationService {
    //    private AnnotationConfigApplicationContext context;
    private final HotelAccommodationRepository hotelAccommodationRepository;
    private final ClientRepository clientRepository;
    private final RoomRepository roomRepository;

/*    public HotelAccommodationService() {
        this.context = new AnnotationConfigApplicationContext();
        this.hotelAccommodationRepository = context.getBean(HotelAccommodationRepository.class);
        this.clientRepository = context.getBean(ClientRepository.class);
        this.roomRepository = context.getBean(RoomRepository.class);
    }*/

    public Vector<Vector<Object>> displayAccommodationForAllTime() {
        var accommodationVector = new Vector<Vector<Object>>();
        var hotelAccommodations = hotelAccommodationRepository.findAll();
        hotelAccommodations.forEach(accommodation -> {
            Vector<Object> row = new Vector<>();
            row.add(accommodation.getAccommodationId());
            row.add(accommodation.getClient().getClientId());
            row.add(accommodation.getRoom().getRoomId());
            row.add(accommodation.getArrivalDate());
            row.add(accommodation.getDepartureDate());
            row.add(accommodation.getNote());
            accommodationVector.add(row);
        });
        return accommodationVector;
    }

    public List<Long> getListOfId() {
        return hotelAccommodationRepository.findAll()
                .stream()
                .map(h -> h.getAccommodationId().longValue())
                .toList();
    }

    @Transactional
    public void addReservation(String clientPassport, String roomNumber, Timestamp arrival, Timestamp depart,
                               String note) throws SQLException, NoSuchElementException {

        var clientId = clientRepository.findClientByPeoplePassportData(clientPassport);
        var roomId = roomRepository.findByRoomNumber(roomNumber);

        hotelAccommodationRepository.save(HotelAccommodation.builder()
                .client(Client.builder()
                        .clientId(clientId.orElseThrow().getClientId()).build())
                .room(Room.builder()
                        .roomId(roomId.orElseThrow().getRoomId()).build())
                .arrivalDate(arrival)
                .departureDate(depart)
                .note(note)
                .build());
    }

    public void deleteReservation(String id) {
        hotelAccommodationRepository.deleteById(BigInteger.valueOf(Long.parseLong(id)));
    }

    @Transactional
    public void updateReservation(Long id, Long clientId, Long roomId, String arrival, String depart, String note) {

        hotelAccommodationRepository.save(
                HotelAccommodation.builder()
                        .accommodationId(BigInteger.valueOf(id))
                        .client(Client.builder()
                                .clientId(BigInteger.valueOf(clientId))
                                .build())
                        .room(Room.builder()
                                .roomId(BigInteger.valueOf(roomId))
                                .build())
                        .arrivalDate(Timestamp.valueOf(arrival))
                        .departureDate(Timestamp.valueOf(depart))
                        .note(note)
                        .build()
        );
    }

    public HotelAccommodation getById(Long id) throws IllegalArgumentException {
        var accommodationEntity = hotelAccommodationRepository.findById(BigInteger.valueOf(id));
        if (accommodationEntity.isEmpty()) {
            throw new IllegalArgumentException("This id " + id + " doesn't exist in the table!");
        }
        return accommodationEntity.get();
    }

    public void percentRevenueGrowthToPdf() throws FileNotFoundException, DocumentException {
        DecimalFormat df = new DecimalFormat("0.00%");

        Document PDFReport = new Document();
        PdfWriter.getInstance(PDFReport, new FileOutputStream("pdf_revenue_report_from_sql_using_java.pdf"));
        PDFReport.open();

        PdfPTable myReportTable = new PdfPTable(4);
        AtomicReference<PdfPCell> tableCell = new AtomicReference<>();
        PdfPCell headerCell;

        headerCell = new PdfPCell(new Phrase("Year"));
        myReportTable.addCell(headerCell);
        headerCell = new PdfPCell(new Phrase("Month"));
        myReportTable.addCell(headerCell);
        headerCell = new PdfPCell(new Phrase("Monthly Revenue"));
        myReportTable.addCell(headerCell);
        headerCell = new PdfPCell(new Phrase("Percent Growth"));
        myReportTable.addCell(headerCell);

        final var percentRevenueGrowth = hotelAccommodationRepository.findMonthlyRevenueWithPercentGrowth();

        for (var r : percentRevenueGrowth) {
            tableCell.set(new PdfPCell(new Phrase(r[0].toString())));
            myReportTable.addCell(tableCell.get());
            tableCell.set(new PdfPCell(new Phrase(r[1].toString())));
            myReportTable.addCell(tableCell.get());
            tableCell.set(new PdfPCell(new Phrase(r[2].toString().concat("$"))));
            myReportTable.addCell(tableCell.get());
            tableCell.set(new PdfPCell(new Phrase(r[3] != null ? df.format(
                    Float.parseFloat(r[3].toString()) / 100) : "NULL")));
            myReportTable.addCell(tableCell.get());
        }
        percentRevenueGrowth.forEach(revenue -> {
        });
        PDFReport.add(myReportTable);
        PDFReport.close();
    }
}
