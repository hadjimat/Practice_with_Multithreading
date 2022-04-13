import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {


        HashMap<String, Account> accounts = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            long accMoney = ThreadLocalRandom.current().nextLong(40000, 400000);
            accounts.put("" + i, new Account("" + i, accMoney));
        }
        Bank bank = new Bank(accounts);

        Random random = new Random();

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            threads.add(new Thread(() -> {
                try {
                    bank.transfer(String.valueOf(random.nextInt(4)),
                            String.valueOf(random.nextInt(4)), random.nextInt(70000));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }

        threads.forEach(Thread::start);
    }
}
