package org.denys.hudymov.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Connection;
import java.sql.SQLException;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class DataSource {

    @Getter
    private static HikariDataSource dataSource = null;

    static {
        HikariConfig config = new HikariConfig("/HotelDB.properties");
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
