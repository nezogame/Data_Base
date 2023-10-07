package org.denys.hudymov;

import org.denys.hudymov.repository.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class App {
    public static void main(String[] args) throws Exception {
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM clients");
            while (resultSet.next()) {
                System.out.println("client id:" + resultSet.getInt("client_id"));
                System.out.println("surname:" + resultSet.getString("surname"));
                System.out.println("empName:" + resultSet.getString("name"));
                System.out.println("patronymic:" + resultSet.getString("patronymic"));
                System.out.println("passport data:" + resultSet.getString("passport_data"));
                System.out.println("comment:" + resultSet.getString("comment"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}