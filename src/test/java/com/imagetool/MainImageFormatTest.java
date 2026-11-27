package com.imagetool;

import com.imagetool.image.Image;
import com.imagetool.image.ImageIoReader;
import com.imagetool.image.PpmWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MainImageFormatTest {
    @Test
    void convertsPpmToPngAndPngToJpeg(@TempDir Path tmp) throws Exception {
        Image source = new Image(16, 16);
        for (int row = 0; row < source.getHeight(); row++) {
            for (int col = 0; col < source.getWidth(); col++) {
                source.setPixel(row, col, 170, 80, 40);
            }
        }
        Path ppm = tmp.resolve("input.ppm");
        Path png = tmp.resolve("output.png");
        Path jpeg = tmp.resolve("output.jpeg");
        PpmWriter.write(source, ppm.toString());

        Main.run(new String[]{"-i", ppm.toString(), "-o", png.toString()});
        Main.run(new String[]{"-i", png.toString(), "-o", jpeg.toString()});

        Image pngResult = ImageIoReader.read(png.toString());
        Image jpegResult = ImageIoReader.read(jpeg.toString());
        assertArrayEquals(new int[]{170, 80, 40}, pngResult.getPixel(0, 0));
        assertEquals(16, jpegResult.getWidth());
        assertEquals(16, jpegResult.getHeight());
    }
}
