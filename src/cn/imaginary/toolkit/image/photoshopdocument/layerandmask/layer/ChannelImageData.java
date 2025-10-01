package cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer;

import cn.imaginary.toolkit.image.photoshopdocument.FileHeader;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.LayerRecords;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ChannelImageData {

    public static int Raw_Data = 0;
    public static int RLE_Compressed = 1;
    public static int ZIP = 2;
    public static int ZIP_With_Prediction = 3;
    private int compression;

    private long length_;
    private long length_Data;

    private byte[] arr_Data;

    public ChannelImageData() {
    }


    public long getLength() {
        return length_;
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

    public int getCompression() {
        return compression;
    }

    private void readCompression(RandomAccessFile rafile) throws IOException {
        // Compression. 0 = Raw Data, 1 = RLE compressed, 2 = ZIP without prediction, 3 = ZIP with prediction.
        compression = rafile.readShort();
    }

    private long readDataLength() {
        return getDataLength();
    }

    public void read(RandomAccessFile rafile, FileHeader fheader, LayerRecords lrecords) {
        try {
            long location = rafile.getFilePointer();

            long length = readDataLength();
            readData(rafile, fheader, lrecords, length);

            length_ += length_Data;
            //            System.out.println("Channel Image data space: " + (location + length_ - rafile.getFilePointer()));
            rafile.seek(location + getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readData(RandomAccessFile rafile, FileHeader fheader, LayerRecords lrecords, long length) throws IOException {
        if (length > 0) {
            byte[] arr = new byte[(int) length];
            rafile.read(arr);
            setData(arr);
            readDataArray(rafile, fheader, lrecords);
        }
    }

    private void readDataArray(RandomAccessFile rafile, FileHeader fheader, LayerRecords lrecords) throws IOException {
        // Image data.:?
        // If the compression code is 0, the image data is just the raw image data, whose size is calculated as (LayerBottom-LayerTop)* (LayerRight-LayerLeft) (from the first field in See Layer records).
        // If the compression code is 1, the image data starts with the byte counts for all the scan lines in the channel (LayerBottom-LayerTop) , with each count stored as a two-byte value.(**PSB** each count stored as a four-byte value.) The RLE compressed data follows, with each scan line compressed separately. The RLE compression is the same compression algorithm used by the Macintosh ROM routine PackBits, and the TIFF standard.
        // If the layer's size, and therefore the data, is odd, a pad byte will be inserted at the end of the row.
        // If the layer is an adjustment layer, the channel data is undefined (probably all white.)
        readCompression(rafile);
        rafile.seek(rafile.getFilePointer() - 2);
        switch (compression) {
            case 0:
                imageDataRaw(rafile, fheader, lrecords);
                break;
            case 1:
                imageDataRLE(rafile, fheader, lrecords);
                break;
            case 2:
                imageDataZip(rafile, fheader, lrecords);
                break;
            case 3:
                imageDataZipPrediction(rafile, fheader, lrecords);
                break;
            default:
                throw new IOException("The Compression Method of the Channel Image Data is wrong.");
        }
    }

    private void imageDataRaw(RandomAccessFile rafile, FileHeader fheader, LayerRecords lrecords) {
    }

    private void imageDataRLE(RandomAccessFile rafile, FileHeader fheader, LayerRecords lrecords) {
    }

    private void imageDataZip(RandomAccessFile rafile, FileHeader fheader, LayerRecords lrecords) {
    }

    private void imageDataZipPrediction(RandomAccessFile rafile, FileHeader fheader, LayerRecords lrecords) {
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Channel Image Data Length: " + getDataLength());
        sbuilder.append("/image data compression: " + getCompression());
        return sbuilder.toString();
    }
}
