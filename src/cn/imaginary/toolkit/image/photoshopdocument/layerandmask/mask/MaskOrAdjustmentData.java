package cn.imaginary.toolkit.image.photoshopdocument.layerandmask.mask;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class MaskOrAdjustmentData {

    private int length_;
    private int length_Data;
    private int mask_Top;
    private int mask_Left;
    private int mask_Bottom;
    private int mask_Right;
    private int color_Default;
    private int flags;
    private int mask_Parameters;
    private int padding;
    private int flags_Real;
    private int user_Mask_Density;
    private int vector_Mask_Density;
    private int mask_Background_Real;
    private int mask_Enclosing_Top;
    private int mask_Enclosing_Left;
    private int mask_Enclosing_Bottom;
    private int mask_Enclosing_Right;

    private boolean is_Mask_Position_Relative;
    private boolean is_Mask_Disabled;
    private boolean is_Mask_Blending_Inverted;
    private boolean is_Mask_Rendering_Other_Data;
    private boolean is_Masks_Parameters_Applied;

    private double user_Mask_Feather;
    private double vector_Mask_Feather;

    private byte[] arr_Data;

    public MaskOrAdjustmentData() {
    }

    public int getMaskTop() {
        return mask_Top;
    }

    public void setMaskTop(int maskTop) {
        mask_Top = maskTop;
    }

    public int getMaskLeft() {
        return mask_Left;
    }

    public void setMaskLeft(int maskLeft) {
        mask_Left = maskLeft;
    }

    public int getMaskBottom() {
        return mask_Bottom;
    }

    public void setMaskBottom(int maskBottom) {
        mask_Bottom = maskBottom;
    }

    public int getMaskRight() {
        return mask_Right;
    }

    public void setMaskRight(int maskRight) {
        mask_Right = maskRight;
    }

    public int getDefaultColor() {
        return color_Default;
    }

    public void setDefaultColor(int color) {
        color_Default = color;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public boolean isMaskPositionRelative() {
        return is_Mask_Position_Relative;
    }

    public void setMaskPositionRelative(boolean isMaskPositionRelative) {
        is_Mask_Position_Relative = isMaskPositionRelative;
    }

    public boolean isMaskDisabled() {
        return is_Mask_Disabled;
    }

    public void setMaskDisabled(boolean isMaskDisabled) {
        is_Mask_Disabled = isMaskDisabled;
    }

    public boolean isMaskBlendingInverted() {
        return is_Mask_Blending_Inverted;
    }

    public void setMaskBlendingInverted(boolean isMaskBlendingInverted) {
        is_Mask_Blending_Inverted = isMaskBlendingInverted;
    }

    public boolean isMaskRenderingOtherData() {
        return is_Mask_Rendering_Other_Data;
    }

    public void setMaskRenderingOtherData(boolean isMaskRenderingOtherData) {
        is_Mask_Rendering_Other_Data = isMaskRenderingOtherData;
    }

    public boolean isMasksParametersApplied() {
        return is_Masks_Parameters_Applied;
    }

    public void setMasksParametersApplied(boolean isMasksParametersApplied) {
        is_Masks_Parameters_Applied = isMasksParametersApplied;
    }

    public int getMaskParameters() {
        return mask_Parameters;
    }

    public void setMaskParameters(int maskParameters) {
        mask_Parameters = maskParameters;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public int getRealFlags() {
        return flags_Real;
    }

    public void setRealFlags(int flags) {
        flags_Real = flags;
    }

    public int getUserMaskDensity() {
        return user_Mask_Density;
    }

    public void setUserMaskDensity(int userMaskDensity) {
        user_Mask_Density = userMaskDensity;
    }

    public double getUserMaskFeather() {
        return user_Mask_Feather;
    }

    public void setUserMaskFeather(double userMaskFeather) {
        user_Mask_Feather = userMaskFeather;
    }

    public int getVectorMaskDensity() {
        return vector_Mask_Density;
    }

    public void setVectorMaskDensity(int vectorMaskDensity) {
        vector_Mask_Density = vectorMaskDensity;
    }

    public double getVectorMaskFeather() {
        return vector_Mask_Feather;
    }

    public void setVectorMaskFeather(double vectorMaskFeather) {
        vector_Mask_Feather = vectorMaskFeather;
    }

    public int getRealMaskBackground() {
        return mask_Background_Real;
    }

    public void setRealMaskBackground(int maskBackgroundReal) {
        mask_Background_Real = maskBackgroundReal;
    }

    public int getMaskEnclosingTop() {
        return mask_Enclosing_Top;
    }

    public void setMaskEnclosingTop(int maskEnclosingTop) {
        mask_Enclosing_Top = maskEnclosingTop;
    }

    public int getMaskEnclosingLeft() {
        return mask_Enclosing_Left;
    }

    public void setMaskEnclosingLeft(int maskEnclosingLeft) {
        mask_Enclosing_Left = maskEnclosingLeft;
    }

    public int getMaskEnclosingBottom() {
        return mask_Enclosing_Bottom;
    }

    public void setMaskEnclosingBottom(int maskEnclosingBottom) {
        mask_Enclosing_Bottom = maskEnclosingBottom;
    }

    public int getMaskEnclosingRight() {
        return mask_Enclosing_Right;
    }

    public void setMaskEnclosingRight(int maskEnclosingRight) {
        mask_Enclosing_Right = maskEnclosingRight;
    }

    public int getLength() {
        return length_;
    }

    public void setLength(int length) {
        length_ = length;
    }

    public int getDataLength() {
        return length_Data;
    }

    public void setDataLength(int length) {
        length_Data = length;
    }

    public byte[] getData() {
        return arr_Data;
    }

    public void setData(byte[] array) {
        arr_Data = array;
    }

    public void read(DataInputStream dinstream) {
        try {
            readDataLength(dinstream);
            readData(dinstream, getDataLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readDataLength(DataInputStream dinstream) throws IOException {
        //Size of the data:4
        //4,Size of the data: Check the size and flags to determine what is or is not present. If zero, the following fields are not present
        int length = dinstream.readInt();
        setDataLength(length);
        setLength(4 + getDataLength());
    }

    private void readData(DataInputStream dinstream, int length) throws IOException {
        if (length > 0) {
            byte[] arr = new byte[length];
            dinstream.read(arr);
            setData(arr);
            readDataArray(arr);
        }
    }

    private void readDataArray(byte[] array) throws IOException {
        try {
            int length = array.length;
            DataInputStream dinstream = new DataInputStream(new ByteArrayInputStream(array));
            // Rectangle enclosing layer mask: Top, left, bottom, right:4 * 4
            setMaskTop(dinstream.readInt());
            setMaskLeft(dinstream.readInt());
            setMaskBottom(dinstream.readInt());
            setMaskRight(dinstream.readInt());

            // Default color. 0 or 255:1
            setDefaultColor(dinstream.readByte() & 0xff);

            // Flags.:1
            // bit 0 = position relative to layer
            // bit 1 = layer mask disabled
            // bit 2 = invert layer mask when blending (Obsolete)
            // bit 3 = indicates that the user mask actually came from rendering other data
            // bit 4 = indicates that the user and/or vector masks have parameters applied to them
            int flags = dinstream.readByte();
            setFlags(flags);
            boolean is_Mask_Position_Relative = (flags % 2) != 0;
            setMaskPositionRelative(is_Mask_Position_Relative);

            boolean is_Mask_Disabled = ((flags >> 1) % 2) != 0;
            setMaskDisabled(is_Mask_Disabled);

            boolean is_Mask_Blending_Inverted = ((flags >> 2) % 2) != 0;
            setMaskBlendingInverted(is_Mask_Blending_Inverted);

            boolean is_Mask_Rendering_Other_Data = ((flags >> 3) % 2) != 0;
            setMaskRenderingOtherData(is_Mask_Rendering_Other_Data);

            boolean is_Masks_Parameters_Applied = ((flags >> 4) % 2) != 0;
            setMasksParametersApplied(is_Masks_Parameters_Applied);

            if (is_Masks_Parameters_Applied) {
                // Mask Parameters. Only present if bit 4 of Flags set above.:1
                setMaskParameters(dinstream.readByte());

                // Mask Parameters bit flags present as follows:?

                // bit 0 = user mask density, 1 byte
                // bit 1 = user mask feather, 8 byte, double
                // bit 2 = vector mask density, 1 byte
                // bit 3 = vector mask feather, 8 bytes, double
                if ((flags % 2) != 0) {
                    setUserMaskDensity(dinstream.readByte());
                }
                if (((flags >> 1) % 2) != 0) {
                    setUserMaskFeather(dinstream.readDouble());
                }
                if (((flags >> 2) % 2) != 0) {
                    setVectorMaskDensity(dinstream.readByte());
                }
                if (((flags >> 3) % 2) != 0) {
                    setVectorMaskFeather(dinstream.readDouble());
                }

                // Padding. Only present if size = 20. Otherwise the following is present:2
                if (length == 20) {
                    setPadding(dinstream.readShort());
                } else {
                    // Real Flags. Same as Flags information above.:1
                    setRealFlags(dinstream.readByte());

                    // Real user mask background. 0 or 255.:1
                    setRealMaskBackground(dinstream.readByte() & 0xFF);

                    // Rectangle enclosing layer mask: Top, left, bottom, right.:4 * 4
                    setMaskEnclosingTop(dinstream.readInt());
                    setMaskEnclosingLeft(dinstream.readInt());
                    setMaskEnclosingBottom(dinstream.readInt());
                    setMaskEnclosingRight(dinstream.readInt());
                }
            }
            dinstream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Layer Mask Or Adjustment Layer Data Length: " + getLength());
        sbuilder.append("/mask_Top: " + mask_Top);
        sbuilder.append("/mask_Left: " + mask_Left);
        sbuilder.append("/mask_Bottom: " + mask_Bottom);
        sbuilder.append("/mask_Right: " + mask_Right);
        sbuilder.append("/colorDefault: " + color_Default);
        sbuilder.append("/flags: " + flags);
        sbuilder.append("/is_Mask_Position_Relative: " + is_Mask_Position_Relative);
        sbuilder.append("/is_Mask_Disabled: " + is_Mask_Disabled);
        sbuilder.append("/is_Mask_Blending_Inverted: " + is_Mask_Blending_Inverted);
        sbuilder.append("/is_Mask_Rendering_Other_Data: " + is_Mask_Rendering_Other_Data);
        sbuilder.append("/is_Masks_Parameters_Applied: " + is_Masks_Parameters_Applied);
        sbuilder.append("/maskParameters: " + mask_Parameters);
        sbuilder.append("/padding: " + padding);
        sbuilder.append("/flags_Real: " + flags_Real);
        sbuilder.append("/User_Mask_Density: " + user_Mask_Density);
        sbuilder.append("/User_Mask_Feather: " + user_Mask_Feather);
        sbuilder.append("/Vector_Mask_Density: " + vector_Mask_Density);
        sbuilder.append("/Vector_Mask_Feather: " + vector_Mask_Feather);
        sbuilder.append("/maskBackground_Real: " + mask_Background_Real);
        sbuilder.append("/maskEnclosing_Left: " + mask_Enclosing_Left);
        sbuilder.append("/maskEnclosing_Top: " + mask_Enclosing_Top);
        sbuilder.append("/maskEnclosing_Bottom: " + mask_Enclosing_Bottom);
        sbuilder.append("/maskEnclosing_Right: " + mask_Enclosing_Right);
        return sbuilder.toString();
    }
}
