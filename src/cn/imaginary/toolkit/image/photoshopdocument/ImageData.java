package cn.imaginary.toolkit.image.photoshopdocument;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ImageData {

    public ImageData() {}

    private int compressionMethod;

    public String[] arr_CompressionMethod = { "Raw", "RLE", "ZIP", "ZIP_Prediction" };

    private int length_ImageData;

    private byte[] arr_ImageData;

    public int getCompressionMethod() {
        return compressionMethod;
    }

    public String getCompressionMethodName() {
        return arr_CompressionMethod[compressionMethod];
    }

    public int getLength() {
        return length_ImageData;
    }

    public void read(RandomAccessFile rafile, FileHeader fheader) {
        if (null == fheader) {
            return;
        }
        //length_ImageData = (int)(rafile.length() - rafile.getFilePointer());
        try {
            //5.1 Compression Method2
            // Compression method:
            // 0 = Raw image data
            // 1 = RLE compressed the image data starts with the byte counts for all the scan lines (rows * channels), with each count stored as a two-byte value.
            // The RLE compressed data follows, with each scan line compressed separately. The RLE compression is the same compression algorithm used by the Macintosh ROM routine PackBits , and the TIFF standard.
            // 2 = ZIP without prediction
            // 3 = ZIP with prediction.
            compressionMethod = rafile.readShort();
            System.out.println("Compression Method: " + compressionMethod);

            //5.2 Image Data：?
            // The image data. Planar order = RRR GGG BBB, etc.
            int pixels = fheader.getHeight() * fheader.getWidth();
            switch (compressionMethod) {
                case 0:
                    imageDataRaw(rafile, fheader, pixels);
                    break;
                case 1:
                    imageDataRLE(rafile, fheader, pixels);
                    break;
                case 2:
                    imageDataZip(rafile, fheader, pixels);
                    break;
                case 3:
                    imageDataZipPrediction(rafile, fheader, pixels);
                    break;
                default:
                    throw new IOException("Unknown Compression Method");
            }

            System.out.println("5.location_ImageData: " + rafile.getFilePointer());
        } catch (IOException e) {}
    }

    private void imageDataRaw(RandomAccessFile rafile, FileHeader fheader, int pixels) {
        //0 = Raw image data
    }

    private void imageDataRLE(RandomAccessFile rafile, FileHeader fheader, int pixels) {
        //1 = RLE compressed the image data
    }

    private void imageDataZip(RandomAccessFile rafile, FileHeader fheader, int pixels) {
        //2 = ZIP without prediction
    }

    private void imageDataZipPrediction(RandomAccessFile rafile, FileHeader fheader, int pixels) {
        //3 = ZIP with prediction.
    }
}
