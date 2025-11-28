# PsdSDK
Psd SDK for Java

[Adobe Photoshop File Formats Specification](https://www.adobe.com/devnet-apps/photoshop/fileformatashtml/)

## 说明（About）：
### 相关信息：
#### 可获取的文件头信息（<u>FileHeader</u>）：
- version, channels, height, width, depth, color_mode.

#### 可获取的图层信息（<u>LayerInfo</u>）：
- <u>Base</u> :
    - left (x), right, top (y), bottom.
    - width, height.
    - channels.
    - opacity.
    - etc.
- <u>Mask/AdjustmentData</u> :
    - left, right, etc.
- <u>LayerType</u> :
    - file, folder open, folder closed, hidden.
- <u>LayerName</u> :
    - byte[] :
        - new String(nameBytes, Charset.forName(charsetName));

#### 可获取的全局图层蒙版信息（<u>GlobalLayerMaskInfo</u>）：
- <u>GlobalMask</u> :
    - red, green, blue, alpha, opacity, kind.

#### 解析后获取的图像像素数据结构（<u>Pixels</u>）：
- byte[channels][height][width]
    - <u>channels</u> (3 - 6) :
        - id = 0 : red
        - id = 1 : green
        - id = 2 : blue
        - id = 3 : alpha (-1)
        - id = 4 : mask (-2)
        - id = 5 : real mask (-3)

## 项目使用方法（Usage）：
- _PsdSDK_ :

将<u>PsdSDK.jar</u>加入项目依赖包。

- _PsdUtils_ :
```java
import cn.imaginary.toolkit.image.PsdUtils;
    ...
    PsdUtils psdUtils = new PsdUtils();
    String psdFilePath = "xx.psd";
    File psdFile = new File(psdFilePath);
    psdUtils.read(psdFile);
```
- _Layers_ :
```java
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.LayerRecords;
    ...
    ArrayList<LayerRecords> arrayList = psdUtils.getLayers();
    LayerRecords layerRecords = arrayList.get(0);
```
- _pixels_ :
```java
    byte[][][] pixels = layerRecords.getImageData();
    File dirFile = psdFile.getParentFile();
    String name = layerRecords.getName(layerRecords.getCharset());
    name += ".png";
    exportImage(new File(dirFile, name), pixels, layerRecords.getWidth(), layerRecords.getHeight());
```
- _ImageData_ :
```java
    ...
    name = psdFile.getName();
    name += ".png";
    exportImage(new File(dirFile, name), imageData.getImageData(), fileHeader.getWidth(), fileHeader.getHeight());
```
- _BufferedImage_ :
```java
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    DataBufferInt dataBufferInt = ((DataBufferInt) bimage.getRaster().getDataBuffer());
    ...
    for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
            alpha = 0xff;
            if (channels > 3) {
                alpha = arrays[3][j][i] & 0xff;
            }
            red = arrays[0][j][i] & 0xff;
            ...
            rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
            dataBufferInt.setElem(j * width + i, rgb);
        }
    }
    ImageIO.write(bufferedImage, "png", pngFile);
```

## 许可（License）：

------------------

License :
 [Apache License (Version 2.0)](http://www.apache.org/licenses/)

------------------

