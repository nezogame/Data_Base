package org.denys.hudymov.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.denys.hudymov.entity.Client;

import java.sql.Connection;
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

    @Override
    public void create(Client entity) {

    }

    @Override
    public void create(List<Client> entities) {

    }

    @Override
    public List<Client> read() {
        List<Client> clients = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM clients");
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
    public Optional<List<Client>> get(long id) {

        return Optional.empty();
    }

    @Override
    public void update(Client entity) {

    }

    @Override
    public void update(List<Client> entities) {

    }

    @Override
    public void delete(int entityId) {

    }
}
