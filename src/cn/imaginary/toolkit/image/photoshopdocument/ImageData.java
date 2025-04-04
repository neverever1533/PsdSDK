package cn.imaginary.toolkit.image.photoshopdocument;

import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.LayerInfo;
// import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer.ChannelImageData;
// import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer.ChannelInfo;
// import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer.LayerRecords;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ImageData {

    public ImageData() {}

    private int compression_Method;

    public static String[] arr_Compression_Method = { "Raw", "RLE", "ZIP", "ZIP_Prediction" };

    private long length_ImageData;

    private byte[] arr_ImageData;

    public int getCompressionMethod() {
        return compression_Method;
    }

    public String getCompressionMethodName() {
        return arr_Compression_Method[compression_Method];
    }

    public long getLength() {
        return length_ImageData;
    }

    public void read(RandomAccessFile rafile, FileHeader fheader, LayerInfo linfo) {
        if (null == fheader) {
            return;
        }
        try {
            length_ImageData = rafile.length() - rafile.getFilePointer();
            //5.1 Compression Method2
            // Compression method:
            // 0 = Raw image data
            // 1 = RLE compressed the image data starts with the byte counts for all the scan lines (rows * channels), with each count stored as a two-byte value. The RLE compressed data follows, with each scan line compressed separately. The RLE compression is the same compression algorithm used by the Macintosh ROM routine PackBits , and the TIFF standard.
            // 2 = ZIP without prediction
            // 3 = ZIP with prediction.

            compression_Method = rafile.readShort();

            //5.2 Image Data：?
            // The image data. Planar order = RRR GGG BBB, etc.

            switch (compression_Method) {
                case 0:
                    imageDataRaw(rafile, fheader, linfo);
                    break;
                case 1:
                    imageDataRLE(rafile, fheader, linfo);
                    break;
                case 2:
                    imageDataZip(rafile, fheader, linfo);
                    break;
                case 3:
                    imageDataZipPrediction(rafile, fheader, linfo);
                    break;
                default:
                    throw new IOException("Unknown Compression Method");
            }

            System.out.println("5.location_ImageData: " + rafile.getFilePointer());
        } catch (IOException e) {}
    }

    private void imageDataRaw(RandomAccessFile rafile, FileHeader fheader, LayerInfo linfo) {
        //0 = Raw image data
    }

    private void imageDataRLE(RandomAccessFile rafile, FileHeader fheader, LayerInfo linfo) {
        //1 = RLE compressed the image data starts with the byte counts for all the scan lines (rows * channels), with each count stored as a two-byte value. The RLE compressed data follows, with each scan line compressed separately. The RLE compression is the same compression algorithm used by the Macintosh ROM routine PackBits , and the TIFF standard.
        int height = fheader.getHeight();
        int channels = fheader.getChannels();
        int scanLines = height * channels;
        short[] arr_Count_ScanLines = new short[scanLines];
        try {
            for (int i = 0; i < scanLines; i++) {
                arr_Count_ScanLines[i] = rafile.readShort();
            }
        } catch (IOException e) {}
    }

    private void imageDataZip(RandomAccessFile rafile, FileHeader fheader, LayerInfo linfo) {
        //2 = ZIP without prediction
    }

    private void imageDataZipPrediction(RandomAccessFile rafile, FileHeader fheader, LayerInfo linfo) {
        //3 = ZIP with prediction.
    }

    private void RleCompress(byte[] src, byte[] dest) {}

    private void RleDecompress(byte[] src, byte[] dest) {}

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Compression Method: " + compression_Method);
        sbuilder.append("/Image Data Length: " + length_ImageData);
        return sbuilder.toString();
    }
}
