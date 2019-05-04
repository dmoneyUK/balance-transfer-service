//package revolut.infrastructure.persistence;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.MockitoAnnotations;
//import revolut.domain.exception.AccountNotAvailableException;
//import revolut.domain.exception.InsufficientFundException;
//import revolut.domain.exception.TransactionException;
//import revolut.domain.model.Account;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.stream.IntStream;
//
//import static java.math.BigDecimal.ONE;
//import static java.math.BigDecimal.TEN;
//import static java.util.stream.Collectors.toList;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.fail;
//
//public class AccountDaoImplITest {
//
//    private static Integer ACCOUNT_MAX_BALANCE = 11111111;
//    private static Integer ACCOUNT_200_BALANCE = 22222222;
//    private static Integer ACCOUNT_500_BALANCE = 33333333;
//
//    private static BigDecimal TWO_HUNDREDS = BigDecimal.valueOf(200).setScale(2);
//    private static BigDecimal FIVE_HUNDREDS = BigDecimal.valueOf(500).setScale(2);
//    private static BigDecimal MAX_BALANCE = BigDecimal.valueOf(9999999999999L).setScale(2);
//
//    private AccountDaoImpl testObj;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        H2DataUtils connectionManager = H2DataUtils.getInstance();
//        testObj = new AccountDaoImpl(connectionManager);
//        connectionManager.populateTestData();
//    }
//
//    @Test
//    public void shouldUpdateBothAccounts_whenTransferSuccessful() {
//
//        testObj.transferBalance(ACCOUNT_200_BALANCE, ACCOUNT_500_BALANCE, TEN);
//
//        Account fromAfterTx = testObj.findBy(ACCOUNT_200_BALANCE);
//        Account toAfterTx = testObj.findBy(ACCOUNT_500_BALANCE);
//
//        assertThat(fromAfterTx.getBalance()).isEqualTo(TWO_HUNDREDS.subtract(TEN));
//        assertThat(toAfterTx.getBalance()).isEqualTo(FIVE_HUNDREDS.add(TEN));
//    }
//
//    @Test
//    public void shouldThrowTransactionExceptionAndNoChangeInBothAccount_whenTargetAccountOverLimit() {
//
//        try {
//            testObj.transferBalance(ACCOUNT_200_BALANCE, ACCOUNT_MAX_BALANCE, TEN);
//            fail("Expected exception was not caught.");
//        }catch (TransactionException ex){
//            assertThat(ex.getMessage()).isEqualTo("Transaction failed and rollback was successful.");
//
//            //verify no change in the accounts
//            Account fromAfterTx = testObj.findBy(ACCOUNT_200_BALANCE);
//            assertThat(fromAfterTx.getBalance()).isEqualTo(TWO_HUNDREDS);
//
//            Account toAfterTx = testObj.findBy(ACCOUNT_MAX_BALANCE);
//            assertThat(toAfterTx.getBalance()).isEqualTo(MAX_BALANCE);
//        }
//
//
//    }
//
//    @Test
//    public void shouldThrowInsufficientFundExceptionAndNoChangeInBothAccount_whenTargetAccountOverLimit() {
//        try {
//            testObj.transferBalance(ACCOUNT_200_BALANCE, ACCOUNT_500_BALANCE, BigDecimal.valueOf(300));
//            fail("Expected exception was not caught.");
//        }catch (InsufficientFundException ex) {
//
//            Account fromAfterTx = testObj.findBy(ACCOUNT_200_BALANCE);
//            assertThat(fromAfterTx.getBalance()).isEqualTo(TWO_HUNDREDS);
//
//            Account toAfterTx = testObj.findBy(ACCOUNT_500_BALANCE);
//            assertThat(toAfterTx.getBalance()).isEqualTo(FIVE_HUNDREDS);
//        }
//    }
//
//    @Test
//    public void shouldThrowAccountNotAvailableExceptionAndNoChangeInBothAccount_whenFaildToFindAccount() {
//        try {
//            testObj.transferBalance(1234567, ACCOUNT_500_BALANCE, BigDecimal.valueOf(300));
//            fail("Expected exception was not caught.");
//        } catch (AccountNotAvailableException ex) {
//
//            Account fromAfterTx = testObj.findBy(ACCOUNT_200_BALANCE);
//            assertThat(fromAfterTx.getBalance()).isEqualTo(TWO_HUNDREDS);
//
//            Account toAfterTx = testObj.findBy(ACCOUNT_500_BALANCE);
//            assertThat(toAfterTx.getBalance()).isEqualTo(FIVE_HUNDREDS);
//        }
//    }
//
//    @Test
//    public void shouldUpdateBothAccountsWith100_when100OnePoundTransfer() throws IOException {
//
//            Runnable restFn = () -> {
//            testObj.transferBalance(ACCOUNT_200_BALANCE, ACCOUNT_500_BALANCE, ONE);
//        };
//
//        List<CompletableFuture> futures = IntStream.range(0, 100)
//                                                   .mapToObj((i) -> CompletableFuture.runAsync(restFn))
//                                                   .collect(toList());
//
//        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
//
//        Account fromAfterTx = testObj.findBy(ACCOUNT_200_BALANCE);
//        Account toAfterTx = testObj.findBy(ACCOUNT_500_BALANCE);
//
//        assertThat(fromAfterTx.getBalance()).isEqualTo(TWO_HUNDREDS.subtract(BigDecimal.valueOf(100)));
//        assertThat(toAfterTx.getBalance()).isEqualTo(FIVE_HUNDREDS.add(BigDecimal.valueOf(100)));
//
//    }
//}
