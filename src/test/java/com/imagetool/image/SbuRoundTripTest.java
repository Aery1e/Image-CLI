package com.imagetool.image;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SbuRoundTripTest {
    @Test
    void roundTrip(@TempDir Path tmp) throws Exception {
        Image img = new Image(4, 1);
        for (int c = 0; c < 4; c++) img.setPixel(0, c, 10, 20, 30);
        Path p = tmp.resolve("t.sbu");
        SbuWriter.write(img, p.toString());
        Image loaded = SbuReader.read(p.toString());
        assertEquals(4, loaded.getWidth());
        assertArrayEquals(new int[]{10, 20, 30}, loaded.getPixel(0, 0));
        assertArrayEquals(new int[]{10, 20, 30}, loaded.getPixel(0, 3));
    }
}