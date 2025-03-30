package cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer;

import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer.ChannelImageData;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer.ChannelInfo;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.mask.LayerBlendingRangesData;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.mask.LayerMaskOrAdjustmentLayerData;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LayerRecords {

    public LayerRecords() {}

    private int length_LayerRecords;

    private int top;
    private int left;
    private int bottom;
    private int right;
    private int channels;

    private String signature;
    private String Signature_BlendMode = "8BIM";

    private String blendMode;
    private int opacity;
    private int clipping;
    private int flags;
    private int filler;
    private int length_ExtraData;

    private String name_Layer;

    public void read(RandomAccessFile rafile) {
        try {
            //4.2.3.1 Rectangle:4*4
            // Rectangle containing the contents of the layer. Specified as top, left, bottom, right coordinates
            top = rafile.readInt();
            left = rafile.readInt();
            bottom = rafile.readInt();
            right = rafile.readInt();

            //4.2.3.2 Channels:2
            // Number of channels in the layer
            channels = rafile.readShort();
            //channels = rafile.readShort() & 0xFF;

            //4.2.3.3 Channel Info ?
            for (int j = 0; j < channels; j++) {
                // Channel information. Six bytes per channel, consisting of:
                // 2 bytes for Channel ID: 0 = red, 1 = green, etc.;
                // -1 = transparency mask; -2 = user supplied layer mask, -3 real user supplied layer mask (when both a user mask and a vector mask are present)
                ChannelInfo cinfo = new ChannelInfo();
                //4.2.3.3.1 Channel ID:2
                cinfo.setID(rafile.readShort());

                //4.2.3.3.2 Channel Data Length:4
                // 4 bytes for length of corresponding channel data. (**PSB** 8 bytes for length of corresponding channel data.) See See Channel image data for structure of channel data.
                cinfo.setLength(rafile.readInt());
                System.out.println(cinfo.toString());
                System.out.println();
            }
            //4.2.3.4 Blend Mode Signature:4
            // Blend mode signature: '8BIM
            byte[] arr = new byte[4];
            rafile.read(arr);
            signature = new String(arr);

            //4.2.3.5 Blend Mode:4
            // Blend mode key:
            // 'pass' = pass through, 'norm' = normal, 'diss' = dissolve, 'dark' = darken, 'mul ' = multiply, 'idiv' = color burn, 'lbrn' = linear burn, 'dkCl' = darker color, 'lite' = lighten, 'scrn' = screen, 'div ' = color dodge, 'lddg' = linear dodge, 'lgCl' = lighter color, 'over' = overlay, 'sLit' = soft light, 'hLit' = hard light, 'vLit' = vivid light, 'lLit' = linear light, 'pLit' = pin light, 'hMix' = hard mix, 'diff' = difference, 'smud' = exclusion, 'fsub' = subtract, 'fdiv' = divide 'hue ' = hue, 'sat ' = saturation, 'colr' = color, 'lum ' = luminosity,
            arr = new byte[4];
            rafile.read(arr);
            blendMode = new String(arr);

            //4.2.3.6 Opacity:1
            // Opacity. 0 = transparent ... 255 = opaque
            opacity = rafile.readByte() & 0xFF;

            //4.2.3.7 Clipping:1
            // Clipping: 0 = base, 1 = non-base
            clipping = rafile.readByte();
            //clipping = rafile.readByte() & 0xFF;

            //4.2.3.8 Flags:1
            // Flags:
            // bit 0 = transparency protected;
            // bit 1 = visible;
            // bit 2 = obsolete;
            // bit 3 = 1 for Photoshop 5.0 and later, tells if bit 4 has useful information;
            // bit 4 = pixel data irrelevant to appearance of document
            flags = rafile.readByte();
            //flags = rafile.readByte() & 0xFF;

            //4.2.3.9 Filler:1
            // Filler (zero)
            filler = rafile.readByte();
            //filler = rafile.readByte() & 0xFF;

            //4.2.3.10 Extra Data Length:4
            // Length of the extra data field ( = the total length of the next five fields).
            length_ExtraData = rafile.readInt();

            //rafile.skipBytes(length_ExtraData);

            //4.2.3.11 Extra Data ?(Layer mask / adjustment layer data)
            // Layer mask data: See See Layer mask / adjustment layer data for structure. Can be 40 bytes, 24 bytes, or 4 bytes if no layer mask
            LayerMaskOrAdjustmentLayerData lmoaldata = new LayerMaskOrAdjustmentLayerData();
            lmoaldata.read(rafile);
            int length_lmoaldata = lmoaldata.getLength();

            //4.2.3.12 Layer Blending Ranges:?
            // Layer blending ranges: See See Layer blending ranges data.
            LayerBlendingRangesData lbrdata = new LayerBlendingRangesData();
            lbrdata.read(rafile);
            int length_lbrdata = lbrdata.getLength();

            //4.2.3.13 Layer Name:?
            // Layer name: Pascal string, padded to a multiple of 4 bytes.
            int length_Name = length_ExtraData - length_lmoaldata - length_lbrdata;
            if (length_Name > 0) {
                arr = new byte[length_Name];
                rafile.read(arr);
                name_Layer = new String(arr);
            } else {
                length_Name = 0;
            }
            length_LayerRecords = 16 + 2 + channels * 6 + 4 + 4 + 1 + 1 + 1 + 1 + 4 + length_ExtraData + length_Name;
        } catch (IOException e) {}
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Top: " + top + "/Left: " + left + "/Bottom: " + bottom + "/Right: " + right);
        sbuilder.append("/Channels: " + channels);
        sbuilder.append("/Signature: " + signature);
        sbuilder.append("/BlendMode: " + blendMode);
        sbuilder.append("/Opacity: " + opacity);
        sbuilder.append("/Clipping: " + clipping);
        sbuilder.append("/Flags: " + flags);
        sbuilder.append("/Filler: " + filler);
        sbuilder.append("/Extra Data Length: " + length_ExtraData);
        return sbuilder.toString();
    }
}
