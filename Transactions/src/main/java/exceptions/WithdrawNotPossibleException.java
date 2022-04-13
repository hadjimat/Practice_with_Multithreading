package exceptions;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;


@AllArgsConstructor
@Log4j2
public class WithdrawNotPossibleException extends Exception {

    private String accountId;

    @Override
    public void printStackTrace() {
        log.error("Списание по счету{} невозможно.", accountId);
    }

    @Override
    public String getMessage() {
        return "Списание по счету №" + accountId + " невозможно.";
    }
}
