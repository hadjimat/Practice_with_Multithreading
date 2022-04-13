import exceptions.ReplenishNotPossibleException;
import exceptions.WithdrawNotPossibleException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Data
@AllArgsConstructor
@Log4j2
public class Account {


    private String accNumber;

    private volatile AtomicLong money;

    private volatile boolean blocked;

    public Account(@NonNull String accountID, long money) {
        this.accNumber = accountID;
        this.money = new AtomicLong(money);
    }


    public void replenish(long amount) throws ReplenishNotPossibleException {
        synchronized (this) {
            if (isLegalOperation(amount)) {
                money.addAndGet(amount);
                log.info("пополнение счета {} на сумму {}", accNumber, amount);
            } else {
                throw new ReplenishNotPossibleException(accNumber);
            }
        }
    }


    public void withdraw(long amount) throws WithdrawNotPossibleException {
        synchronized (this) {
            if (isLegalOperation(amount) && money.get() > amount) {
                money.accumulateAndGet(amount, (x, y) -> x - y);
                log.info("списание со счета {} на сумму {}", accNumber, amount);
            } else {
                throw new WithdrawNotPossibleException(accNumber);
            }
        }
    }

    private boolean isLegalOperation(long amount) {
        synchronized (this) {
            return !blocked && amount > 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        synchronized (this) {
            if (this == o) return true;
            if (!(o instanceof Account account)) return false;
            return this.getAccNumber().equals(account.getAccNumber());
        }
    }

    @Override
    public int hashCode() {
        synchronized (this) {
            return Objects.hash(this.getAccNumber());
        }
    }
}
