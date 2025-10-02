package cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer;

import cn.imaginary.toolkit.image.photoshopdocument.FileHeader;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.LayerRecords;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
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
    private long length_Data_Image;

    private byte[] arr_Data;
    private byte[] arr_Data_Image;

    public ChannelImageData() {}

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

    public byte[] getImageData() {
        return arr_Data_Image;
    }

    public void setImageData(byte[] array) {
        arr_Data_Image = array;
    }

    public int getCompression() {
        return compression;
    }

    public void setCompression(int compression) {
        this.compression = compression;
    }

    private void readCompression(DataInputStream dinstream) throws IOException {
        // Compression. 0 = Raw Data, 1 = RLE compressed, 2 = ZIP without prediction, 3 = ZIP with prediction.
        setCompression(dinstream.readShort());
    }

    private long readDataLength() {
        return getDataLength();
    }

    //    public void read(RandomAccessFile rafile, FileHeader fheader, LayerRecords lrecords) {
    public void read(RandomAccessFile rafile, FileHeader fheader, int width, int height) {
        try {
            long location = rafile.getFilePointer();

            readData(rafile, fheader, width, height);

            length_ += length_Data;
            //            System.out.println("Channel Image data space: " + (location + length_ - rafile.getFilePointer()));
            rafile.seek(location + getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readData(RandomAccessFile rafile, FileHeader fheader, int width, int height) throws IOException {
        if (length_Data > 0) {
            /*if (length_Data >= 2) {
                readCompression(rafile);
                rafile.seek(rafile.getFilePointer() - 2);
            }*/
            byte[] arr = new byte[(int) length_Data];
            rafile.read(arr);
            setData(arr);
            readDataArray(arr, fheader, width, height);
        }
    }

    private void readDataArray(byte[] array, FileHeader fheader, int width, int height) {
        try {
            DataInputStream dinstream = new DataInputStream(new ByteArrayInputStream(array));

            readCompression(dinstream);
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
        switch (compression) {
            case 0:
                readImageDataRaw(dinstream, fheader, width, height);
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

    private void readImageDataRaw(DataInputStream dinstream, FileHeader fheader, int width, int height)
        throws IOException {
        int length_Image = width * height;
        byte[] arr_Data_Image = new byte[length_Image];
        dinstream.read(arr_Data_Image);
        setImageData(arr_Data_Image);
    }

    private void readImageDataRLE(DataInputStream dinstream, FileHeader fheader, int width, int height)
        throws IOException {
        int offset = 2;
        byte[] arr_ScanLine = new byte[height];
        int count;
        for (int i = 0; i < height; i++) {
            if (!fheader.isFilePsb()) {
                count = dinstream.readShort();
                offset += 2;
            } else {
                count = dinstream.readInt();
                offset += 4;
            }
            arr_ScanLine[i] = (byte) count;
        }
        /*System.out.println("arr_ScanLine: ");
        System.out.println(arr_ScanLine[0]);
        System.out.println(arr_ScanLine[1]);
        System.out.println(arr_ScanLine[2]);
        System.out.println(arr_ScanLine[3]);*/

        /*int length = length_Data - offset;
        byte[] arr_Image = new byte[length];
        dinstream.read(arr_Image);
        int length_Image = width * height;
        byte[] arr_Data_Image = new byte[length_Image];
        byte[] arr;
        int length;
        for (int i = 0; i < height; i++) {
            count = arr_ScanLine[i];
            if (count < 0) {
                count = 1 - count;
                length = arr_ScanLine[i++];
                arr = new byte[length];
                dinstream.read(arr);
            }
        }*/
    }

    private void readImageDataZip(DataInputStream dinstream, FileHeader fheader, int width, int height)
        throws IOException {}

    private void readImageDataZipPrediction(DataInputStream dinstream, FileHeader fheader, int width, int height)
        throws IOException {}

    private void readImageData(byte[] array, FileHeader fheader, LayerRecords lrecords) throws IOException {
        // Image data.:?
        // If the compression code is 0, the image data is just the raw image data, whose size is calculated as (LayerBottom-LayerTop)* (LayerRight-LayerLeft) (from the first field in See Layer records).
        // If the compression code is 1, the image data starts with the byte counts for all the scan lines in the channel (LayerBottom-LayerTop) , with each count stored as a two-byte value.(**PSB** each count stored as a four-byte value.) The RLE compressed data follows, with each scan line compressed separately. The RLE compression is the same compression algorithm used by the Macintosh ROM routine PackBits, and the TIFF standard.
        // If the layer's size, and therefore the data, is odd, a pad byte will be inserted at the end of the row.
        // If the layer is an adjustment layer, the channel data is undefined (probably all white.)
        switch (compression) {
            case 0:
                readImageDataRaw(array, fheader, lrecords);
                break;
            case 1:
                readImageDataRLE(array, fheader, lrecords);
                break;
            case 2:
                readImageDataZip(array, fheader, lrecords);
                break;
            case 3:
                readImageDataZipPrediction(array, fheader, lrecords);
                break;
            default:
                throw new IOException("The Compression Method of the Channel Image Data is wrong.");
        }
    }

    private void readImageDataRaw(byte[] array, FileHeader fheader, LayerRecords lrecords) {}

    private void readImageDataRLE(byte[] array, FileHeader fheader, LayerRecords lrecords) {}

    private void readImageDataZip(byte[] array, FileHeader fheader, LayerRecords lrecords) {}

    private void readImageDataZipPrediction(byte[] array, FileHeader fheader, LayerRecords lrecords) {}

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Channel Image Data Length: " + getDataLength());
        sbuilder.append("/image data compression: " + getCompression());
        return sbuilder.toString();
    }
}
