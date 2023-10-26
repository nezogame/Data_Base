package org.denys.hudymov.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.denys.hudymov.entity.Client;
import org.jetbrains.annotations.NotNull;

@Data
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ClientDao implements Dao<Client> {
    private final String INSERT_SQL =
            "INSERT INTO Clients " +
                    "VALUES(?,?,?,?,?)";

    private final String UPDATE_SQL = "UPDATE Clients SET surname=?, name=?, " +
            "patronymic=?, passport_data=?, comment=? " +
            "WHERE client_id=?";

    private final String DELETE_SQL = "DELETE FROM  Clients " +
            "WHERE client_id=?";

    @Override
    public void create(Client entity) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(INSERT_SQL)) {

            setParameters(preparedStatement, entity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    @Override
    public List<Client> read() {
        List<Client> clients = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Clients");
            while (resultSet.next()) {
                Client client = Client
                        .builder()
                        .clientId(resultSet.getLong("client_id"))
                        .surname(resultSet.getString("surname"))
                        .name(resultSet.getString("name"))
                        .patronymic(resultSet.getString("patronymic"))
                        .passportData(resultSet.getString("passport_data"))
                        .comment(resultSet.getString("comment"))
                        .build();
                clients.add(client);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public Optional<Client> get(long id) {
        Optional<Client> client = Optional.empty();
        String readSql = "SELECT * FROM Clients " +
                "WHERE client_id=?";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSql)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            client = Optional.ofNullable(Client
                    .builder()
                    .clientId(resultSet.getLong("client_id"))
                    .surname(resultSet.getString("surname"))
                    .name(resultSet.getString("name"))
                    .patronymic(resultSet.getString("patronymic"))
                    .passportData(resultSet.getString("passport_data"))
                    .comment(resultSet.getString("comment"))
                    .build());

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public void update(Client entity) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            setParameters(preparedStatement, entity);
            preparedStatement.setLong(6, entity.getClientId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int entityId) throws SQLIntegrityConstraintViolationException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            preparedStatement.setLong(1, entityId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLIntegrityConstraintViolationException(e.getMessage());
        }
    }

    private void setParameters(@NotNull PreparedStatement preparedStatement, @NotNull Client entity) throws SQLException {
        preparedStatement.setString(1, entity.getSurname());
        preparedStatement.setString(2, entity.getName());
        preparedStatement.setString(3, entity.getPatronymic());
        preparedStatement.setString(4, entity.getPassportData());
        preparedStatement.setString(5, entity.getComment());
    }

    public List<Long> getAllId() {
        List<Long> clientsId = new ArrayList<>();
        String selectSQL = "SELECT client_id FROM Clients " +
                "ORDER BY client_id";
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                clientsId.add(resultSet.getLong("client_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientsId;
    }

    public List<String> getAllPassportCode() {
        List<String> clientsPassport = new ArrayList<>();
        String selectSQL = "SELECT passport_data FROM Clients " +
                "ORDER BY passport_data";
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                clientsPassport.add(resultSet.getString("passport_data"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientsPassport;
    }

    public Optional<Client> getByPassport(String passport) {
        Optional<Client> client = Optional.empty();
        try (Connection connection = DataSource.getConnection()) {
            String SelectQuery = "SELECT * FROM Clients WHERE passport_data=?";
            PreparedStatement preparedStatement =
                    connection.prepareStatement(SelectQuery);
            preparedStatement.setString(1, passport);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) { // Check if there is a result

                client = Optional.of(Client
                        .builder()
                        .clientId(resultSet.getLong("client_id"))
                        .surname(resultSet.getString("surname"))
                        .name(resultSet.getString("name"))
                        .patronymic(resultSet.getString("patronymic"))
                        .passportData(resultSet.getString("passport_data"))
                        .comment(resultSet.getString("comment"))
                        .build());
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    public Map<Integer, Client> getUsersAndDayTheySpendByPassportCode(String code) {
        Map<Integer, Client> clientWithDays = new HashMap<>();

        String selectQuery = "SELECT c.passport_data, c.name, c.surname, c.patronymic, " +
                "DateDiff(dd, accom.arrival_date,accom.departure_date) as days_spend " +
                "FROM Clients as c " +
                "INNER JOIN HotelAccommodations as accom " +
                "ON c.client_id = accom.client_id " +
                "WHERE c.passport_data = ?";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setString(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                clientWithDays.put(
                        resultSet.getInt("days_spend"),
                        Client
                                .builder()
                                .surname(resultSet.getString("surname"))
                                .name(resultSet.getString("name"))
                                .patronymic(resultSet.getString("patronymic"))
                                .passportData(resultSet.getString("passport_data"))
                                .build()
                );
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientWithDays;
    }

    public List<Client> displayGuestsInTheHotel() {
        List<Client> clients = new ArrayList<>();

        String selectSQL = "SELECT c.surname, c.name, c.patronymic, c.passport_data, c.comment " +
                "FROM Clients c " +
                "WHERE EXISTS ( " +
                "    SELECT 1 " +
                "    FROM HotelAccommodations a " +
                "    WHERE a.client_id = c.client_id " +
                "    AND a.departure_date > CURRENT_TIMESTAMP " +
                ")";
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                clients.add(
                        Client
                                .builder()
                                .surname(resultSet.getString("surname"))
                                .name(resultSet.getString("name"))
                                .patronymic(resultSet.getString("patronymic"))
                                .passportData(resultSet.getString("passport_data"))
                                .comment(resultSet.getString("comment"))
                                .build()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public Map<Client, Integer> avgDaysAtHotel() {
        Map<Client, Integer> clientsWithDays = new LinkedHashMap<>();

        String selectSQL = "SELECT c.passport_data, c.surname, c.name, c.patronymic, " +
                "AVG(DATEDIFF(day, a.arrival_date, a.departure_date)) AS avg_stay_duration " +
                "FROM Clients c " +
                "LEFT JOIN HotelAccommodations a ON c.client_id = a.client_id " +
                "GROUP BY c.passport_data, c.surname, c.name, c.patronymic " +
                "ORDER BY c.passport_data";
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                clientsWithDays.put(
                        Client
                                .builder()
                                .surname(resultSet.getString("surname"))
                                .name(resultSet.getString("name"))
                                .patronymic(resultSet.getString("patronymic"))
                                .passportData(resultSet.getString("passport_data"))
                                .build(),
                        resultSet.getInt("avg_stay_duration")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientsWithDays;
    }

    public Map<Client, Integer> findClientsPlacementForLastYear() {
        Map<Client, Integer> clientsForLastYear = new HashMap<>();

        String selectQuery = "SELECT c.passport_data, c.surname, c.name, c.patronymic, " +
                "COUNT(*) AS active_accommodations " +
                "    FROM Clients c " +
                "    JOIN HotelAccommodations a ON c.client_id = a.client_id " +
                "    WHERE a.arrival_date >= DATEADD(YEAR, -1, CURRENT_TIMESTAMP) " +
                "    GROUP BY c.passport_data, c.client_id, c.surname, c.name, c.patronymic";
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            while (resultSet.next()) {
                clientsForLastYear.put(
                        Client
                                .builder()
                                .surname(resultSet.getString("surname"))
                                .name(resultSet.getString("name"))
                                .patronymic(resultSet.getString("patronymic"))
                                .passportData(resultSet.getString("passport_data"))
                                .build(),
                        resultSet.getInt("active_accommodations")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientsForLastYear;
    }

    public List<Client> findClientsByNumberOfHotelStays(Integer minAccommodation) {
        List<Client> clientsForLastYear = new ArrayList<>();

        String selectSQL = "SELECT c.passport_data, c.surname, c.name, c.patronymic, c.comment  " +
                "FROM Clients c " +
                "WHERE ( " +
                "    SELECT COUNT(*) " +
                "    FROM HotelAccommodations a " +
                "    WHERE a.client_id = c.client_id " +
                ") >= ?";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
        ) {
            preparedStatement.setInt(1, minAccommodation);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                clientsForLastYear.add(
                        Client.builder()
                                .surname(resultSet.getString("surname"))
                                .name(resultSet.getString("name"))
                                .patronymic(resultSet.getString("patronymic"))
                                .passportData(resultSet.getString("passport_data"))
                                .comment(resultSet.getString("comment"))
                                .build()
                );
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientsForLastYear;
    }
}
