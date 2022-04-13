import java.io.File;

public class Main {

    private static final int newWidth = 300;
    private static int threatCount = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        String srcFolder = "C:\\Users\\user\\Desktop\\src";
        String dstFolder = "C:\\Users\\user\\Desktop\\dst";

        File srcDir = new File(srcFolder);

        long start = System.currentTimeMillis();

        File[] files = srcDir.listFiles();

        assert files != null;
        int pieces = 1;
        int srcPos = 0;
        int residual;
        if (files.length > threatCount) {
            pieces = files.length / threatCount;
        } else {
            threatCount = files.length;
        }
        residual = files.length % threatCount;

        for (int i = 0; i < threatCount; i++) {
            File[] files1;

            if (residual != 0) {
                files1 = new File[pieces + 1];
                System.arraycopy(files, srcPos, files1, 0, pieces + 1);
                srcPos = srcPos + pieces + 1;
                residual--;
            } else {
                files1 = new File[pieces];
                System.arraycopy(files, srcPos, files1, 0, pieces);
                srcPos = srcPos + pieces;
            }
            ImageResizer resizer = new ImageResizer(files1, newWidth, dstFolder, start);
            new Thread(resizer).start();
        }
    }
}
