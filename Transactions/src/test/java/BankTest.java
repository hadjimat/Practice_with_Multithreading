import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class BankTest {

    private Bank bank;

    @Before
    public void init() {
        HashMap<String, Account> accounts = new HashMap<>();
        accounts.put("0",new Account("0", 15000));
        accounts.put("1" , new Account("1", 130000));
        accounts.put("2", new Account("2", 2525000));
        Account blockedAccount = new Account("3", 15200);
        blockedAccount.setBlocked(true);
        accounts.put("3", blockedAccount);
        bank = new Bank(accounts);
    }


    @Test
    public void whenAccountIsNotExistThenReturnNull() {
        assertNull(bank.getAccount("12"));
    }


    @Test
    public void getAccountBalanceTest() {
        assertEquals(15000, bank.getAccountBalance("0"));
    }


    @Test
    public void notExistsAccountGetBalanceTest() {
        assertEquals(0, bank.getAccountBalance("24"));
    }


    @Test
    public void blockedAccountGetBalanceTest() {
        assertEquals(15200, bank.getAccountBalance("3"));
    }


    @Test
    public void legalTransferTest() throws Exception {
        bank.transfer("0", "1", 7500);
        assertEquals(7500, bank.getAccount("0").getMoney().get());
        assertEquals(137500, bank.getAccount("1").getMoney().get());
    }


    @Test
    public void transferFromBlockedAccountTest() throws Exception {
        bank.transfer("3", "0", 7500);
        assertEquals(15200, bank.getAccount("3").getMoney().get());
        assertEquals(15000, bank.getAccount("0").getMoney().get());
    }


    @Test
    public void transferToBlockedAccountTest() throws Exception {
        bank.transfer("0", "3", 7500);
        assertEquals(15200, bank.getAccount("3").getMoney().get());
        assertEquals(15000, bank.getAccount("0").getMoney().get());
    }


    @Test
    public void transferZeroAmountTest() throws Exception {
        bank.transfer("0","1",0);
        assertEquals(15000, bank.getAccount("0").getMoney().get());
        assertEquals(130000, bank.getAccount("1").getMoney().get());
    }


    @Test
    public void transferNegativeAmountTest() throws Exception {
        bank.transfer("1","2",-500);
        assertEquals(15000, bank.getAccount("0").getMoney().get());
        assertEquals(130000, bank.getAccount("1").getMoney().get());
    }
}
