package cn.imaginary.toolkit.image.photoshopdocument;

import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer.ChannelImageData;
import cn.imaginary.toolkit.image.rle.PackBitsUtils;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ImageData {

    public static String[] arr_Name_Compression = { "Raw", "RLE", "ZIP", "ZIP_Prediction" };

    public int compression;

    public static int Raw = 0;
    public static int RLE = 1;
    public static int ZIP = 2;
    public static int ZIP_Prediction = 3;

    private long length_;
    private long length_Data;

    private byte[] arr_Data;

    private byte[][][] arrays_Data_Image;

    public ImageData() {}

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

    public String getCompressionMethodName() {
        return arr_Name_Compression[compression];
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

    public byte[][][] getImageData() {
        return arrays_Data_Image;
    }

    public void setImageData(byte[][][] arrays) {
        arrays_Data_Image = arrays;
    }

    public void read(RandomAccessFile rafile, FileHeader fheader) {
        try {
            setLength(rafile.length() - rafile.getFilePointer());

            //5.1 Compression Method:2
            // Compression method:
            // 0 = Raw image data
            // 1 = RLE compressed the image data starts with the byte counts for all the scan lines (rows * channels), with each count stored as a two-byte value. The RLE compressed data follows, with each scan line compressed separately. The RLE compression is the same compression algorithm used by the Macintosh ROM routine PackBits , and the TIFF standard.
            // 2 = ZIP without prediction
            // 3 = ZIP with prediction.
            setCompression(rafile.readShort());

            setDataLength(getLength() - 2);

            readData(rafile, fheader, getDataLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readData(RandomAccessFile rafile, FileHeader fheader, long length) throws IOException {
        if (length > 0) {
            byte[] arr = new byte[(int) length];
            rafile.read(arr);
            setData(arr);
            readDataArray(arr, fheader);
        }
    }

    private void readDataArray(byte[] array, FileHeader fheader) throws IOException {
        try {
            DataInputStream dinstream = new DataInputStream(new ByteArrayInputStream(array));
            readImageData(dinstream, fheader);
            dinstream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readImageData(DataInputStream dinstream, FileHeader fheader) throws IOException {
        int channels = fheader.getChannels();
        int width = fheader.getWidth();
        int height = fheader.getHeight();
        //5.2 Image Data：?
        // The image data. Planar order = RRR GGG BBB, etc.
        int compression = getCompression();
        switch (compression) {
            case 0:
                readImageDataRaw(dinstream, channels, width, height);
                break;
            case 1:
                readImageDataRLE(dinstream, fheader, channels, width, height);
                break;
            case 2:
                readImageDataZip(dinstream, fheader, channels, width, height);
                break;
            case 3:
                readImageDataZipPrediction(dinstream, fheader, channels, width, height);
                break;
            default:
                throw new IOException("The Compression Method of the Image Data is wrong.");
        }
    }

    private void readImageDataRaw(DataInputStream dinstream, int channels, int width, int height) throws IOException {
        byte[] arr;
        byte[][][] arrays_data_image = new byte[channels][height][width];
        for (int i = 0; i < channels; i++) {
            for (int j = 0; j < height; j++) {
                arr = new byte[width];
                dinstream.read(arr);
                arrays_data_image[i][j] = arr;
            }
        }
        setImageData(arrays_data_image);
    }

    private void readImageDataRLE(DataInputStream dinstream, FileHeader fheader, int channels, int width, int height)
        throws IOException {
        int[] arr_ScanLine = new int[height * channels];
        int count;
        for (int i = 0; i < arr_ScanLine.length; i++) {
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
        byte[][][] arrays_data_image = new byte[channels][height][width];
        for (int i = 0; i < channels; i++) {
            for (int j = 0; j < height; j++) {
                index = height * i + j;
                count = arr_ScanLine[index];
                arr = new byte[count];
                dinstream.read(arr);
                arr = pbutils.getDataDecompressed(arr);
                arrays_data_image[i][j] = arr;
            }
        }
        setImageData(arrays_data_image);
    }

    private void readImageDataZip(DataInputStream rafile, FileHeader fheader, int channels, int width, int height) {
        //2 = ZIP without prediction
    }

    private void readImageDataZipPrediction(
        DataInputStream rafile,
        FileHeader fheader,
        int channels,
        int width,
        int height
    ) {
        //3 = ZIP with prediction.
    }

    public String toString() {
        String sbuilder = "Compression Method: " + compression + "/Image Data Length: " + length_Data;
        return sbuilder;
    }
}
