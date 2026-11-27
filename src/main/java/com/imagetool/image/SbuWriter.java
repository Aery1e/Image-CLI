package com.imagetool.image;

import com.imagetool.error.ErrorCode;
import com.imagetool.error.ImageToolException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SbuWriter {
    public static void write(Image img, String path) throws ImageToolException {
        int width = img.getWidth();
        int height = img.getHeight();
        int[][] px = img.getPixels();

        Map<Long, Integer> index = new LinkedHashMap<>();
        List<int[]> table = new ArrayList<>();
        int[] flat = new int[width * height];

        int n = 0;
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int rr = px[r][c * 3], gg = px[r][c * 3 + 1], bb = px[r][c * 3 + 2];
                long key = ((long) rr << 16) | ((long) gg << 8) | bb;
                Integer idx = index.get(key);
                if (idx == null) {
                    idx = table.size();
                    index.put(key, idx);
                    table.add(new int[]{rr, gg, bb});
                }
                flat[n++] = idx;
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write("SBU\n");
            bw.write(width + " " + height + "\n");
            bw.write(table.size() + "\n");
            for (int[] rgb : table) {
                bw.write(rgb[0] + " " + rgb[1] + " " + rgb[2] + " ");
            }
            bw.newLine();
            int i = 0;
            while (i < flat.length) {
                int j = i + 1;
                while (j < flat.length && flat[j] == flat[i]) j++;
                int run = j - i;
                if (run == 1) bw.write(flat[i] + " ");
                else bw.write("*" + run + " " + flat[i] + " ");
                i = j;
            }
            bw.newLine();
        } catch (IOException e) {
            throw new ImageToolException(ErrorCode.OUTPUT_FILE_UNWRITABLE, "Cannot write: " + path);
        }
    }
}