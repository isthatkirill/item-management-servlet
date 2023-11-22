package isthatkirill.itemmanagement.repository.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Kirill Emelyanov
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionHelper {

    private static PGSimpleDataSource dataSource;

    @SneakyThrows
    public static DataSource getDataSource() {
        if (dataSource != null) {
            return dataSource;
        }
        dataSource = new PGSimpleDataSource();
        Properties prop = new Properties();
        InputStream input = ConnectionHelper.class.getClassLoader().getResourceAsStream("configuration.properties");
        prop.load(input);

        dataSource.setUrl(prop.getProperty("db.url"));
        dataSource.setUser(prop.getProperty("db.user"));
        dataSource.setPassword(prop.getProperty("db.password"));
        dataSource.setPortNumbers(new int[]{5432});

        return dataSource;
    }

}
