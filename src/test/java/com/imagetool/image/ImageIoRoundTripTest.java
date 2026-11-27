package com.imagetool.image;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ImageIoRoundTripTest {
    @Test
    void pngRoundTripPreservesRgb(@TempDir Path tmp) throws Exception {
        Image img = new Image(2, 1);
        img.setPixel(0, 0, 10, 20, 30);
        img.setPixel(0, 1, 200, 150, 100);

        Path path = tmp.resolve("roundtrip.png");
        ImageIoWriter.write(img, path.toString(), "png");
        Image loaded = ImageIoReader.read(path.toString());

        assertArrayEquals(new int[]{10, 20, 30}, loaded.getPixel(0, 0));
        assertArrayEquals(new int[]{200, 150, 100}, loaded.getPixel(0, 1));
    }

    @Test
    void readerCompositesTransparentPngOverWhite(@TempDir Path tmp) throws Exception {
        BufferedImage source = new BufferedImage(2, 1, BufferedImage.TYPE_INT_ARGB);
        source.setRGB(0, 0, 0x00ff0000);
        source.setRGB(1, 0, 0x800000ff);
        Path path = tmp.resolve("transparent.png");
        ImageIO.write(source, "png", path.toFile());

        Image loaded = ImageIoReader.read(path.toString());

        assertArrayEquals(new int[]{255, 255, 255}, loaded.getPixel(0, 0));
        assertArrayEquals(new int[]{127, 127, 255}, loaded.getPixel(0, 1));
    }

    @Test
    void jpegRoundTripAllowsLossyColorChanges(@TempDir Path tmp) throws Exception {
        Image img = new Image(16, 16);
        for (int row = 0; row < img.getHeight(); row++) {
            for (int col = 0; col < img.getWidth(); col++) {
                img.setPixel(row, col, 170, 80, 40);
            }
        }
        Path path = tmp.resolve("roundtrip.jpg");

        ImageIoWriter.write(img, path.toString(), "jpeg");
        Image loaded = ImageIoReader.read(path.toString());

        assertEquals(16, loaded.getWidth());
        assertEquals(16, loaded.getHeight());
        int[] pixel = loaded.getPixel(8, 8);
        assertTrue(Math.abs(170 - pixel[0]) <= 10);
        assertTrue(Math.abs(80 - pixel[1]) <= 10);
        assertTrue(Math.abs(40 - pixel[2]) <= 10);
    }
}
