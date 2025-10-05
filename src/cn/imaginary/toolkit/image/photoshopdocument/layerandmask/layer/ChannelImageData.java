package cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer;

import cn.imaginary.toolkit.image.photoshopdocument.FileHeader;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.LayerRecords;
import cn.imaginary.toolkit.image.rle.PackBitsUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ChannelImageData {

    public static int Raw = 0;
    public static int RLE = 1;
    public static int ZIP = 2;
    public static int ZIP_Prediction = 3;

    private int compression;

    private long length_;
    private long length_Data;

    private byte[] arr_Data;

    private byte[][] arrs_Data_Image;

    public ChannelImageData() {
    }

    public long getLength() {
        return length_;
    }

    public void setLength(long length) {
        length_ = length;
    }

    public long getDataLength() {
        return length_Data;
    }

    public void setDataLength(long length) {
        length_Data = length;
    }

    public byte[] getData() {
        return arr_Data;
    }

    public void setData(byte[] array) {
        arr_Data = array;
    }

    public byte[][] getImageData() {
        return arrs_Data_Image;
    }

    public void setImageData(byte[][] arrays) {
        arrs_Data_Image = arrays;
    }

    public int getCompression() {
        return compression;
    }

    public void setCompression(int compression) throws IOException {
        switch (compression) {
            case 0:
                this.compression = Raw;
                break;
            case 1:
                this.compression = RLE;
                break;
            case 2:
                this.compression = ZIP;
                break;
            case 3:
                this.compression = ZIP_Prediction;
                break;
            default:
                throw new IOException("The Compression Method of the Image Data is wrong.");
        }
    }

    public void read(DataInputStream dinstream, long length, FileHeader fheader, int width, int height) {
        try {
            readDataLength(length);
            readData(dinstream, fheader, width, height, getDataLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readDataLength(long length) {
        setDataLength(length);
        setLength(length);
    }

    private void readData(DataInputStream dinstream, FileHeader fheader, int width, int height, long length)
            throws IOException {
        if (length > 0) {
            byte[] arr = new byte[(int) length];
            dinstream.read(arr);
            setData(arr);
            readDataArray(arr, fheader, width, height);
        }
    }

    private void readDataArray(byte[] array, FileHeader fheader, int width, int height) {
        try {
            DataInputStream dinstream = new DataInputStream(new ByteArrayInputStream(array));

            // Compression. 0 = Raw Data, 1 = RLE compressed, 2 = ZIP without prediction, 3 = ZIP with prediction.
            setCompression(dinstream.readShort());

            readImageData(dinstream, fheader, width, height);

            dinstream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readImageData(DataInputStream dinstream, FileHeader fheader, int width, int height)
            throws IOException {
        // Image data.:?
        // If the compression code is 0, the image data is just the raw image data, whose size is calculated as (LayerBottom-LayerTop)* (LayerRight-LayerLeft) (from the first field in See Layer records).
        // If the compression code is 1, the image data starts with the byte counts for all the scan lines in the channel (LayerBottom-LayerTop) , with each count stored as a two-byte value.(**PSB** each count stored as a four-byte value.) The RLE compressed data follows, with each scan line compressed separately. The RLE compression is the same compression algorithm used by the Macintosh ROM routine PackBits, and the TIFF standard.
        // If the layer's size, and therefore the data, is odd, a pad byte will be inserted at the end of the row.
        // If the layer is an adjustment layer, the channel data is undefined (probably all white.)
        int compression = getCompression();
        switch (compression) {
            case 0:
                readImageDataRaw(dinstream, width, height);
                break;
            case 1:
                readImageDataRLE(dinstream, fheader, width, height);
                break;
            case 2:
                readImageDataZip(dinstream, fheader, width, height);
                break;
            case 3:
                readImageDataZipPrediction(dinstream, fheader, width, height);
                break;
            default:
                throw new IOException("The Compression Method of the Channel Image Data is wrong.");
        }
    }

    public void readImageDataRaw(DataInputStream dinstream, int width, int height)
            throws IOException {
        byte[] arr;
        byte[][] arrays_data_image = new byte[height][width];
        for (int i = 0; i < height; i++) {
            arr = new byte[width];
            dinstream.read(arr);
            arrays_data_image[i] = arr;
        }
        setImageData(arrays_data_image);
    }

    public void readImageDataRLE(DataInputStream dinstream, FileHeader fheader, int width, int height)
            throws IOException {
        int[] arr_ScanLine = new int[height];
        int count;
        for (int i = 0; i < height; i++) {
            if (!fheader.isFilePsb()) {
                count = dinstream.readShort();
            } else {
                count = dinstream.readInt();
            }
            arr_ScanLine[i] = count;
        }

        int index;
        byte[] arr;
        PackBitsUtils pbutils = new PackBitsUtils();
        byte[][] arrays_data_image = new byte[height][width];
        for (int i = 0; i < height; i++) {
            count = arr_ScanLine[i];
            arr = new byte[count];
            dinstream.read(arr);
            arr = pbutils.getDataDecompressed(arr);
            arrays_data_image[i] = arr;
        }
        setImageData(arrays_data_image);
    }

    public void readImageDataZip(DataInputStream dinstream, FileHeader fheader, int width, int height)
            throws IOException {
    }

    public void readImageDataZipPrediction(DataInputStream dinstream, FileHeader fheader, int width, int height)
            throws IOException {
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Channel Image Data Length: " + getDataLength());
        sbuilder.append("/image data compression: " + getCompression());
        return sbuilder.toString();
    }
}
