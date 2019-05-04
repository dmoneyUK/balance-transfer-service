//package revolut.domain.service;
//
//import org.junit.Before;
//import org.junit.Test;
//import revolut.domain.model.Account;
//import revolut.infrastructure.framework.TransactionWrapper;
//import revolut.infrastructure.persistence.AccountDao;
//import revolut.infrastructure.persistence.AccountDaoImpl;
//import revolut.infrastructure.persistence.H2DataUtils;
//import revolut.infrastructure.persistence.JpaEntityManagerFactory;
//import revolut.infrastructure.rest.entity.TransferTransactionRequest;
//
//import javax.persistence.EntityManager;
//import java.math.BigDecimal;
//
//import static java.math.BigDecimal.TEN;
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class TransactionalTransferTransactionServiceImplTest {
//
//    private TransferTransactionService testObj;
//    private AccountDao accountDao;
//
//    private static Integer ACCOUNT_MAX_BALANCE = 11111111;
//    private static Integer ACCOUNT_200_BALANCE = 22222222;
//    private static Integer ACCOUNT_500_BALANCE = 33333333;
//
//    private static BigDecimal TWO_HUNDREDS = BigDecimal.valueOf(200).setScale(2);
//    private static BigDecimal FIVE_HUNDREDS = BigDecimal.valueOf(500).setScale(2);
//    private static BigDecimal MAX_BALANCE = BigDecimal.valueOf(9999999999999L).setScale(2);
//
//    @Before
//    public void setUp() {
//
//    }
//
//    @Test
//    public void process() {
//        H2DataUtils.populateTestData();
//        EntityManager em = JpaEntityManagerFactory.getInstance();
//        accountDao = new AccountDaoImpl(em);
//        testObj = TransactionWrapper.transactionally(em, new TransferTransactionServiceImpl(accountDao));
//
//        TransferTransactionRequest request = TransferTransactionRequest.builder()
//                                                                       .source(ACCOUNT_200_BALANCE)
//                                                                       .target(ACCOUNT_500_BALANCE)
//                                                                       .amount(BigDecimal.TEN)
//                                                                       .build();
//        testObj.process(request);
//
//        Account fromAfterTx = accountDao.findBy(ACCOUNT_200_BALANCE);
//        Account toAfterTx = accountDao.findBy(ACCOUNT_500_BALANCE);
//
//        assertThat(fromAfterTx.getBalance()).isEqualTo(TWO_HUNDREDS.subtract(TEN));
//        assertThat(toAfterTx.getBalance()).isEqualTo(FIVE_HUNDREDS.add(TEN));
//
//    }
//}
