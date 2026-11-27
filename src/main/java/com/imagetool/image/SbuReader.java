package com.imagetool.image;

import com.imagetool.error.ErrorCode;
import com.imagetool.error.ImageToolException;

import java.io.File;
import java.util.Scanner;

public class SbuReader {
    public static Image read(String path) throws ImageToolException {
        try (Scanner sc = new Scanner(new File(path))) {
            if (!sc.next().equals("SBU")) {
                throw new ImageToolException(ErrorCode.INPUT_FILE_MISSING, "Not an SBU file");
            }
            int width = sc.nextInt();
            int height = sc.nextInt();
            int tableLen = sc.nextInt();
            int[][] table = new int[tableLen][3];
            for (int i = 0; i < tableLen; i++) {
                table[i][0] = sc.nextInt();
                table[i][1] = sc.nextInt();
                table[i][2] = sc.nextInt();
            }
            Image img = new Image(width, height);
            int total = width * height;
            int written = 0;
            while (written < total) {
                String tok = sc.next();
                int count = 1;
                if (tok.startsWith("*")) {
                    count = Integer.parseInt(tok.substring(1));
                    tok = sc.next();
                }
                int idx = Integer.parseInt(tok);
                int[] rgb = table[idx];
                for (int k = 0; k < count && written < total; k++, written++) {
                    img.setPixel(written / width, written % width, rgb[0], rgb[1], rgb[2]);
                }
            }
            return img;
        } catch (ImageToolException e) {
            throw e;
        } catch (Exception e) {
            throw new ImageToolException(ErrorCode.INPUT_FILE_MISSING, "Cannot read SBU: " + path);
        }
    }
}