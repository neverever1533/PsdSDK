package cn.imaginary.toolkit.image.photoshopdocument.layerandmask;

import cn.imaginary.toolkit.image.photoshopdocument.FileHeader;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.additional.SectionDvider;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class AdditionalLayerInfo {

    public static String Signature_8BIM = "8BIM";
    public static String Signature_8B64 = "8B64";

    public static String[] arr_Key = {
            "lrFX",
            "tySh",
            "luni",
            "lyid",
            "lfx2",
            "Patt",
            "Pat2",
            "Pat3",
            "Anno",
            "clbl",
            "infx",
            "knko",
            "lspf",
            "lclr",
            "fxrp",
            "grdm",
            "lsct",
            "brst",
            "SoCo",
            "PtFl",
            "GdFl",
            "vmsk",
            "vsms",
            "TySh",
            "ffxi",
            "lnsr",
            "shpa",
            "shmd",
            "lyvr",
            "tsly",
            "lmgm",
            "vmgm",
            "brit",
            "mixr",
            "clrL",
            "plLd",
            "lnkD",
            "lnk2",
            "lnk3",
            "phfl",
            "blwh",
            "CgEd",
            "Txt2",
            "vibA",
            "pths",
            "anFX",
            "FMsk",
            "SoLd",
            "vstk",
            "vscg",
            "sn2P",
            "vogk",
            "PxSc",
            "cinf",
            "PxSD",
            "artb",
            "artd",
            "abdd",
            "SoLE",
            "LMsk",
            "expA",
            "FXid",
            "FEid",
    };

    private byte[] arr_Data;
    private byte[] arr_Name;

    private long length_;
    private long length_Data;

    private String key;
    private String signature;

    private Charset charset;

    private int layerType;
    private int layerType_Sub;

    //4.4 Additional Layer Information
    public AdditionalLayerInfo() {
    }


    public void setSignature(String signature) throws IOException {
        if (
                null == signature ||
                        (!signature.equalsIgnoreCase(Signature_8BIM) && !signature.equalsIgnoreCase(Signature_8B64))
        ) {
            throw new IOException("The Signature of the Additional Layer Information is wrong.");
        } else {
            this.signature = signature;
        }
    }

    public static boolean isSupportedKey(String key) {
        if (null != key) {
            for (int i = 0; i < arr_Key.length; i++) {
                if (key.equalsIgnoreCase(arr_Key[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    public byte[] getData() {
        return arr_Data;
    }

    public void setData(byte[] array) {
        arr_Data = array;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) throws IOException {
        if (isSupportedKey(key)) {
            this.key = key;
        } else {
            throw new IOException("The key of the Additional Layer Information is wrong.");
        }
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

    public byte[] getNameBytes() {
        return arr_Name;
    }

    private void setNameBytes(byte[] array) {
        arr_Name = array;
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
            //Signature：4
            //4,Signature: '8BIM' or '8B64'
            byte[] arr = new byte[4];
            dinstream.read(arr);
            length_ += 4;
            setSignature(new String(arr));

            //4.4.2 Key：4
            //Key: a 4-character code (See individual sections)
            /*
    lrFX:Effects Layer info;
    tySh:Type Tool info;
    luni:Unicode layer name;
    lyid:Layer ID;
    lfx2:Object-based effects layer info;
    Patt, Pat2 or Pat3:Patterns;
    Anno:Annotations;
    clbl:Blend clipping elements;
    infx:Blend interior elements;
    knko:Knockout setting;
    lspf:Protected setting;
    lclr:Sheet color setting;
    fxrp:Reference point;
    grdm:Gradient settings;
    lsct:Section divider setting;
    brst:Channel blending restrictions setting;
    SoCo:Solid color sheet setting;
    PtFl:Pattern fill setting;
    GdFl:Gradient fill setting;
    vmsk or vsms:Vector mask setting;
    TySh:Type tool object setting;
    ffxi:Foreign effect ID;
    lnsr:Layer name source setting;
    shpa:Pattern data;
    shmd:Metadata setting;
    lyvr:Layer version;
    tsly:Transparency shapes layer;
    lmgm:Layer mask as global mask;
    vmgm:Vector mask as global mask;
    brit:Brightness and Contrast;
    mixr:Channel Mixer;
    clrL:Color Lookup;
    plLd:Placed Layer;
    lnkD, lnk2 and lnk3:Linked Layer;
    phfl:Photo Filter;
    blwh:Black White;
    CgEd:Content Generator Extra Data;
    Txt2:Text Engine Data;
    vibA:Vibrance;
    pths:Unicode Path Name;
    anFX:Animation Effects;
    FMsk:Filter Mask;
    SoLd:Placed Layer Data;
    vstk:Vector Stroke Data;
    vscg:Vector Stroke Content Data;
    sn2P:Using Aligned Rendering;
    vogk:Vector Origination Data;
    PxSc:Pixel Source Data;
    cinf:Compositor Used;
    PxSD;Pixel Source Data;
    artb or artd or abdd:Artboard Data;
    SoLE:Smart Object Layer Data;
    LMsk:User Mask;
    expA:Exposure;
    FXid or FEid:Filter Effects;
    */
            arr = new byte[4];
            dinstream.read(arr);
            length_ += 4;
            setKey(new String(arr));

            readDataLength(dinstream, fheader);
            readData(dinstream, getDataLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readDataLength(DataInputStream dinstream, FileHeader fheader) throws IOException {
        //4.4.3 Data below Length：4
        //Length data below, rounded up to an even byte count.
        // (**PSB**, the following keys have a length count of 8 bytes: LMsk, Lr16, Lr32, Layr, Mt16, Mt32, Mtrn, Alph, FMsk, lnk2, FEid, FXid, PxSD.
        long length;
        if (fheader.isFilePsb()) {
            length = dinstream.readLong();
            length_ += 8;
        } else {
            length = dinstream.readInt();
            length_ += 4;
        }
        if (length % 2 != 0) {
            length++;
        }
        setDataLength(length);
        setLength(length_ + getDataLength());
    }

    private void readData(DataInputStream dinstream, long length) throws IOException {
        //4.4.4 Data：?
        // Data (See individual sections)
        if (length > 0) {
            byte[] arr = new byte[(int) length];
            dinstream.read(arr);
            setData(arr);
            readDataArray(arr, getKey());
        }
    }

    private void readDataArray(byte[] array, String key) {
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Additional Layer Information Length: " + getLength());
        sbuilder.append("/signature: " + signature);
        sbuilder.append("/key: " + key);
        sbuilder.append("/length_Data: " + length_Data);
        return sbuilder.toString();
    }
}
