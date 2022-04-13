import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.concurrent.ForkJoinPool;

public class Main {

    private static final String url = "https://lenta.ru";
    private static String fileName;
    private static final String DST_FOLDER = "src/main/resources/sitemap/";
    private static final String FILE_EXTENSION = "txt";
    private static final int numberOfCores = Runtime.getRuntime().availableProcessors();
    private static long startOfTime;

    public static void main(String[] args) {
        try {
            startOfTime = System.currentTimeMillis();

            fileName = new URL(url).getHost().replace(".", "_");

            System.out.println("Start: " + LocalTime.now());

            LinkExecutor linkExecutor = new LinkExecutor(url);
            String siteMap = new ForkJoinPool(numberOfCores).invoke(linkExecutor);

            System.out.println("Finish: " + LocalTime.now());
            writeToFile(siteMap);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.err.println("Превышено допустимое количество, введите другое значение!");
        }
    }

    private static void writeToFile(String siteMap) {
        if (!Files.exists(Paths.get(DST_FOLDER))) {
            new File(DST_FOLDER);
        }
        String filePath = DST_FOLDER.concat(fileName).concat(".").concat(FILE_EXTENSION);
        File file = new File(filePath);
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write(siteMap);
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Файл записан: " + file.getAbsolutePath());
        long periodOfTimeInSec = (System.currentTimeMillis() - startOfTime) / 1000;
        System.out.println("Карта сайта " + url + " сформирована за " + periodOfTimeInSec + " секунд");
    }
}
