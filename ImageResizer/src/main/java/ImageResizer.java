import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageResizer implements Runnable{

    private final File[] files;
    private final int newWidth;
    private final String dstFolder;
    private final long start;

    public ImageResizer(File[] files, int newWidth, String dstFolder, long start) {
        this.files = files;
        this.newWidth = newWidth;
        this.dstFolder = dstFolder;
        this.start = start;
    }

    @Override
    public void run() {

        try {
            assert files != null;
            for (File file : files) {
                BufferedImage image = ImageIO.read(file);
                if (image == null) {
                    continue;
                }

                int newHeight = (int) Math.round(
                        image.getHeight() / (image.getWidth() / (double) newWidth)
                );

                BufferedImage newImage = Scalr.resize(image, newWidth, newHeight, Scalr.OP_ANTIALIAS, Scalr.OP_BRIGHTER);

                String filename = file.getName();

                Pattern pattern = Pattern.compile("\\.(\\w+)\\z");
                Matcher matcher = pattern.matcher(filename);
                String format = matcher.group(1).toLowerCase();

                File newFile = new File(dstFolder + "/" + file.getName());
                ImageIO.write(newImage, format, newFile);

            }
            System.out.println("Duration: " + (System.currentTimeMillis() - start));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
