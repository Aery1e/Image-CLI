package com.imagetool.ops;

import com.imagetool.error.ErrorCode;
import com.imagetool.error.ImageToolException;
import com.imagetool.font.BitmapFont;
import com.imagetool.image.Image;

public class TextRenderOperation {
    public static void render(Image img, String spec) throws ImageToolException {
        // Split on commas, ignoring commas inside quotes
        String[] parts = spec.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        if (parts.length != 5) {
            throw new ImageToolException(ErrorCode.R_ARGUMENT_INVALID, "-r needs 5 values");
        }
        String message = unquote(parts[0]);
        String fontPath = unquote(parts[1]);
        int size, row, col;
        try {
            size = Integer.parseInt(parts[2].trim());
            row = Integer.parseInt(parts[3].trim());
            col = Integer.parseInt(parts[4].trim());
        } catch (NumberFormatException e) {
            throw new ImageToolException(ErrorCode.R_ARGUMENT_INVALID, "-r numeric values invalid");
        }
        if (size <= 0 || row < 0 || col < 0) {
            throw new ImageToolException(ErrorCode.R_ARGUMENT_INVALID, "-r values out of range");
        }

        BitmapFont font = BitmapFont.load(fontPath);
        int cursor = col;
        for (char ch : message.toCharArray()) {
            boolean[][] letter = font.getLetter(ch);
            int lh = letter.length;
            int lw = letter[0].length;
            for (int r = 0; r < lh * size; r++) {
                for (int c = 0; c < lw * size; c++) {
                    if (!letter[r / size][c / size]) continue;
                    int tr = row + r;
                    int tc = cursor + c;
                    if (tr < 0 || tr >= img.getHeight()) continue;
                    if (tc < 0 || tc >= img.getWidth()) continue;
                    img.setPixel(tr, tc, 255, 255, 255);
                }
            }
            cursor += lw * size;
        }
    }

    private static String unquote(String s) {
        s = s.trim();
        if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }
}