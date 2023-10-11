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
                    "VALUES(?,?,?,?,?,?)";

    @Override
    public void create(Client entity) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)) {
            setParameters(preparedStatement, entity);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
                        .Comment(resultSet.getString("comment"))
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
        Optional<Client> client = Optional.ofNullable(null);
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
                    .Comment(resultSet.getString("comment"))
                    .build());

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public void update(Client entity) {
        try (Connection connection = DataSource.getConnection()) {
            String UPDATE_SQL = "UPDATE Clients SET name=?, surname=?, patronymic=?, passport_data=?, comment=? " +
                    "WHERE client_id=?";
            PreparedStatement preparedStatement =
                    connection.prepareStatement(UPDATE_SQL);

            setParameters(preparedStatement, entity);
            preparedStatement.setLong(6, entity.getClientId());
            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int entityId) {

    }

    private void setParameters(@NotNull PreparedStatement preparedStatement, @NotNull Client entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getSurname());
        preparedStatement.setString(3, entity.getPatronymic());
        preparedStatement.setString(4, entity.getPassportData());
        preparedStatement.setString(5, entity.getComment());
    }
}
