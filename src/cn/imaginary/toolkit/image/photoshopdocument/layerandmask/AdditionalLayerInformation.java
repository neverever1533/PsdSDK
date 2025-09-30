package cn.imaginary.toolkit.image.photoshopdocument.layerandmask;

import cn.imaginary.toolkit.image.photoshopdocument.FileHeader;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.additional.SectionDvider;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.additional.UnicodeLayerName;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Arrays;

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
    private long length_;
    private long length_Data;
    private String key;
    private String signature;

    private byte[] arr_Name;

    private Charset charset;

    private int layerType;

    private int layerType_Sub;

    //4.4 Additional Layer Information
    public AdditionalLayerInfo() {}

    public static boolean isSupportedKey(String key) {
        if (null != key) {
            for (int i = 0; i < arr_Key.length; i++) {
                if (key.equalsIgnoreCase(arr_Key[i])) return true;
            }
        }
        return false;
    }

    public void setData(byte[] array) {
        arr_Data = array;
    }

    public byte[] getData() {
        return arr_Data;
    }

    public String getKey() {
        return key;
    }

    public long getLength() {
        return length_;
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
            //4.4 Additional Layer Information: ?
            long location = rafile.getFilePointer();

            readSignature(rafile);
            readKey(rafile);
            readDataLength(rafile, fheader);
            readData(rafile, key);

            length_ += length_Data;
            //            System.out.println("additional layer info space: " + (location + length_ - rafile.getFilePointer()));
            rafile.seek(location + getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readData(RandomAccessFile rafile, String key) throws IOException {
        //4.4.4 Data：?
        // Data (See individual sections)
        if (length_Data > 0) {
            byte[] arr = new byte[(int) length_Data];
            rafile.read(arr);
            setData(arr);
        }
    }

    private void readDataLength(RandomAccessFile rafile, FileHeader fheader) throws IOException {
        //4.4.3 Data below Length：4
        //Length data below, rounded up to an even byte count.
        // (**PSB**, the following keys have a length count of 8 bytes: LMsk, Lr16, Lr32, Layr, Mt16, Mt32, Mtrn, Alph, FMsk, lnk2, FEid, FXid, PxSD.
        if (fheader.isFilePsb()) {
            length_Data = rafile.readLong();
            length_ += 8;
        } else {
            length_Data = rafile.readInt();
            length_ += 4;
        }
        if (length_Data % 2 != 0) {
            length_Data++;
        }
    }

    private void readKey(RandomAccessFile rafile) throws IOException {
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
        byte[] arr = new byte[4];
        rafile.read(arr);
        length_ += 4;
        String key = new String(arr);
        if (isSupportedKey(key)) {
            this.key = key;
        } else {
            throw new IOException("The key of the Additional Layer Information is wrong.");
        }
    }

    private void readSignature(RandomAccessFile rafile) throws IOException {
        //4.4.1 Signature：4
        // Signature: '8BIM' or '8B64'
        byte[] arr = new byte[4];
        rafile.read(arr);
        length_ += 4;
        signature = new String(arr);
        if (signature.equalsIgnoreCase(Signature_8BIM) || signature.equalsIgnoreCase(Signature_8B64)) {} else {
            throw new IOException("The Signature of the Additional Layer Information is wrong.");
        }
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
