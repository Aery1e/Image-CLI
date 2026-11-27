package com.imagetool.image;

import com.imagetool.error.ErrorCode;
import com.imagetool.error.ImageToolException;

import java.io.File;
import java.util.Scanner;

public class PpmReader {
    public static Image read(String path) throws ImageToolException {
        try (Scanner sc = new Scanner(new File(path))) {
            if (!sc.next().equals("P3")) {
                throw new ImageToolException(ErrorCode.INPUT_FILE_MISSING, "Not a P3 PPM file");
            }
            int width = sc.nextInt();
            int height = sc.nextInt();
            sc.nextInt(); // maxval
            Image img = new Image(width, height);
            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {
                    img.setPixel(r, c, sc.nextInt(), sc.nextInt(), sc.nextInt());
                }
            }
            return img;
        } catch (ImageToolException e) {
            throw e;
        } catch (Exception e) {
            throw new ImageToolException(ErrorCode.INPUT_FILE_MISSING, "Cannot read: " + path);
        }
    }
}