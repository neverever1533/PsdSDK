package cn.imaginary.toolkit.image.photoshopdocument.layerandmask;

import cn.imaginary.toolkit.image.photoshopdocument.FileHeader;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.additional.SectionDvider;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.additional.UnicodeLayerName;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer.ChannelImageData;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer.ChannelInfo;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.mask.BlendingRangesData;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.mask.MaskOrAdjustmentData;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedMap;

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

    public LayerRecords() {
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public long getLength() {
        return length_;
    }

    public int getTop() {
        return top;
    }

    public int getLeft() {
        return left;
    }

    public int getBottom() {
        return bottom;
    }

    public int getRight() {
        return right;
    }

    public int getChannels() {
        return channels;
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

    public String getBlendModeKey() {
        return key_BlendMode;
    }

    public int getBlendMode() {
        return blendMode;
    }

    public int getOpacity() {
        return opacity;
    }

    public int getClipping() {
        return clipping;
    }

    public int getFlags() {
        return flags;
    }

    public boolean isTransparencyProtected() {
        return is_Transparency_Protected;
    }

    public boolean isVisible() {
        return is_Visible;
    }

    public boolean isObsolete() {
        return is_Obsolete;
    }

    public boolean isPixelDataUseful() {
        return is_Pixel_Data_Useful;
    }

    public boolean isPixelDataIrrelevant() {
        return is_Pixel_Data_Irrelevant;
    }

    public int getFiller() {
        return filler;
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

    public void read(RandomAccessFile rafile, FileHeader fheader) {
        try {
            long location = rafile.getFilePointer();

            readRectangle(rafile);
            readChannelInfo(rafile, fheader);
            readBlendModeSignature(rafile);
            readBlendModeKey(rafile);
            readOpacity(rafile);
            readClipping(rafile);
            readFlags(rafile);
            readFiller(rafile);

            int length_Data_Extra = readExtraDataLength(rafile);
            readExtraData(rafile, fheader, length_Data_Extra);

            length_ += length_Data_Extra;
            //            System.out.println("layer records space: " + (location + length_ - rafile.getFilePointer()));
            rafile.seek(location + getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int readName(RandomAccessFile rafile) throws IOException {
        //4.2.3.13 Layer Name:?
        // Layer name: Pascal string, padded to a multiple of 4 bytes.
        int length_Name = rafile.readByte() & 0xFF;
        int temp = (1 + length_Name) % 4;
        int len;
        if (temp != 0) {
            len = 4 - temp;
        } else {
            len = 0;
        }
        length_Name += len;
        byte[] arr = new byte[length_Name];
        rafile.read(arr);
        setNameBytes(arr);
        System.out.print("layerrecords read name length: " + length_Name);
        System.out.println("/name: " + getName("gbk"));
        //        System.out.println("name_Layer: " + getName(getCharset()));
        return 1 + length_Name;
    }

    private long readLayerBlendingRangesData(RandomAccessFile rafile) {
        //4.2.3.12 Layer Blending Ranges:?
        // Layer blending ranges: See See Layer blending ranges data.
        brdata = new BlendingRangesData();
        brdata.read(rafile);
        System.out.println(brdata.toString());
        System.out.println();
        return brdata.getLength();
    }

    private long readMaskOrAdjustmentData(RandomAccessFile rafile) {
        //4.2.3.11 Extra Data ?(Layer mask / adjustment layer data)
        // Layer mask data: See See Layer mask / adjustment layer data for structure. Can be 40 bytes, 24 bytes, or 4 bytes if no layer mask
        moadata = new MaskOrAdjustmentData();
        moadata.read(rafile);
        System.out.println(moadata.toString());
        System.out.println();
        return moadata.getLength();
    }

    private void readExtraDataArray(RandomAccessFile rafile, FileHeader fheader, long length) throws IOException {
        long length_moadata = readMaskOrAdjustmentData(rafile);

        long length_brdata = readLayerBlendingRangesData(rafile);

        int length_name = readName(rafile);

        long length_ = length - length_moadata - length_brdata - length_name;
        long length_alinfo = readAdditionalLayerInfo(rafile, fheader, length_);

        long space = length_ - length_alinfo;
        System.out.println("layer records alinfo read space: " + space);
        if (space > 0) {
            rafile.skipBytes((int) space);
        }
    }

    private long readAdditionalLayerInfo(RandomAccessFile rafile, FileHeader fheader, long length) throws IOException {
        long location = rafile.getFilePointer();
        // Additional Layer Information:?
        // (Photoshop 4.0 and later)
        // Series of tagged blocks containing various types of data. See See Additional Layer Information for the list of the types of data that can be included here.
        arrayList_AdditionalLayerInfo = new ArrayList<AdditionalLayerInfo>();

        String signature;
        long offset = 0;
        byte[] arr;
        while (offset < length) {
            arr = new byte[4];
            rafile.read(arr);
            signature = new String(arr);
            rafile.seek(rafile.getFilePointer() - 4);
            if (
                    signature.equalsIgnoreCase(AdditionalLayerInfo.Signature_8BIM) ||
                            signature.equalsIgnoreCase(AdditionalLayerInfo.Signature_8B64)
            ) {
                AdditionalLayerInfo alinfo = new AdditionalLayerInfo();
                alinfo.read(rafile, fheader);
                String key = alinfo.getKey();
                if (null != key) {
                    arrayList_AdditionalLayerInfo.add(alinfo);
                    readAdditionLayerInfo(alinfo.getData(), key);
                    //                    System.out.println("leyertype name: " + alinfo.getLayerTypeName());
                    //                    System.out.println("sub layertype name: " + alinfo.getSubLayerTypeName());
                    //                    setNameBytes(alinfo.getNameBytes(), alinfo.getCharset());
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
        //readAdditionalInfoArray();
        return rafile.getFilePointer() - location;
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

    private void readExtraData(RandomAccessFile rafile, FileHeader fheader, int length) throws IOException {
        readExtraDataArray(rafile, fheader, length);
        //        if (length_ExtraData > 0) {
        //            byte[] arr=new byte[length_ExtraData];
        //            readExtraDataArray(arr, fheader);
        //        }
    }

    private int readExtraDataLength(RandomAccessFile rafile) throws IOException {
        //4.2.3.10 Extra Data Length:4
        // Length of the extra data field ( = the total length of the next five fields).
        int length = rafile.readInt();
        length_ += 4;
        return length;
    }

    private void readFiller(RandomAccessFile rafile) throws IOException {
        //4.2.3.9 Filler:1
        // Filler (zero)
        filler = rafile.readByte();
        length_ += 1;
        if (filler != 0) {
            filler = 0;
        }
    }

    private void readFlags(RandomAccessFile rafile) throws IOException {
        //4.2.3.8 Flags:1
        // Flags:
        // bit 0 = transparency protected;
        // bit 1 = visible;
        // bit 2 = obsolete;
        // bit 3 = 1 for Photoshop 5.0 and later, tells if bit 4 has useful information;
        // bit 4 = pixel data irrelevant to appearance of document
        flags = rafile.readByte();
        length_ += 1;
        is_Transparency_Protected = (flags % 2) != 0;
        is_Visible = ((flags >> 1) % 2) != 0;
        is_Obsolete = ((flags >> 2) % 2) == 0;
        is_Pixel_Data_Useful = ((flags >> 3) % 2) != 0;
        if (is_Pixel_Data_Useful) {
            is_Pixel_Data_Irrelevant = ((flags >> 4) % 2) != 0;
        }
    }

    private void readClipping(RandomAccessFile rafile) throws IOException {
        //4.2.3.7 Clipping:1
        // Clipping: 0 = base, 1 = non-base
        clipping = rafile.readByte();
        length_ += 1;
    }

    private void readOpacity(RandomAccessFile rafile) throws IOException {
        //4.2.3.6 Opacity:1
        // Opacity. 0 = transparent ... 255 = opaque
        opacity = rafile.readByte() & 0xFF;
        length_ += 1;
    }

    private void readBlendModeKey(RandomAccessFile rafile) throws IOException {
        //4.2.3.5 Blend Mode:4
        // Blend mode key:
        // 'pass' = pass through, 'norm' = normal, 'diss' = dissolve, 'dark' = darken, 'mul ' = multiply, 'idiv' = color burn, 'lbrn' = linear burn, 'dkCl' = darker color, 'lite' = lighten, 'scrn' = screen, 'div ' = color dodge, 'lddg' = linear dodge, 'lgCl' = lighter color, 'over' = overlay, 'sLit' = soft light, 'hLit' = hard light, 'vLit' = vivid light, 'lLit' = linear light, 'pLit' = pin light, 'hMix' = hard mix, 'diff' = difference, 'smud' = exclusion, 'fsub' = subtract, 'fdiv' = divide 'hue ' = hue, 'sat ' = saturation, 'colr' = color, 'lum ' = luminosity,
        byte[] arr = new byte[4];
        rafile.read(arr);
        length_ += 4;
        key_BlendMode = new String(arr);
    }

    private void readBlendModeSignature(RandomAccessFile rafile) throws IOException {
        //4.2.3.4 Blend Mode Signature:4
        // Blend mode signature: '8BIM
        byte[] arr = new byte[4];
        rafile.read(arr);
        length_ += 4;
        signature = new String(arr);
        if (!signature.equalsIgnoreCase(Signature_8BIM)) {
            throw new IOException("BlendMode signature is wrong.");
        }
    }

    private void readChannelInfo(RandomAccessFile rafile, FileHeader fheader) throws IOException {
        int channels = readChannels(rafile);
        long location = rafile.getFilePointer();
        arrayList_ChannelInfo = new ArrayList<ChannelInfo>();
        for (int j = 0; j < channels; j++) {
            ChannelInfo cinfo = new ChannelInfo();
            cinfo.read(rafile, fheader);
            arrayList_ChannelInfo.add(j, cinfo);
//            length_ += cinfo.getLength();
            System.out.println(cinfo.toString());
        }
        length_ += rafile.getFilePointer() - location;
    }

    private int readChannels(RandomAccessFile rafile) throws IOException {
        //4.2.3.2 Channels:2
        // Number of channels in the layer
        int channels = rafile.readShort();
        length_ += 2;
        return channels;
    }

    private void readRectangle(RandomAccessFile rafile) throws IOException {
        //4.2.3.1 Rectangle:4*4
        // Rectangle containing the contents of the layer. Specified as top, left, bottom, right coordinates
        top = rafile.readInt();
        left = rafile.readInt();
        bottom = rafile.readInt();
        right = rafile.readInt();
        length_ += 16;
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
        sbuilder.append("/Layer Name: " + getName("gbk"));
        //        sbuilder.append("/Layer Name: " + getName(getCharset()));
        return sbuilder.toString();
    }
}
