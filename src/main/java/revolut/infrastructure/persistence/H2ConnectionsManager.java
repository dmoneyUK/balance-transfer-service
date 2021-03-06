package revolut.infrastructure.persistence;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class H2ConnectionsManager {
    
    private static H2ConnectionsManager instance;
    
    private H2ConnectionsManager() {
        DbUtils.loadDriver("org.h2.Driver");
    }
    
    public static H2ConnectionsManager getInstance() {
        if (instance == null) {
            synchronized (H2ConnectionsManager.class) {
                if (instance == null) {
                    instance = new H2ConnectionsManager();
                }
            }
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:mem:revolut;DB_CLOSE_DELAY=-1", "sa", "sa");
    }
    
    public void populateTestData() {
        log.info("Populating Test User Table and data ..... ");
        Connection conn = null;
        try {
            conn = getConnection();
            RunScript.execute(conn, new FileReader("src/main/resources/demo.sql"));
        } catch (SQLException e) {
            log.error("populateTestData(): Error populating user data: ", e);
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            log.error("populateTestData(): Error finding test script file ", e);
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }
}
