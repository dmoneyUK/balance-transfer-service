package revolut.infrastructure.persistence;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.h2.tools.RunScript;
import revolut.domain.model.AccountDetails;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class AccountDaoImpl implements AccountDao {
    
    private final static String SQL_GET_ACC_BY_ID = "SELECT * FROM Account WHERE AccountNumber = ? ";
    private final static String SQL_LOCK_ACC_BY_ID = "SELECT * FROM Account WHERE AccountNumber = ? FOR UPDATE";
    private final static String SQL_CREATE_ACC = "INSERT INTO Account (AccountNumber, AccountHolder, Balance) VALUES (?, ?, ?)";
    private final static String SQL_UPDATE_ACC_BALANCE = "UPDATE Account SET Balance = ? WHERE AccountNumber = ? ";
    
    public AccountDaoImpl() throws Exception {
        DbUtils.loadDriver("org.h2.Driver");
        populateTestData();
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
    
    @Override
    public AccountDetails findBy(Integer accountNumber) throws SQLException, RuntimeException {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        AccountDetails acc = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_GET_ACC_BY_ID);
            stmt.setLong(1, accountNumber);
            rs = stmt.executeQuery();
            if (rs.next()) {
                acc = AccountDetails.builder()
                                    .accountNumber(rs.getInt("AccountNumber"))
                                    .accountHolder(rs.getString("AccountHolder"))
                                    .balance(rs.getBigDecimal("Balance"))
                                    .build();
                if (log.isDebugEnabled()) {
                    log.debug("Retrieve Account By Id: " + acc);
                }
            }
            return acc;
        } catch (SQLException e) {
            conn.rollback();
            throw new RuntimeException("Error" , e);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        
    }
    
}
