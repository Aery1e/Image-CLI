package com.imagetool.ops;

import com.imagetool.error.ErrorCode;
import com.imagetool.error.ImageToolException;
import com.imagetool.image.Image;

public class CopyOperation {
    public static int[][] copy(Image img, String spec) throws ImageToolException {
        String[] parts = spec.split(",");
        if (parts.length != 4) {
            throw new ImageToolException(ErrorCode.C_ARGUMENT_INVALID, "-c needs 4 values");
        }
        int row, col, w, h;
        try {
            row = Integer.parseInt(parts[0].trim());
            col = Integer.parseInt(parts[1].trim());
            w = Integer.parseInt(parts[2].trim());
            h = Integer.parseInt(parts[3].trim());
        } catch (NumberFormatException e) {
            throw new ImageToolException(ErrorCode.C_ARGUMENT_INVALID, "-c values must be integers");
        }
        if (row < 0 || col < 0 || w <= 0 || h <= 0) {
            throw new ImageToolException(ErrorCode.C_ARGUMENT_INVALID, "-c values out of range");
        }
        int maxH = Math.min(h, img.getHeight() - row);
        int maxW = Math.min(w, img.getWidth() - col);
        if (maxH <= 0 || maxW <= 0) {
            throw new ImageToolException(ErrorCode.C_ARGUMENT_INVALID, "-c region out of bounds");
        }
        int[][] buf = new int[maxH][maxW * 3];
        for (int r = 0; r < maxH; r++)
            for (int c = 0; c < maxW * 3; c++)
                buf[r][c] = img.getPixels()[row + r][col * 3 + c];
        return buf;
    }
}