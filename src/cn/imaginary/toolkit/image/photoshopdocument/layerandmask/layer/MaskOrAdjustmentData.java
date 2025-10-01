package cn.imaginary.toolkit.image.photoshopdocument.layerandmask.mask;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MaskOrAdjustmentData {

    private int length_Data;
    private int mask_Top;
    private int mask_Left;
    private int mask_Bottom;
    private int mask_Right;
    private int colorDefault;
    private int flags;
    private int maskParameters;
    private int padding;
    private int flags_Real;
    private int user_Mask_Density;
    private int vector_Mask_Density;
    private int maskBackground_Real;
    private int maskEnclosing_Top;
    private int maskEnclosing_Left;
    private int maskEnclosing_Bottom;
    private int maskEnclosing_Right;

    private boolean is_Mask_Position_Relative;
    private boolean is_Mask_Disabled;
    private boolean is_Mask_Blending_Inverted;
    private boolean is_Mask_Rendering_Other_Data;
    private boolean is_Masks_Parameters_Applied;

    private double user_Mask_Feather;
    private double vector_Mask_Feather;

    private long length_;

    public MaskOrAdjustmentData() {}

    public int getMaskTop() {
        return mask_Top;
    }

    public int getMaskLeft() {
        return mask_Left;
    }

    public int getMaskBottom() {
        return mask_Bottom;
    }

    public int getMaskRight() {
        return mask_Right;
    }

    public int getDefaultColor() {
        return colorDefault;
    }

    public int getFlags() {
        return flags;
    }

    public boolean isMaskPositionRelative() {
        return is_Mask_Position_Relative;
    }

    public boolean isMaskDisabled() {
        return is_Mask_Disabled;
    }

    public boolean isMaskBlendingInverted() {
        return is_Mask_Blending_Inverted;
    }

    public boolean isMaskRenderingOtherData() {
        return is_Mask_Rendering_Other_Data;
    }

    public boolean isMasksParametersApplied() {
        return is_Masks_Parameters_Applied;
    }

    public int getMaskParameters() {
        return maskParameters;
    }

    public int getPadding() {
        return padding;
    }

    public int getRealFlags() {
        return flags_Real;
    }

    public int getUserMaskDensity() {
        return user_Mask_Density;
    }

    public double getUserMaskFeather() {
        return user_Mask_Feather;
    }

    public int getVectorMaskDensity() {
        return vector_Mask_Density;
    }

    public double getVectorMaskFeather() {
        return vector_Mask_Feather;
    }

    public int getRealMaskBackground() {
        return maskBackground_Real;
    }

    public int getMaskEnclosingTop() {
        return maskEnclosing_Top;
    }

    public int getMaskEnclosingLeft() {
        return maskEnclosing_Left;
    }

    public int getMaskEnclosingBottom() {
        return maskEnclosing_Bottom;
    }

    public int getMaskEnclosingRight() {
        return maskEnclosing_Right;
    }

    public long getLength() {
        return length_;
    }

    public void read(RandomAccessFile rafile) {
        try {
            long location = rafile.getFilePointer();

            readDataLength(rafile);
            readData(rafile);

            length_ += length_Data;
            //            System.out.println("mask or adjustment data space: " + (location + length_ - rafile.getFilePointer()));

            rafile.seek(location + getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readDataArray(RandomAccessFile rafile) throws IOException {
        readRectangle(rafile);
        readDefaultColor(rafile);
        readFlags(rafile);
    }

    private void readFlags(RandomAccessFile rafile) throws IOException {
        // Flags.:1
        // bit 0 = position relative to layer
        // bit 1 = layer mask disabled
        // bit 2 = invert layer mask when blending (Obsolete)
        // bit 3 = indicates that the user mask actually came from rendering other data
        // bit 4 = indicates that the user and/or vector masks have parameters applied to them
        flags = rafile.readByte();
        is_Mask_Position_Relative = (flags % 2) != 0;
        is_Mask_Disabled = ((flags >> 1) % 2) != 0;
        is_Mask_Blending_Inverted = ((flags >> 2) % 2) != 0;
        is_Mask_Rendering_Other_Data = ((flags >> 3) % 2) != 0;
        is_Masks_Parameters_Applied = ((flags >> 4) % 2) != 0;

        if (is_Masks_Parameters_Applied) {
            // Mask Parameters. Only present if bit 4 of Flags set above.:1
            maskParameters = rafile.readByte();

            // Mask Parameters bit flags present as follows:?

            // bit 0 = user mask density, 1 byte
            // bit 1 = user mask feather, 8 byte, double
            // bit 2 = vector mask density, 1 byte
            // bit 3 = vector mask feather, 8 bytes, double
            if ((flags % 2) != 0) {
                user_Mask_Density = rafile.readByte();
            }
            if (((flags >> 1) % 2) != 0) {
                user_Mask_Feather = rafile.readDouble();
            }
            if (((flags >> 2) % 2) != 0) {
                vector_Mask_Density = rafile.readByte();
            }
            if (((flags >> 3) % 2) != 0) {
                vector_Mask_Feather = rafile.readDouble();
            }
            // Padding. Only present if size = 20. Otherwise the following is present:2
            if (length_Data == 20) {
                padding = rafile.readShort();
            } else {
                // Real Flags. Same as Flags information above.:1
                flags_Real = rafile.readByte();

                // Real user mask background. 0 or 255.:1
                maskBackground_Real = rafile.readByte() & 0xFF;

                // Rectangle enclosing layer mask: Top, left, bottom, right.:4 * 4
                maskEnclosing_Top = rafile.readInt();
                maskEnclosing_Left = rafile.readInt();
                maskEnclosing_Bottom = rafile.readInt();
                maskEnclosing_Right = rafile.readInt();
            }
        }
    }

    private void readDefaultColor(RandomAccessFile rafile) throws IOException {
        // Default color. 0 or 255:1
        colorDefault = rafile.readByte() & 0xff;
    }

    private void readRectangle(RandomAccessFile rafile) throws IOException {
        // Rectangle enclosing layer mask: Top, left, bottom, right:4 * 4
        mask_Top = rafile.readInt();
        mask_Left = rafile.readInt();
        mask_Bottom = rafile.readInt();
        mask_Right = rafile.readInt();
    }

    private void readData(RandomAccessFile rafile) throws IOException {
        if (length_Data > 0) {
            readDataArray(rafile);
        }
    }

    private void readDataLength(RandomAccessFile rafile) throws IOException {
        // Size of the data: Check the size and flags to determine what is or is not present. If zero, the following fields are not present:4
        length_Data = rafile.readInt();
        length_ += 4;
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Layer Mask Or Adjustment Layer Data Length: " + getLength());
        sbuilder.append("/mask_Top: " + mask_Top);
        sbuilder.append("/mask_Left: " + mask_Left);
        sbuilder.append("/mask_Bottom: " + mask_Bottom);
        sbuilder.append("/mask_Right: " + mask_Right);
        sbuilder.append("/colorDefault: " + colorDefault);
        sbuilder.append("/flags: " + flags);
        sbuilder.append("/is_Mask_Position_Relative: " + is_Mask_Position_Relative);
        sbuilder.append("/is_Mask_Disabled: " + is_Mask_Disabled);
        sbuilder.append("/is_Mask_Blending_Inverted: " + is_Mask_Blending_Inverted);
        sbuilder.append("/is_Mask_Rendering_Other_Data: " + is_Mask_Rendering_Other_Data);
        sbuilder.append("/is_Masks_Parameters_Applied: " + is_Masks_Parameters_Applied);
        sbuilder.append("/maskParameters: " + maskParameters);
        sbuilder.append("/padding: " + padding);
        sbuilder.append("/flags_Real: " + flags_Real);
        sbuilder.append("/User_Mask_Density: " + user_Mask_Density);
        sbuilder.append("/User_Mask_Feather: " + user_Mask_Feather);
        sbuilder.append("/Vector_Mask_Density: " + vector_Mask_Density);
        sbuilder.append("/Vector_Mask_Feather: " + vector_Mask_Feather);
        sbuilder.append("/maskBackground_Real: " + maskBackground_Real);
        sbuilder.append("/maskEnclosing_Left: " + maskEnclosing_Left);
        sbuilder.append("/maskEnclosing_Top: " + maskEnclosing_Top);
        sbuilder.append("/maskEnclosing_Bottom: " + maskEnclosing_Bottom);
        sbuilder.append("/maskEnclosing_Right: " + maskEnclosing_Right);
        return sbuilder.toString();
    }
}
