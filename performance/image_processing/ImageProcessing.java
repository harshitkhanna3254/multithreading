package performance.image_processing;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;;

public class ImageProcessing {

    // public static final String SOURCE_IMAGE_1 =
    // "multithreading/performance/flowers.jpg";
    // public static final String DESTINATION_IMAGE_1 =
    // "multithreading/performance/out/flowers.jpg";

    public static final String SOURCE_IMAGE_1 = "multithreading/performance/many-flowers.jpg";
    public static final String DESTINATION_IMAGE_1 = "multithreading/performance/out/many-flowers.jpg";

    public static void main(String[] args) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_IMAGE_1));
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        long startTime = System.currentTimeMillis();
        // recolorSingleThreaded(originalImage, resultImage);
        int numberOfThreads = 2;
        recolorMultithreaded(originalImage, resultImage, numberOfThreads);
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        File outputFile = new File(DESTINATION_IMAGE_1);
        ImageIO.write(resultImage, "jpg", outputFile);

        System.out.println(String.valueOf(duration));
    }

    public static void recolorMultithreaded(BufferedImage originalImage, BufferedImage resultImage,
            int numberOfThreads) {
        List<Thread> threads = new ArrayList<>();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight() / numberOfThreads;

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadMultiplier = i;

            Thread thread = new Thread(() -> {
                int xOrigin = 0;
                int yOrigin = height * threadMultiplier;

                recolorImage(originalImage, resultImage, xOrigin, yOrigin, width, height);
            });

            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }
    }

    public static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage resultImage) {
        recolorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
    }

    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner,
            int topCorner, int width, int height) {
        for (int i = leftCorner; i < leftCorner + width && i < originalImage.getWidth(); i++) {
            for (int j = topCorner; j < topCorner + height && j < originalImage.getHeight(); j++) {
                recolorPixel(originalImage, resultImage, i, j);
            }
        }
    }

    public static void recolorPixel(BufferedImage originalimage, BufferedImage resultImage, int x, int y) {
        int rgbOfPixel = originalimage.getRGB(x, y);

        int red = getRed(rgbOfPixel);
        int green = getGreen(rgbOfPixel);
        int blue = getBlue(rgbOfPixel);

        if (isShadeOfGrey(red, green, blue)) {
            red = Math.min(red + 10, 255);
            green = Math.max(0, green - 80);
            blue = Math.max(0, blue - 20);
        }

        int newRGB = createRGBFromColors(red, green, blue);

        setRGB(resultImage, x, y, newRGB);
    }

    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    public static boolean isShadeOfGrey(int red, int green, int blue) {
        int tolerance = 30; // This can be adjusted according to how strict or lenient you want to be

        boolean closeRedGreen = Math.abs(red - green) <= tolerance;
        boolean closeRedBlue = Math.abs(red - blue) <= tolerance;
        boolean closeGreenBlue = Math.abs(green - blue) <= tolerance;

        // If all pairs are within tolerance, it's a shade of grey
        return closeRedGreen && closeRedBlue && closeGreenBlue;
    }

    public static int createRGBFromColors(int red, int green, int blue) {
        int rgb = 0;

        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;

        rgb |= 0xFF000000;

        return rgb;
    }

    public static int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }

    public static int getGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }

    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }
}
