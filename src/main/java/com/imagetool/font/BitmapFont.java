package com.imagetool.font;

import com.imagetool.error.ErrorCode;
import com.imagetool.error.ImageToolException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class BitmapFont {
    private final int height;
    private final List<boolean[][]> letters; // A-Z, index 26 = space

    private BitmapFont(int height, List<boolean[][]> letters) {
        this.height = height;
        this.letters = letters;
    }

    public int getHeight() { return height; }

    public boolean[][] getLetter(char ch) {
        if (ch >= 'a' && ch <= 'z') ch = Character.toUpperCase(ch);
        if (ch >= 'A' && ch <= 'Z') return letters.get(ch - 'A');
        return letters.get(26);
    }

    public static BitmapFont load(String path) throws ImageToolException {
        List<String> lines;
        try {
            lines = Files.readAllLines(new File(path).toPath());
        } catch (IOException e) {
            throw new ImageToolException(ErrorCode.R_ARGUMENT_INVALID, "Cannot read font: " + path);
        }
        while (!lines.isEmpty() && lines.get(lines.size() - 1).trim().isEmpty()) {
            lines.remove(lines.size() - 1);
        }
        if (lines.isEmpty()) {
            throw new ImageToolException(ErrorCode.R_ARGUMENT_INVALID, "Empty font file");
        }
        int h = lines.size();
        int w = 0;
        for (String l : lines) w = Math.max(w, l.length());
        char[][] grid = new char[h][w];
        for (int r = 0; r < h; r++) {
            String l = lines.get(r);
            for (int c = 0; c < w; c++) {
                grid[r][c] = c < l.length() ? l.charAt(c) : ' ';
            }
        }

        List<int[]> spans = new ArrayList<>();
        int c = 0;
        while (c < w) {
            while (c < w && spaceCol(grid, c)) c++;
            if (c >= w) break;
            int start = c;
            while (c < w && !spaceCol(grid, c)) c++;
            spans.add(new int[]{start, c});
        }
        if (spans.size() < 26) {
            throw new ImageToolException(ErrorCode.R_ARGUMENT_INVALID,
                    "Font must contain 26 letters, found " + spans.size());
        }

        List<boolean[][]> result = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            int[] s = spans.get(i);
            int lw = s[1] - s[0];
            boolean[][] letter = new boolean[h][lw];
            for (int r = 0; r < h; r++)
                for (int cc = 0; cc < lw; cc++)
                    letter[r][cc] = grid[r][s[0] + cc] == '*';
            result.add(letter);
        }
        result.add(new boolean[h][5]); // space
        return new BitmapFont(h, result);
    }

    private static boolean spaceCol(char[][] g, int col) {
        for (char[] row : g) if (row[col] == '*') return false;
        return true;
    }
}