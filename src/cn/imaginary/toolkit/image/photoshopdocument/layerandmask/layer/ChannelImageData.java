package cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ChannelImageData {

    public ChannelImageData() {}

    private int compression;

    private byte[] arr_ImageData;

    public int getCompression() {
        return compression;
    }

    public void read(
        RandomAccessFile rafile,
        int length,
        int layerTop,
        int layerLeft,
        int layerBottom,
        int layerRight
    ) {
        try {
            // Compression. 0 = Raw Data, 1 = RLE compressed, 2 = ZIP without prediction, 3 = ZIP with prediction.
            compression = rafile.readInt();

            // Image data.:?
            // If the compression code is 0, the image data is just the raw image data, whose size is calculated as (LayerBottom-LayerTop)* (LayerRight-LayerLeft) (from the first field in See Layer records).
            // If the compression code is 1, the image data starts with the byte counts for all the scan lines in the channel (LayerBottom-LayerTop) , with each count stored as a two-byte value.(**PSB** each count stored as a four-byte value.) The RLE compressed data follows, with each scan line compressed separately. The RLE compression is the same compression algorithm used by the Macintosh ROM routine PackBits, and the TIFF standard.
            // If the layer's size, and therefore the data, is odd, a pad byte will be inserted at the end of the row.
            // If the layer is an adjustment layer, the channel data is undefined (probably all white.)
            switch (compression) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:
                    break;
            }
            arr_ImageData = null;
        } catch (IOEception e) {}
    }
}
