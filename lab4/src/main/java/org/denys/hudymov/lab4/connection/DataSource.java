package org.denys.hudymov.lab4.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class DataSource {

    @Getter
    private static HikariDataSource dataSource = null;

    static {
        HikariConfig config = new HikariConfig("C:\\Users\\Denys\\My learning\\3_Course\\Data_Base\\lab4\\src\\main\\resources\\EmployeeAccountingDB.properties");
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
