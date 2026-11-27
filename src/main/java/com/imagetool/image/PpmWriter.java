package com.imagetool.image;

import com.imagetool.error.ErrorCode;
import com.imagetool.error.ImageToolException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PpmWriter {
    public static void write(Image img, String path) throws ImageToolException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write("P3\n");
            bw.write(img.getWidth() + " " + img.getHeight() + "\n");
            bw.write("255\n");
            int[][] px = img.getPixels();
            for (int r = 0; r < img.getHeight(); r++) {
                for (int c = 0; c < img.getWidth() * 3; c++) {
                    bw.write(px[r][c] + " ");
                }
                bw.newLine();
            }
        } catch (IOException e) {
            throw new ImageToolException(ErrorCode.OUTPUT_FILE_UNWRITABLE, "Cannot write: " + path);
        }
    }
}