import lombok.extern.log4j.Log4j2;
import exceptions.AccountNotExistsException;
import exceptions.BlockedAccountException;
import exceptions.IdenticalAccountsException;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;


@Log4j2
public class Bank {

    private final Random random = new Random();
    private HashMap<String, Account> accounts;

    public Bank(HashMap<String, Account> accountSet) {
        accounts = accountSet;
    }


    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum)
            throws InterruptedException {
        log.info("операция {} отправлена на проверку.", UUID.randomUUID().toString());
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    public Account getAccount(String accountId) {
        try {
            if (isAccountExists(accountId))
                return accounts.get(accountId);
        } catch (AccountNotExistsException ane) {
            System.out.println(ane.getMessage());
        }
        return null;
    }


    public void transfer( String senderAccount, String recipientAccount, long amount) throws Exception {
        Account firstLock;
        Account secondLock;

        if (senderAccount.equals(recipientAccount)) {
            throw new IdenticalAccountsException();
        }
        else if (Integer.parseInt(accounts.get(senderAccount).getAccNumber()) < Integer.parseInt(accounts.get(recipientAccount).getAccNumber())) {
            firstLock = accounts.get(senderAccount);
            secondLock = accounts.get(recipientAccount);
        }
        else {
            firstLock = accounts.get(recipientAccount);
            secondLock = accounts.get(senderAccount);
        }

        synchronized (firstLock) {
            synchronized (secondLock) {
                try {
                    if (getAccount(recipientAccount).isBlocked()) {
                        throw new BlockedAccountException(recipientAccount);
                    }
                    getAccount(senderAccount).withdraw(amount);
                    getAccount(recipientAccount).replenish(amount);
                    if (amount > 50000) {
                        if (isFraud(senderAccount, recipientAccount)) {
                            getAccount(senderAccount).setBlocked(true);
                            getAccount(recipientAccount).setBlocked(true);
                            log.warn("Операция не прошла проверку.\nСчета заблокированы.");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public long getAccountBalance(String accountId) {
        try {
            return getAccount(accountId).getMoney().get();
        } catch (NullPointerException npe) {
            return 0;
        }
    }


    private boolean isAccountExists(String accountId) throws AccountNotExistsException {
        if (accounts.containsKey(accountId)) {
            return true;
        } else {
            throw new AccountNotExistsException(accountId);
        }
    }
}
