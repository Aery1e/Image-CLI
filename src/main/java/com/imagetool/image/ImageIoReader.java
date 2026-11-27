package com.imagetool.image;

import com.imagetool.error.ErrorCode;
import com.imagetool.error.ImageToolException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageIoReader {
    public static Image read(String path) throws ImageToolException {
        BufferedImage source;
        try {
            source = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new ImageToolException(ErrorCode.INPUT_FILE_MISSING, "Cannot read: " + path);
        }
        if (source == null) {
            throw new ImageToolException(ErrorCode.INPUT_FILE_MISSING, "Unsupported or invalid image: " + path);
        }

        Image img = new Image(source.getWidth(), source.getHeight());
        for (int row = 0; row < source.getHeight(); row++) {
            for (int col = 0; col < source.getWidth(); col++) {
                int argb = source.getRGB(col, row);
                int alpha = argb >>> 24;
                int red = compositeOnWhite((argb >>> 16) & 0xff, alpha);
                int green = compositeOnWhite((argb >>> 8) & 0xff, alpha);
                int blue = compositeOnWhite(argb & 0xff, alpha);
                img.setPixel(row, col, red, green, blue);
            }
        }
        return img;
    }

    private static int compositeOnWhite(int color, int alpha) {
        return (color * alpha + 255 * (255 - alpha) + 127) / 255;
    }
}
