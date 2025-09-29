package cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer;

import cn.imaginary.toolkit.image.photoshopdocument.FileHeader;
import cn.imaginary.toolkit.image.rle.PackBitsUtils;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ChannelImageData {

    private int Raw_Data = 0;
    private int RLE_Compressed = 1;
    private int ZIP_Without_Prediction = 2;
    private int ZIP_With_Prediction = 3;
    private int compression;
    
    private long length_;
    private long length_Data;

    public ChannelImageData() {}

    public long getLength() {
      return length_;
    }
    
    public int getCompression() {
        return compression;
    }

    private void readCompression(RandomAccessFile rafile) throws IOException {
        // Compression. 0 = Raw Data, 1 = RLE compressed, 2 = ZIP without prediction, 3 = ZIP with prediction.
        compression = rafile.readShort();
        length_ += 2;
    }

    private void readDataLength(ChannelInfo cinfo) {
        length_Data = cinfo.getDataLength();
    }

    public void read(RandomAccessFile rafile, FileHeader fheader, LayerRecords lrecords, ChannelInfo cinfo) {
        try {
            long location = rafile.getFilePointer();
            readCompression(rafile);
            readDataLength(cinfo);
            readData(rafile, fheader, lrecords, cinfo);
            length_ += length_Data;
            rafile.seek(location + getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readData(RandomAccessFile rafile, FileHeader fheader, LayerRecords lrecords, ChannelInfo cinfo)
        throws IOException {
        // Image data.:?
        // If the compression code is 0, the image data is just the raw image data, whose size is calculated as (LayerBottom-LayerTop)* (LayerRight-LayerLeft) (from the first field in See Layer records).
        // If the compression code is 1, the image data starts with the byte counts for all the scan lines in the channel (LayerBottom-LayerTop) , with each count stored as a two-byte value.(**PSB** each count stored as a four-byte value.) The RLE compressed data follows, with each scan line compressed separately. The RLE compression is the same compression algorithm used by the Macintosh ROM routine PackBits, and the TIFF standard.
        // If the layer's size, and therefore the data, is odd, a pad byte will be inserted at the end of the row.
        // If the layer is an adjustment layer, the channel data is undefined (probably all white.)
        // byte[] arr;
        switch (compression) {
            case 0:
                imageDataRaw(rafile, fheader, lrecords, cinfo);
                break;
            case 1:
                imageDataRLE(rafile, fheader, lrecords, cinfo);
                break;
            case 2:
                imageDataZip(rafile, fheader, lrecords, cinfo);
                break;
            case 3:
                imageDataZipPrediction(rafile, fheader, lrecords, cinfo);
                break;
            default:
                throw new IOException("The Compression Method of the Channel Image Data is wrong.");
        }
    }

    private void imageDataRaw(RandomAccessFile rafile, FileHeader fheader, LayerRecords lrecords, ChannelInfo cinfo) {}

    private void imageDataRLE(RandomAccessFile rafile, FileHeader fheader, LayerRecords lrecords, ChannelInfo cinfo) {}

    private void imageDataZip(RandomAccessFile rafile, FileHeader fheader, LayerRecords lrecords, ChannelInfo cinfo) {}

    private void imageDataZipPrediction(
        RandomAccessFile rafile,
        FileHeader fheader,
        LayerRecords lrecords,
        ChannelInfo cinfo
    ) {}
}
