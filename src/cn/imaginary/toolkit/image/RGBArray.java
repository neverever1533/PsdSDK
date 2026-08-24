package cn.imaginary.toolkit.image;

import java.awt.image.BufferedImage;

public class RGBArray {
    public RGBArray() {
    }

    public int[][] getArrays(BufferedImage root) {
        if (null != root) {
            int w = root.getWidth();
            int h = root.getHeight();
            int[][] array = new int[w - 1][h - 1];
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    array[x][y] = root.getRGB(x, y);
                }
            }
            return array;
        }
        return null;
    }

    public BufferedImage getImage(int[][] array) {
        if (null != array) {
            int width = array.length;
            int height = array[width - 1].length;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, array[x][y]);
                }
            }
            return image;
        }
        return null;
    }

    public BufferedImage getImage(byte[][][] arrays, int width, int height) {
        if (null != arrays) {
            if (width <= 0 || height <= 0) {
                return null;
            }
            int channels = arrays.length;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            int rgb;
            int a = 0xff;
            int r;
            int b;
            int g;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (channels > 3) {
                        a = arrays[3][y][x] & 0xff;
                    }
                    r = arrays[0][y][x] & 0xff;
                    g = arrays[1][y][x] & 0xff;
                    b = arrays[2][y][x] & 0xff;
                    rgb = (a << 24) | (r << 16) | (g << 8) | b;
                    image.setRGB(x, y, rgb);
                }
            }
            return image;
        }
        return null;
    }
}
