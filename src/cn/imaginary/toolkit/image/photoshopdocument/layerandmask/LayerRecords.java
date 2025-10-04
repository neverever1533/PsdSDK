package cn.imaginary.toolkit.image.photoshopdocument.layerandmask;

import cn.imaginary.toolkit.image.photoshopdocument.FileHeader;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.additional.SectionDvider;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.additional.UnicodeLayerName;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer.ChannelInfo;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.mask.BlendingRangesData;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.mask.MaskOrAdjustmentData;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class LayerRecords {

    public static String Signature_8BIM = "8BIM";
    public static String[] arr_key_BlendMode = {
            "pass",
            "norm",
            "diss",
            "dark",
            "mul ",
            "idiv",
            "lbrn",
            "dkCl",
            "lite",
            "scrn",
            "div ",
            "lddg",
            "lgCl",
            "over",
            "sLit",
            "hLit",
            "vLit",
            "lLit",
            "pLit",
            "hMix",
            "diff",
            "smud",
            "fsub",
            "fdiv",
            "hue ",
            "sat ",
            "colr",
            "lum ",
    };

    private ArrayList<ChannelInfo> arrayList_ChannelInfo;

    private ArrayList<AdditionalLayerInfo> arrayList_AdditionalLayerInfo;

    private long length_;

    private String signature;
    private String key_BlendMode;

    private int length_Data_Extra;
    private int top;
    private int left;
    private int bottom;
    private int right;
    private int channels;
    private int blendMode;
    private int opacity;
    private int clipping;
    private int flags;
    private int filler;
    private int layerType;
    private int layerType_Sub;

    private boolean is_Transparency_Protected;
    private boolean is_Visible;
    private boolean is_Obsolete;
    private boolean is_Pixel_Data_Useful;
    private boolean is_Pixel_Data_Irrelevant;

    private byte[] arr_Name;

    private BlendingRangesData brdata;

    private MaskOrAdjustmentData moadata;

    private Charset charset;

    private byte[] arr_Data_Extra;

    public LayerRecords() {
    }

    public long getLength() {
        return length_;
    }

    public void setLength(long length) {
        length_ = length;
    }

    public int getExtraDataLength() {
        return length_Data_Extra;
    }

    public void setExtraDataLength(int length) {
        length_Data_Extra = length;
    }

    public byte[] getExtraData() {
        return arr_Data_Extra;
    }

    public void setExtraData(byte[] array) {
        arr_Data_Extra = array;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getHeight() {
        return bottom - top;
    }

    public int getWidth() {
        return right - left;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public ArrayList<AdditionalLayerInfo> getAdditionalLayerInfoList() {
        return arrayList_AdditionalLayerInfo;
    }

    public ArrayList<ChannelInfo> getChannelInfoList() {
        return arrayList_ChannelInfo;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) throws IOException {
        if (null == signature || !signature.equalsIgnoreCase(Signature_8BIM)) {
            throw new IOException("BlendMode signature is wrong.");
        } else {
            this.signature = signature;
        }
    }

    public String getBlendModeKey() {
        return key_BlendMode;
    }

    public boolean isSupportedBlendModeKey(String key) {
        if (null != key) {
            for (int i = 0; i < arr_key_BlendMode.length; i++) {
                if (key.equalsIgnoreCase(arr_key_BlendMode[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setBlendModeKey(String key) throws IOException {
        if (isSupportedBlendModeKey(key)) {
            key_BlendMode = key;
        } else {
            throw new IOException("The key of the BlendMode in the LayerRecords is wrong.");
        }
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public int getClipping() {
        return clipping;
    }

    public void setClipping(int clipping) {
        this.clipping = clipping;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public boolean isTransparencyProtected() {
        return is_Transparency_Protected;
    }

    public void setTransparencyProtected(boolean isTransparencyProtected) {
        is_Transparency_Protected = isTransparencyProtected;
    }

    public boolean isVisible() {
        return is_Visible;
    }

    public void setVisible(boolean isVisible) {
        is_Visible = isVisible;
    }

    public boolean isObsolete() {
        return is_Obsolete;
    }

    public void isObsolete(boolean isObsolete) {
        is_Obsolete = isObsolete;
    }

    public boolean isPixelDataUseful() {
        return is_Pixel_Data_Useful;
    }

    public void setPixelDataUseful(boolean isPixelDataUseful) {
        is_Pixel_Data_Useful = isPixelDataUseful;
    }

    public boolean isPixelDataIrrelevant() {
        return is_Pixel_Data_Irrelevant;
    }

    public void setPixelDataIrrelevant(boolean isPixelDataIrrelevant) {
        is_Pixel_Data_Irrelevant = isPixelDataIrrelevant;
    }

    public int getFiller() {
        return filler;
    }

    public void setFiller(int filler) {
        this.filler = 0;
    }

    public MaskOrAdjustmentData getMaskOrAdjustmentLayerData() {
        return moadata;
    }

    public BlendingRangesData getBlendingRangesData() {
        return brdata;
    }

    public String getName(String charsetName) {
        return getName(Charset.forName(charsetName));
    }

    public String getName(Charset charset) {
        return new String(getNameBytes(), charset);
    }

    public void setNameBytes(byte[] array, String charsetName) {
        setNameBytes(array, Charset.forName(charsetName));
    }

    public void setNameBytes(byte[] array, Charset charset) {
        setNameBytes(array);
        setCharset(charset);
    }

    private void setNameBytes(byte[] array) {
        arr_Name = array;
    }

    public byte[] getNameBytes() {
        return arr_Name;
    }

    public Charset getCharset() {
        if (null == charset) {
            charset = Charset.defaultCharset();
        }
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public void setCharset(String charsetName) {
        setCharset(Charset.forName(charsetName));
    }

    public int getLayerType() {
        return layerType;
    }

    public void setLayerType(int type) {
        layerType = type;
    }

    public String getLayerTypeName() {
        return SectionDvider.getLayerTypeName(layerType);
    }

    public int getSubLayerType() {
        return layerType_Sub;
    }

    public void setSubLayerType(int subType) {
        layerType_Sub = subType;
    }

    public String getSubLayerTypeName() {
        return SectionDvider.getSubLayerTypeName(layerType_Sub);
    }

    public void read(DataInputStream dinstream, FileHeader fheader) {
        try {
            readData(dinstream, fheader);
            readExtraDataLength(dinstream);
            readExtraData(dinstream, fheader, getExtraDataLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readData(DataInputStream dinstream, FileHeader fheader) throws IOException {
        //Rectangle:4*4
        //4*4,Rectangle containing the contents of the layer. Specified as top, left, bottom, right coordinates
        setTop(dinstream.readInt());
        setLeft(dinstream.readInt());
        setBottom(dinstream.readInt());
        setRight(dinstream.readInt());
        length_ += 16;

        //Channels:2
        //2,Number of channels in the layer
        int channels = dinstream.readShort();
        setChannels(channels);
        length_ += 2;

        //6 * number of channels,Channel information. Six bytes per channel, consisting of:
        //2 bytes for Channel ID: 0 = red, 1 = green, etc.;
        //-1 = transparency mask; -2 = user supplied layer mask, -3 real user supplied layer mask (when both a user mask and a vector mask are present)
        //4 bytes for length of corresponding channel data. (**PSB** 8 bytes for length of corresponding channel data.) See See Channel image data for structure of channel data.
        arrayList_ChannelInfo = new ArrayList<ChannelInfo>();
        for (int j = 0; j < channels; j++) {
            ChannelInfo cinfo = new ChannelInfo();
            cinfo.read(dinstream, fheader);
            arrayList_ChannelInfo.add(j, cinfo);
            length_ += cinfo.getLength();
            //            System.out.println(cinfo.toString());
        }

        //Blend Mode Signature:4
        //4,Blend mode signature: '8BIM
        byte[] arr = new byte[4];
        dinstream.read(arr);
        length_ += 4;
        setSignature(new String(arr));

        //Blend Mode Key:4
        //4,Blend mode key:
        // 'pass' = pass through, 'norm' = normal, 'diss' = dissolve, 'dark' = darken, 'mul ' = multiply, 'idiv' = color burn, 'lbrn' = linear burn, 'dkCl' = darker color, 'lite' = lighten, 'scrn' = screen, 'div ' = color dodge, 'lddg' = linear dodge, 'lgCl' = lighter color, 'over' = overlay, 'sLit' = soft light, 'hLit' = hard light, 'vLit' = vivid light, 'lLit' = linear light, 'pLit' = pin light, 'hMix' = hard mix, 'diff' = difference, 'smud' = exclusion, 'fsub' = subtract, 'fdiv' = divide 'hue ' = hue, 'sat ' = saturation, 'colr' = color, 'lum ' = luminosity,
        arr = new byte[4];
        dinstream.read(arr);
        length_ += 4;
        setBlendModeKey(new String(arr));

        //Opacity:1
        //1,Opacity. 0 = transparent ... 255 = opaque
        setOpacity(dinstream.readByte() & 0xFF);
        length_ += 1;

        //Clipping:1
        //1,Clipping: 0 = base, 1 = non-base
        setClipping(dinstream.readByte());
        length_ += 1;

        //Flags:1
        //1,Flags:
        // bit 0 = transparency protected;
        // bit 1 = visible;
        // bit 2 = obsolete;
        // bit 3 = 1 for Photoshop 5.0 and later, tells if bit 4 has useful information;
        // bit 4 = pixel data irrelevant to appearance of document
        int flags = dinstream.readByte();
        setFlags(flags);
        length_ += 1;
        is_Transparency_Protected = (flags % 2) != 0;
        is_Visible = ((flags >> 1) % 2) != 0;
        is_Obsolete = ((flags >> 2) % 2) == 0;
        is_Pixel_Data_Useful = ((flags >> 3) % 2) != 0;
        if (is_Pixel_Data_Useful) {
            is_Pixel_Data_Irrelevant = ((flags >> 4) % 2) != 0;
        }

        //Filler:1
        //1,Filler (zero)
        setFiller(dinstream.readByte());
        length_ += 1;
    }

    private void readExtraDataLength(DataInputStream dinstream) throws IOException {
        //Extra Data Length:4
        //4,Length of the extra data field ( = the total length of the next five fields).
        setExtraDataLength(dinstream.readInt());
        length_ += 4 + getExtraDataLength();
    }

    private void readExtraData(DataInputStream dinstream, FileHeader fheader, int length) throws IOException {
        if (length > 0) {
            byte[] arr = new byte[length];
            dinstream.read(arr);
            setExtraData(arr);
            readExtraDataArray(arr, fheader);
        }
    }

    private void readExtraDataArray(byte[] array, FileHeader fheader) throws IOException {
        try {
            DataInputStream dinstream = new DataInputStream(new ByteArrayInputStream(array));
            //Layer mask data:variable
            //variable,Layer mask data: See See Layer mask / adjustment layer data for structure. Can be 40 bytes, 24 bytes, or 4 bytes if no layer mask
            moadata = new MaskOrAdjustmentData();
            moadata.read(dinstream);
            int length_moadata = moadata.getLength();
            System.out.println(moadata.toString());

            //Layer Blending Ranges:variable
            //variable,Layer blending ranges: See See Layer blending ranges data.
            brdata = new BlendingRangesData();
            brdata.read(dinstream);
            int length_brdata = brdata.getLength();
            System.out.println(brdata.toString());

            //Layer Name:variable
            //variable,Layer name: Pascal string, padded to a multiple of 4 bytes.
            int length_Name = dinstream.readByte() & 0xFF;
            int temp = (1 + length_Name) % 4;
            int len;
            if (temp != 0) {
                len = 4 - temp;
            } else {
                len = 0;
            }
            length_Name += len;
            byte[] arr = new byte[length_Name];
            dinstream.read(arr);
            setCharset("gbk");
            setNameBytes(arr);
            System.out.print("layerrecords read name length: " + length_Name);
            System.out.println("/name: " + getName("gbk"));
            //        System.out.println("name_Layer: " + getName(getCharset()));

            int length_alinfo = array.length - length_moadata - length_brdata - length_Name;
            readAdditionalLayerInfo(dinstream, fheader, length_alinfo);
            dinstream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readAdditionalLayerInfo(DataInputStream dinstream, FileHeader fheader, int length) throws IOException {
        //Additional Layer Information:
        //There are several types of layer information that have been added in Photoshop 4.0 and later. These exist at the end of the layer records structure (see the last row of See Layer records). They have the following structure:
        arrayList_AdditionalLayerInfo = new ArrayList<AdditionalLayerInfo>();

        String signature;
        long offset = 0;
        byte[] arr;
        while (offset < length) {
            dinstream.mark((int) offset);
            arr = new byte[4];
            dinstream.read(arr);
            signature = new String(arr);
            dinstream.reset();
            if (
                    signature.equalsIgnoreCase(AdditionalLayerInfo.Signature_8BIM) ||
                            signature.equalsIgnoreCase(AdditionalLayerInfo.Signature_8B64)
            ) {
                AdditionalLayerInfo alinfo = new AdditionalLayerInfo();
                alinfo.read(dinstream, fheader);
                String key = alinfo.getKey();
                if (null != key) {
                    arrayList_AdditionalLayerInfo.add(alinfo);
                    readAdditionLayerInfo(alinfo.getData(), key);
                    System.out.println(alinfo.toString());
                    offset += alinfo.getLength();
                } else {
                    offset++;
                    // throw new IOException("The key of the Additional Layer Information is wrong.");
                }
            } else {
                offset++;
                // throw new IOException("The Signature of the Additional Layer Information is wrong.");
            }
        }
    }

    private void readAdditionLayerInfo(byte[] array, String key) throws IOException {
        if (key.equalsIgnoreCase(SectionDvider.Key)) {
            SectionDvider sdvider = new SectionDvider();
            sdvider.read(array, key);
            setLayerType(sdvider.getLayerType());
            setSubLayerType(sdvider.getSubLayerType());
            System.out.println(sdvider.toString());
        } else if (key.equalsIgnoreCase(UnicodeLayerName.Key)) {
            UnicodeLayerName ulname = new UnicodeLayerName();
            ulname.read(array, key);
            byte[] arr = ulname.getData();
            if (null != arr) {
                setNameBytes(arr, ulname.getCharset());
                System.out.println(ulname.toString());
            }
        }
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Layer Records Length: " + getLength());
        sbuilder.append("/Top: " + top + "/Left: " + left + "/Bottom: " + bottom + "/Right: " + right);
        sbuilder.append("/Channels: " + channels);
        sbuilder.append("/Signature: " + signature);
        sbuilder.append("/BlendMode: " + blendMode);
        sbuilder.append("/Opacity: " + opacity);
        sbuilder.append("/Clipping: " + clipping);
        sbuilder.append("/Flags: " + flags);
        sbuilder.append("/is_Transparency_Protected: " + is_Transparency_Protected);
        sbuilder.append("/is_Visible: " + is_Visible);
        sbuilder.append("/is_Obsolete: " + is_Obsolete);
        sbuilder.append("/is_Pixel_Data_Useful: " + is_Pixel_Data_Useful);
        sbuilder.append("/is_Pixel_Data_Irrelevant: " + is_Pixel_Data_Irrelevant);
        sbuilder.append("/Filler: " + filler);
        sbuilder.append("/Layer Type: " + getLayerType());
        sbuilder.append("/Layer Type Name: " + getLayerTypeName());
        sbuilder.append("/Sub Layer Type: " + getSubLayerType());
        sbuilder.append("/Sun Layer Type Name: " + getSubLayerTypeName());
        sbuilder.append("/arr Name length: " + getNameBytes().length);
//        sbuilder.append("/Layer Name: " + getName("gbk"));
        sbuilder.append("/Layer Name: " + getName(getCharset()));
        return sbuilder.toString();
    }
}
