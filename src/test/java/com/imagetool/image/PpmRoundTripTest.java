package com.imagetool.image;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PpmRoundTripTest {
    @Test
    void roundTrip(@TempDir Path tmp) throws Exception {
        Image img = new Image(3, 2);
        img.setPixel(0, 0, 10, 20, 30);
        img.setPixel(0, 1, 40, 50, 60);
        img.setPixel(0, 2, 70, 80, 90);
        img.setPixel(1, 0, 1, 2, 3);
        img.setPixel(1, 1, 4, 5, 6);
        img.setPixel(1, 2, 7, 8, 9);

        Path p = tmp.resolve("t.ppm");
        PpmWriter.write(img, p.toString());
        Image loaded = PpmReader.read(p.toString());

        assertEquals(3, loaded.getWidth());
        assertEquals(2, loaded.getHeight());
        assertArrayEquals(new int[]{10, 20, 30}, loaded.getPixel(0, 0));
        assertArrayEquals(new int[]{7, 8, 9}, loaded.getPixel(1, 2));
    }
}