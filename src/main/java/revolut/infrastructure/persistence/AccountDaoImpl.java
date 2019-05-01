package revolut.infrastructure.persistence;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import revolut.domain.exception.AccountNotAvailableException;
import revolut.domain.exception.InsufficientFundException;
import revolut.domain.exception.TransactionException;
import revolut.domain.exception.UnrecoverableException;
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
    
    private final static String SQL_GET_ACC_BY_ID = "SELECT * from Account WHERE AccountNumber = ? ";
    private final static String SQL_LOCK_ACC_BY_ID = "SELECT * from Account WHERE AccountNumber = ? FOR UPDATE";
    private final static String SQL_UPDATE_ACC_BALANCE = "UPDATE Account SET Balance = ? WHERE AccountNumber = ? ";
    
    private H2ConnectionsManager connectionManager;
    
    @Inject
    public AccountDaoImpl(H2ConnectionsManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    
    @Override
    public Account findBy(Integer accountNumber) throws TransactionException {
        
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
            log.error("Failed target retrieve account with accoutNumber: {}", accountNumber);
            throw new TransactionException("Exception when reading DB", e);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
    
    }
    
    @Override
    public void transferBalance(final Integer sourceAccountNumber, final Integer targetAccountNumber,
                                final BigDecimal transactionAmount) throws TransactionException {
        Connection conn = null;
        PreparedStatement lockStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;
        Optional<Account> sourceAccountOpt = Optional.empty();
        Optional<Account> toAccountOpt = Optional.empty();
        
        try {
            conn = connectionManager.getConnection();
            conn.setAutoCommit(false);
            
            // lock the credit and debit account for writing:
            lockStmt = conn.prepareStatement(SQL_LOCK_ACC_BY_ID);
            lockStmt.setInt(1, sourceAccountNumber);
            rs = lockStmt.executeQuery();
            if (rs.next()) {
                sourceAccountOpt = Optional.of(Account.builder()
                                                      .accountNumber(rs.getInt("AccountNumber"))
                                                      .accountHolder(rs.getString("AccountHolder"))
                                                      .balance(rs.getBigDecimal("Balance"))
                                                      .build());
                log.debug("Found source account: {}", sourceAccountOpt);
    
            }
    
            lockStmt = conn.prepareStatement(SQL_LOCK_ACC_BY_ID);
            lockStmt.setInt(1, targetAccountNumber);
            rs = lockStmt.executeQuery();
            if (rs.next()) {
                toAccountOpt = Optional.of(Account.builder()
                                                  .accountNumber(rs.getInt("AccountNumber"))
                                                  .accountHolder(rs.getString("AccountHolder"))
                                                  .balance(rs.getBigDecimal("Balance"))
                                                  .build());
                log.debug("Found target Account: {}", toAccountOpt);
            }
    
            Account sourceAccount = sourceAccountOpt.orElseThrow(
                    () -> new AccountNotAvailableException("Failed to access to the account: " + sourceAccountNumber));
            Account targetAccount = toAccountOpt.orElseThrow(
                    () -> new AccountNotAvailableException("Failed to access to the account: " + targetAccountNumber));
            
            
            // check enough fund in source account
            BigDecimal sourceAccountBalance = sourceAccount.getBalance();
            BigDecimal targetAccountBalance = targetAccount.getBalance();
    
            if (sourceAccountBalance.compareTo(transactionAmount) < 0) {
                throw new InsufficientFundException("No sufficient fund in the account: " + sourceAccountNumber);
            }
            
            // proceed with update
            updateStmt = conn.prepareStatement(SQL_UPDATE_ACC_BALANCE);
            updateStmt.setBigDecimal(1, sourceAccountBalance.subtract(transactionAmount));
            updateStmt.setInt(2, sourceAccountNumber);
            updateStmt.addBatch();
            updateStmt.setBigDecimal(1, targetAccountBalance.add(transactionAmount));
            updateStmt.setInt(2, targetAccountNumber);
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
                    throw new TransactionException("Transaction failed and rollback was successful.");
                }
            } catch (SQLException re) {
                throw new UnrecoverableException("Transaction failed and could not rollback.", re);
            }
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(lockStmt);
            DbUtils.closeQuietly(updateStmt);
        }
    }
    
}
