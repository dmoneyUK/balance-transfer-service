package revolut.infrastructure.persistence;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import revolut.domain.exception.InsufficientFundException;
import revolut.domain.exception.TransationException;
import revolut.domain.model.Account;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Slf4j
public class AccountDaoImpl implements AccountDao {
    
    private final static String SQL_GET_ACC_BY_ID = "SELECT * FROM Account WHERE AccountNumber = ? ";
    private final static String SQL_LOCK_ACC_BY_ID = "SELECT * FROM Account WHERE AccountNumber = ? FOR UPDATE";
    private final static String SQL_CREATE_ACC = "INSERT INTO Account (AccountNumber, AccountHolder, Balance) VALUES (?, ?, ?)";
    private final static String SQL_UPDATE_ACC_BALANCE = "UPDATE Account SET Balance = ? WHERE AccountNumber = ? ";
    
    private H2ConnectionsManager connectionManager;
    
    @Inject
    public AccountDaoImpl(H2ConnectionsManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    
    @Override
    public Account findBy(Integer accountNumber) throws TransationException {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Account acc = null;
        try {
            conn = connectionManager.getConnection();
            stmt = conn.prepareStatement(SQL_GET_ACC_BY_ID);
            stmt.setLong(1, accountNumber);
            rs = stmt.executeQuery();
            if (rs.next()) {
                acc = Account.builder()
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
            log.error("Failed to retrieve account with accoutNumber: {}", accountNumber);
            throw new TransationException("Exception when reading DB", e);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
    
    }
    
    @Override
    public void transferBalance(Integer fromAccountNumber, Integer toAccountNumber, BigDecimal transferAmount) throws TransationException {
        Connection conn = null;
        PreparedStatement lockStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;
        Optional<Account> fromAccount = Optional.empty();
        Optional<Account> toAccount = Optional.empty();
        
        try {
            conn = connectionManager.getConnection();
            conn.setAutoCommit(false);
            
            // lock the credit and debit account for writing:
            lockStmt = conn.prepareStatement(SQL_LOCK_ACC_BY_ID);
            lockStmt.setInt(1, fromAccountNumber);
            rs = lockStmt.executeQuery();
            if (rs.next()) {
                fromAccount = Optional.ofNullable(Account.builder()
                                                         .accountNumber(rs.getInt("AccountNumber"))
                                                         .accountHolder(rs.getString("AccountHolder"))
                                                         .balance(rs.getBigDecimal("Balance"))
                                                         .build());
                log.debug("transferAccountBalance from Account: {}", fromAccount);
            }
            
            lockStmt = conn.prepareStatement(SQL_LOCK_ACC_BY_ID);
            lockStmt.setInt(1, toAccountNumber);
            rs = lockStmt.executeQuery();
            if (rs.next()) {
                toAccount = Optional.ofNullable(Account.builder()
                                                       .accountNumber(rs.getInt("AccountNumber"))
                                                       .accountHolder(rs.getString("AccountHolder"))
                                                       .balance(rs.getBigDecimal("Balance"))
                                                       .build());
                log.debug("transferAccountBalance to Account: {}", toAccount);
            }
            
            // check locking status
            if (!(fromAccount.isPresent() && toAccount.isPresent())) {
                throw new TransationException("Fail to lock both accounts for write");
            }
            
            // check enough fund in source account
            BigDecimal fromAccountbalance = fromAccount.get().getBalance();
            BigDecimal toAccountbalance = toAccount.get().getBalance();
            if (fromAccountbalance.compareTo(transferAmount) < 0) {
                throw new InsufficientFundException("Not enough fund in Account.");
            }
            
            // proceed with update
            updateStmt = conn.prepareStatement(SQL_UPDATE_ACC_BALANCE);
            updateStmt.setBigDecimal(1, fromAccountbalance.subtract(transferAmount));
            updateStmt.setInt(2, fromAccountNumber);
            updateStmt.addBatch();
            updateStmt.setBigDecimal(1, toAccountbalance.add(transferAmount));
            updateStmt.setInt(2, toAccountNumber);
            updateStmt.addBatch();
            int[] rowsUpdated = updateStmt.executeBatch();
            log.debug("Number of rows updated for the transfer : " + rowsUpdated[0] + rowsUpdated[1]);
            // If there is no error, commit the transaction
            conn.commit();
        } catch (SQLException se) {
            // rollback transaction if exception occurs
            log.error("Transfer transaction failed, rollback.", se);
            try {
                if (conn != null) {
                    conn.rollback();
                    throw new TransationException("Transaction failed and rollback was successful.");
                }
            } catch (SQLException re) {
                throw new TransationException("Fail to rollback transaction.", re);
            }
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(lockStmt);
            DbUtils.closeQuietly(updateStmt);
        }
    }
    
}
