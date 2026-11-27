package com.imagetool.ops;

import com.imagetool.error.ErrorCode;
import com.imagetool.error.ImageToolException;
import com.imagetool.image.Image;

public class PasteOperation {
    public static void paste(Image img, String spec, int[][] buf) throws ImageToolException {
        String[] parts = spec.split(",");
        if (parts.length != 2) {
            throw new ImageToolException(ErrorCode.P_ARGUMENT_INVALID, "-p needs 2 values");
        }
        int row, col;
        try {
            row = Integer.parseInt(parts[0].trim());
            col = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new ImageToolException(ErrorCode.P_ARGUMENT_INVALID, "-p values must be integers");
        }
        if (row < 0 || col < 0) {
            throw new ImageToolException(ErrorCode.P_ARGUMENT_INVALID, "-p values out of range");
        }
        int maxH = Math.min(buf.length, img.getHeight() - row);
        int maxW = Math.min(buf[0].length / 3, img.getWidth() - col);
        for (int r = 0; r < maxH; r++)
            for (int c = 0; c < maxW * 3; c++)
                img.getPixels()[row + r][col * 3 + c] = buf[r][c];
    }
}