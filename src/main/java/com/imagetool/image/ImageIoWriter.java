package com.imagetool.image;

import com.imagetool.error.ErrorCode;
import com.imagetool.error.ImageToolException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageIoWriter {
    public static void write(Image img, String path, String formatName) throws ImageToolException {
        BufferedImage output = new BufferedImage(
                img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        int[][] pixels = img.getPixels();
        for (int row = 0; row < img.getHeight(); row++) {
            for (int col = 0; col < img.getWidth(); col++) {
                int offset = col * 3;
                int pixel = (pixels[row][offset] << 16)
                        | (pixels[row][offset + 1] << 8)
                        | pixels[row][offset + 2];
                output.setRGB(col, row, pixel);
            }
        }

        try {
            if (!ImageIO.write(output, formatName, new File(path))) {
                throw new ImageToolException(
                        ErrorCode.OUTPUT_FILE_UNWRITABLE, "Unsupported image format: " + formatName);
            }
        } catch (IOException e) {
            throw new ImageToolException(ErrorCode.OUTPUT_FILE_UNWRITABLE, "Cannot write: " + path);
        }
    }
}
