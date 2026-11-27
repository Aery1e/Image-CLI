package com.imagetool.ops;

import com.imagetool.image.Image;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CopyPasteTest {
    @Test
    void copyThenPaste() throws Exception {
        Image img = new Image(4, 4);
        for (int r = 0; r < 4; r++)
            for (int c = 0; c < 4; c++)
                img.setPixel(r, c, r * 10, c * 10, 0);

        int[][] buf = CopyOperation.copy(img, "0,0,2,2");
        PasteOperation.paste(img, "2,2", buf);

        assertArrayEquals(new int[]{0, 0, 0}, img.getPixel(2, 2));
        assertArrayEquals(new int[]{0, 10, 0}, img.getPixel(2, 3));
        assertArrayEquals(new int[]{10, 0, 0}, img.getPixel(3, 2));
    }
}