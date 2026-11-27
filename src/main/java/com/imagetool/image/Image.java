package com.imagetool.image;

public class Image {
    private final int width;
    private final int height;
    private final int[][] pixels; // [height][width*3]

    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[height][width * 3];
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int[][] getPixels() { return pixels; }

    public int[] getPixel(int row, int col) {
        return new int[] {
            pixels[row][col * 3],
            pixels[row][col * 3 + 1],
            pixels[row][col * 3 + 2]
        };
    }

    public void setPixel(int row, int col, int r, int g, int b) {
        pixels[row][col * 3] = r;
        pixels[row][col * 3 + 1] = g;
        pixels[row][col * 3 + 2] = b;
    }
}