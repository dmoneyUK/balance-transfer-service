package revolut.infrastructure.persistence;

import com.atomikos.icatch.jta.UserTransactionImp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.h2.tools.RunScript;
import revolut.domain.exception.TransactionException;
import revolut.domain.model.AccountDetails;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Slf4j
public class AccountDaoImpl implements AccountDao {
    
    private final static String SQL_GET_ACC_BY_ID = "SELECT * FROM Account WHERE AccountNumber = ? ";
    private final static String SQL_LOCK_ACC_BY_ID = "SELECT * FROM Account WHERE AccountNumber = ? FOR UPDATE";
    private final static String SQL_CREATE_ACC = "INSERT INTO Account (AccountNumber, AccountHolder, Balance) VALUES (?, ?, ?)";
    private final static String SQL_UPDATE_ACC_BALANCE = "UPDATE Account SET Balance = ? WHERE AccountNumber = ? ";
    
    private DataSource ds;
    
    @Inject
    public AccountDaoImpl(DataSource ds) {
        this.ds = ds;
        populateTestData();
    }
    
    @Override
    public AccountDetails findBy(Integer accountNumber) {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        AccountDetails acc = null;
        try {
            conn = ds.getConnection();
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
            throw new TransactionException(e);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        
    }
    
    @Override
    public int updateAccount(Integer accountNumber, BigDecimal newBalance) throws Exception {
        
        UserTransaction utx = new UserTransactionImp();
        boolean error = false;
        try {
            utx.begin();
            
            Connection conn = null;
            PreparedStatement lockStmt = null;
            PreparedStatement updateStmt = null;
            ResultSet rs = null;
            int updateCount = -1;
            try {
                conn = ds.getConnection();
                conn.setAutoCommit(false);
                // lock account for writing:
                lockStmt = conn.prepareStatement(SQL_LOCK_ACC_BY_ID);
                lockStmt.setLong(1, accountNumber);
                rs = lockStmt.executeQuery();
                AccountDetails targetAccount = null;
                if (rs.next()) {
                    targetAccount = new AccountDetails(rs.getInt("accountNumber"), rs.getString("accountHolder"),
                                                       rs.getBigDecimal("Balance"));
                    if (log.isDebugEnabled()) {
                        log.debug("updateAccountBalance from Account: " + targetAccount);
                    }
                }
                
                if (targetAccount == null) {
                    throw new TransactionException("updateAccountBalance(): fail to lock account : " + accountNumber);
                }
                // update account upon success locking
                updateStmt = conn.prepareStatement(SQL_UPDATE_ACC_BALANCE);
                updateStmt.setBigDecimal(1, newBalance);
                updateStmt.setInt(2, accountNumber);
                updateCount = updateStmt.executeUpdate();
                if (log.isDebugEnabled()) {
                    log.debug("New Balance after Update: " + targetAccount);
                }
                return updateCount;
            } catch (SQLException se) {
                // rollback transaction if exception occurs
                log.error("updateAccountBalance(): User Transaction Failed, rollback initiated for: " + accountNumber, se);
                try {
                    if (conn != null) {
                        conn.rollback();
                    }
                } catch (SQLException re) {
                    error = true;
                    throw new TransactionException("Fail to rollback transaction", re);
                }
            } finally {
                DbUtils.closeQuietly(conn);
                DbUtils.closeQuietly(rs);
                DbUtils.closeQuietly(lockStmt);
                DbUtils.closeQuietly(updateStmt);
            }
            return updateCount;
        } catch (Exception e) {
            error = true;
            throw e;
        } finally {
            
            if (error) {
                utx.rollback();
            } else {
                utx.commit();
            }
        }
    }
    
    
    private void populateTestData() {
        log.info("Populating Test User Table and data ..... ");
        Connection conn = null;
        try {
            conn = ds.getConnection();
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
