package revolut.infrastructure.persistence;

import lombok.extern.slf4j.Slf4j;
import revolut.domain.exception.TransactionException;
import revolut.domain.model.Account;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
public class AccountDaoImpl implements AccountDao {
    
    private final static String SQL_GET_BY_ACC_NUMBER = "SELECT ac FROM Account ac WHERE (ac.accountNumber = :accountNumber)";
    private final static String SQL_UPDATE_ACC_BALANCE = "UPDATE Account ac SET ac.balance = :balance WHERE ac.accountNumber = :accountNumber";
    
    private EntityManagerFactory entityManagerFactory;
    
    @Inject
    public AccountDaoImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    
    @Override
    public Account findBy(Integer accountNumber) throws TransactionException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
    
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery(SQL_GET_BY_ACC_NUMBER, Account.class);
            query.setParameter("accountNumber", accountNumber);
            List<Account> resultList = query.getResultList();
    
            return resultList.get(0);
        } catch (Exception e) {
            log.error("Failed target retrieve account with accoutNumber: {}", accountNumber);
            throw new TransactionException("Exception when reading DB", e);
        } finally {
            entityManager.getTransaction().commit();
        }
    
    }
    
    @Override
    public int updateBalance(Integer accountNumber, BigDecimal update) {
    
        int updateCount = -1;
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
    
            // lock account for writing:
            Account target = getAccount(entityManager, accountNumber);
    
            if (target == null) {
                throw new TransactionException("Failed target retrieve account with accoutNumber: " + accountNumber);
            }
            // update account upon success locking
            BigDecimal balance = target.getBalance().add(update);
    
            Query updateQuery = entityManager.createQuery(SQL_UPDATE_ACC_BALANCE);
            updateQuery.setParameter("balance", balance);
            updateQuery.setParameter("accountNumber", accountNumber);
            updateCount = updateQuery.executeUpdate();
    
            entityManager.getTransaction().commit();
    
            log.info("New Balance after Update: " + balance);
            return updateCount;
        } catch (Exception se) {
            // rollback transaction if exception occurs
            log.error("updateAccountBalance(): User Transaction Failed, rollback initiated for: " + accountNumber, se);
            entityManager.getTransaction().rollback();
            throw new TransactionException("Transaction failed and rollback was successful.");
        }
    
    }
    
    private Account getAccount(EntityManager entityManager, Integer accountNumber) {
        Query retrieveQuery = entityManager.createQuery(SQL_GET_BY_ACC_NUMBER, Account.class);
        retrieveQuery.setParameter("accountNumber", accountNumber);
        retrieveQuery.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        List<Account> resultList = retrieveQuery.getResultList();
        return resultList.get(0);
    }
    //
    //@Override
    //public void transferBalance(final Integer sourceAccountNumber, final Integer targetAccountNumber,
    //                            final BigDecimal transactionAmount) throws TransactionException {
    //
    //    Optional<Account> sourceAccountOpt = Optional.empty();
    //    Optional<Account> toAccountOpt = Optional.empty();
    //
    //    try {
    //        entityManager.getTransaction().begin();
    //
    //        // lock the credit and debit account for writing:
    //        Query query = entityManager.createQuery(SQL_LOCK_ACC_BY_ID, Account.class);
    //        query.setParameter(1, sourceAccountNumber);
    //        List<Account> resultList = query.getResultList();
    //        sourceAccountOpt = Optional.of(resultList.get(0));
    //        log.info("Found source account: {}", sourceAccountNumber);
    //
    //        query = entityManager.createQuery(SQL_LOCK_ACC_BY_ID, Account.class);
    //        query.setParameter(1, targetAccountNumber);
    //        resultList = query.getResultList();
    //        toAccountOpt = Optional.of(resultList.get(0));
    //        log.info("Found target account: {}", targetAccountNumber);
    //
    //
    //        Account sourceAccount = sourceAccountOpt.orElseThrow(
    //                () -> new AccountNotAvailableException("Failed to access to the account: " + sourceAccountNumber));
    //        Account targetAccount = toAccountOpt.orElseThrow(
    //                () -> new AccountNotAvailableException("Failed to access to the account: " + targetAccountNumber));
    //
    //
    //        // check enough fund in source account
    //        BigDecimal sourceAccountBalance = sourceAccount.getBalance();
    //        BigDecimal targetAccountBalance = targetAccount.getBalance();
    //
    //        if (sourceAccountBalance.compareTo(transactionAmount) < 0) {
    //            throw new InsufficientFundException("No sufficient fund in the account: " + sourceAccountNumber);
    //        }
    //
    //        // proceed with update
    //        updateStmt = conn.prepareStatement(SQL_UPDATE_ACC_BALANCE);
    //        updateStmt.setBigDecimal(1, sourceAccountBalance.subtract(transactionAmount));
    //        updateStmt.setInt(2, sourceAccountNumber);
    //        updateStmt.addBatch();
    //        updateStmt.setBigDecimal(1, targetAccountBalance.add(transactionAmount));
    //        updateStmt.setInt(2, targetAccountNumber);
    //        updateStmt.addBatch();
    //        int[] rowsUpdated = updateStmt.executeBatch();
    //        log.debug("Number of rows updated for the transfer : " + rowsUpdated[0] + rowsUpdated[1]);
    //        // If there is no error, commit the transaction
    //        conn.commit();
    //    } catch (SQLException se) {
    //        // rollback transaction if exception occurs
    //        log.error("Transfer transaction failed, rollback.", se);
    //        try {
    //            if (conn != null) {
    //                conn.rollback();
    //                throw new TransactionException("Transaction failed and rollback was successful.");
    //            }
    //        } catch (SQLException re) {
    //            throw new UnrecoverableException("Transaction failed and could not rollback.", re);
    //        }
    //    } finally {
    //        DbUtils.closeQuietly(conn);
    //        DbUtils.closeQuietly(rs);
    //        DbUtils.closeQuietly(lockStmt);
    //        DbUtils.closeQuietly(updateStmt);
    //    }
    //}
    //
}
