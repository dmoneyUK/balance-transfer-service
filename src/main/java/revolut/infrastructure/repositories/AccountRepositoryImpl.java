package revolut.infrastructure.repositories;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import revolut.domain.exception.TransactionException;
import revolut.domain.model.AccountDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static revolut.infrastructure.persistence.H2Utils.getConnection;

@Slf4j
public class AccountRepositoryImpl implements AccountRepository{
    
    private final static String SQL_GET_ACC_BY_ID = "SELECT * FROM Account WHERE AccountNumber = ? ";
    private final static String SQL_LOCK_ACC_BY_ID = "SELECT * FROM Account WHERE AccountNumber = ? FOR UPDATE";
    private final static String SQL_CREATE_ACC = "INSERT INTO Account (AccountNumber, AccountHolder, Balance) VALUES (?, ?, ?)";
    private final static String SQL_UPDATE_ACC_BALANCE = "UPDATE Account SET Balance = ? WHERE AccountNumber = ? ";
    
    @Override
    public AccountDetails findBy(Integer accountNumber) {
        
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
            throw new TransactionException(e);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        
    }
}
