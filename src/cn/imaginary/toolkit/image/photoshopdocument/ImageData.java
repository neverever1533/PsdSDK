package cn.imaginary.toolkit.image.photoshopdocument;

import cn.imaginary.toolkit.image.rle.PackBitsUtils;
// import java.awt.image.BufferedImage;
// import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

// import javax.imageio.ImageIO;

public class ImageData {

    public static String[] arr_Compression_Method = { "Raw", "RLE", "ZIP", "ZIP_Prediction" };

    private int compression;
    private int Raw_Data = 0;
    private int RLE_Compressed = 1;
    private int ZIP_Without_Prediction = 2;
    private int ZIP_With_Prediction = 3;

    private long length_;
    private long length_Data;
    private byte[] arr_ImageData;

    public ImageData() {}

    public int getCompression() {
        return compression;
    }

    public String getCompressionMethodName() {
        return arr_Compression_Method[compression];
    }

    public long getLength() {
        return length_;
    }

    public byte[] getImageData() {
        return arr_ImageData;
    }

    private void readCompression(RandomAccessFile rafile) throws IOException {
        //5.1 Compression Method:2
        // Compression method:
        // 0 = Raw image data
        // 1 = RLE compressed the image data starts with the byte counts for all the scan lines (rows * channels), with each count stored as a two-byte value. The RLE compressed data follows, with each scan line compressed separately. The RLE compression is the same compression algorithm used by the Macintosh ROM routine PackBits , and the TIFF standard.
        // 2 = ZIP without prediction
        // 3 = ZIP with prediction.
        compression = rafile.readShort();
        length_ += 2;
    }

    private void readDataLength(RandomAccessFile rafile) throws IOException {
        length_Data = rafile.length() - rafile.getFilePointer();
    }

    public void read(RandomAccessFile rafile, FileHeader fheader) {
        try {
            long location = rafile.getFilePointer();
            readCompression(rafile);
            readDataLength(rafile);
            readData(rafile, fheader);
            length_ += length_Data;
            rafile.seek(location + getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readData(RandomAccessFile rafile, FileHeader fheader) throws IOException {
        //5.2 Image Data：?
        // The image data. Planar order = RRR GGG BBB, etc.
        switch (compression) {
            case 0:
                imageDataRaw(rafile, fheader);
                break;
            case 1:
                imageDataRLE(rafile, fheader);
                break;
            case 2:
                imageDataZip(rafile, fheader);
                break;
            case 3:
                imageDataZipPrediction(rafile, fheader);
                break;
            default:
                throw new IOException("The Compression Method of the Image Data is wrong.");
        }
        System.out.println("5.location_ImageData: " + rafile.getFilePointer());
    }

    private void imageDataRaw(RandomAccessFile rafile, FileHeader fheader) {
        //0 = Raw
    }

    private void imageDataRLE(RandomAccessFile rafile, FileHeader fheader) {
        //1 = RLE compressed
    }

    private void imageDataZip(RandomAccessFile rafile, FileHeader fheader) {
        //2 = ZIP without prediction
    }

    private void imageDataZipPrediction(RandomAccessFile rafile, FileHeader fheader) {
        //3 = ZIP with prediction.
    }

    public String toString() {
        String sbuilder = "Compression Method: " + compression + "/Image Data Length: " + length_Data;
        return sbuilder;
    }
}
