package org.denys.hudymov.repository;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.denys.hudymov.entity.Client;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ClientDao implements Dao<Client> {
    private final String INSERT_SQL =
            "INSERT INTO Clients " +
                    "VALUES(?,?,?,?,?)";

    private final String UPDATE_SQL = "UPDATE Clients SET surname=?, name=?, patronymic=?, passport_data=?, comment=? " +
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
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String READ_SQL = "SELECT * FROM Clients " +
                    "WHERE client_id=?";
            ResultSet resultSet = statement.executeQuery(READ_SQL);
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
    public void delete(int entityId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            preparedStatement.setLong(1, entityId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
}
