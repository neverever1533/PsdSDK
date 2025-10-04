package cn.imaginary.toolkit.image.photoshopdocument.layerandmask.additional;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class SectionDvider {

    public static int LayerType_Sub_Normal = 0;
    public static int LayerType_Sub_Scene_Group = 1;
    public static int LayerType_File = 0;
    public static int LayerType_Folder_Open = 1;
    public static int LayerType_Folder_Closed = 2;
    public static int LayerType_Hidden = 3;

    private String key_BlendMode;

    public static String[] arr_LayerType = {"layer_File", "layer_Folder_Open", "layer_Folder_Closed", "layer_Hidden"};
    public static String[] arr_LayerType_Sub = {"sub_Normal", "sub_Scene_Group"};

    public static String Key = "lsct";
    public static String Signature_8BIM = "8BIM";

    private int layerType_Sub;
    private int layerType;
    private int length_Data;

    private long length_;

    public SectionDvider() {
    }

    public int getLayerType() {
        return layerType;
    }

    public long getLength() {
        return length_;
    }

    public void setLayerType(int type) throws IOException {
        switch (type) {
            case 0:
                layerType = LayerType_File;
                break;
            case 1:
                layerType = LayerType_Folder_Open;
                break;
            case 2:
                layerType = LayerType_Folder_Closed;
                break;
            case 3:
                layerType = LayerType_Hidden;
                break;
            default:
                throw new IOException("Unknown Layer Type");
        }
    }

    public void setSubLayerType(int subType) throws IOException {
        switch (subType) {
            case 0:
                layerType_Sub = LayerType_Sub_Normal;
                break;
            case 1:
                layerType_Sub = LayerType_Sub_Scene_Group;
                break;
            default:
                throw new IOException("Unknown Layer Sub Type");
        }
    }

    public int getSubLayerType() {
        return layerType_Sub;
    }

    public String getLayerTypeName() {
        return getLayerTypeName(layerType);
    }

    public static String getLayerTypeName(int type) {
        return arr_LayerType[type];
    }

    public String getSubLayerTypeName() {
        return getSubLayerTypeName(layerType_Sub);
    }

    public static String getSubLayerTypeName(int subType) {
        return arr_LayerType_Sub[subType];
    }

    public void read(byte[] array, String key) {
        try {
            // Section divider setting (Photoshop 6.0)
            // Key is 'lsct' . Data is as follows:
            // Section Divider setting
            readKey(key);
            readDataLength(array);
            readData(array);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readKey(String key) throws IOException {
        if (null == key || !key.equalsIgnoreCase(Key)) {
            throw new IOException("Section dvider setting key is wrong.");
        }
    }

    public void readDataLength(byte[] array) {
        length_Data = array.length;
    }

    public void readData(byte[] array) throws IOException {
        try {
            DataInputStream dinstream = new DataInputStream(new ByteArrayInputStream(array));
            // 4,Type. 4 possible values, 0 = any other type of layer, 1 = open "folder", 2 = closed "folder", 3 = bounding section divider, hidden in the UI
            int type = dinstream.readInt();
            length_ += 4;
            setLayerType(type);

            int length = length_Data;
            if (length >= 12) {
                // Following is only present if length >= 12
                // 4,Signature: '8BIM'
                byte[] arr = new byte[4];
                dinstream.read(arr);
                length_ += 4;
                String signature = new String(arr);
                if (!signature.equalsIgnoreCase(Signature_8BIM)) {
                    throw new IOException("Signature is Wrong!");
                }

                // 4,Key. See blend mode keys in See Layer records.
                arr = new byte[4];
                dinstream.read(arr);
                length_ += 4;
                key_BlendMode = new String(arr);

                if (length >= 16) {
                    // Following is only present if length >= 16
                    // 4,Sub type. 0 = normal, 1 = scene group, affects the animation timeline.
                    setSubLayerType(dinstream.readInt());
                    length_ += 4;
                }
            }
            dinstream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Section Dvider setting length: " + length_);
        sbuilder.append("/key: " + Key);
        sbuilder.append("/layer type: " + layerType);
        sbuilder.append("/layer type name: " + getLayerTypeName());
        sbuilder.append("/blend mode key: " + key_BlendMode);
        sbuilder.append("/sub layer type: " + layerType_Sub);
        sbuilder.append("/sub layer type name: " + getSubLayerTypeName());
        return sbuilder.toString();
    }
}
