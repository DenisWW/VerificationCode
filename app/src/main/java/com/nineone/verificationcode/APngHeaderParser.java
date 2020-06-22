package com.nineone.verificationcode;

import android.util.Log;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nineone.verificationcode.config.APngConstant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class APngHeaderParser {
    private ByteBuffer rawData;
    private APngHeader header;
    private int apngSequenceExpect = 0;
    private List<APngChunk> chunks = new ArrayList<>();
    public APngHeaderParser setData(@Nullable byte[] data) {
        if (data != null) {
            setData(ByteBuffer.wrap(data));
        } else {
            rawData = null;
//            header.status = AnimDecoder.STATUS_OPEN_ERROR;
        }
        return this;
    }
    public APngHeaderParser setData(@NonNull ByteBuffer data) {
        reset();
        rawData = data.asReadOnlyBuffer();
        rawData.position(0);
        rawData.order(ByteOrder.BIG_ENDIAN);
        return this;
    }

    @NonNull
    public APngHeader parseHeader() {
        if (rawData == null) {
            throw new IllegalStateException("You must call setData() before parseHeader()");
        }
        readHeaderContents();
        return header;
    }

    private void readHeaderContents() {
        if (!checkSignature()) return;
        int length, code;
        boolean done = false;
        while (!done) {
            length = readInt();
            code = readInt();
            switch (code) {
                case APngConstant.IHDR_VALUE:
                    if (length != APngConstant.LENGTH_IHDR) {
//                        header.status = AnimDecoder.STATUS_FORMAT_ERROR;
                        break;
                    }
                    readIHDR();
                    break;
                case APngConstant.acTL_VALUE:
                    if (length != APngConstant.LENGTH_acTL) {
//                        header.status = AnimDecoder.STATUS_FORMAT_ERROR;
                        break;
                    }
                    readAcTL();
                    break;
                case APngConstant.PLTE_VALUE:
                    readPalette(length);
                    break;
                case APngConstant.fcTL_VALUE:
                    if (length != APngConstant.LENGTH_fcTL) {
//                        header.status = AnimDecoder.STATUS_FORMAT_ERROR;
                        break;
                    }
                    readFcTL();
                    break;
                case APngConstant.IDAT_VALUE:
                    readIDAT(length);
                    break;
                case APngConstant.fdAT_VALUE:
                    readFdAT(length);
                    break;
                case APngConstant.IEND_VALUE:
                    header.iendPosition = rawData.position() - APngConstant.CHUNK_TOP_LENGTH;
                    done = true;
                    break;
                case APngConstant.tIME_VALUE:
                case APngConstant.iTXt_VALUE:
                case APngConstant.tEXt_VALUE:
                case APngConstant.zTXt_VALUE:
                    chunks.add(new APngChunk(length + APngConstant.CHUNK_TOP_LENGTH + APngConstant.LENGTH_CRC,
                            rawData.position() - APngConstant.CHUNK_TOP_LENGTH));
                    skip(length);
                    break;
                case APngConstant.gAMA_VALUE:
                case APngConstant.bKGD_VALUE:
                case APngConstant.tRNS_VALUE:
                default:
                    skip(length);
                    break;
            }


//            if (code == APngConstant.IHDR_VALUE) {
//                readIHDR();
//            } else if (code == APngConstant.acTL_VALUE) {
//                readAcTL();
//            } else if (code == APngConstant.PLTE_VALUE) {
//                readPalette(length);
//            } else if (code == APngConstant.fcTL_VALUE) {
//                readFcTL();
//            } else if (code == APngConstant.IDAT_VALUE) {
//                readIDAT(length);
//            } else if (code == APngConstant.fdAT_VALUE) {
//                readFdAT(length);
//            } else if (code == APngConstant.tIME_VALUE || code == APngConstant.iTXt_VALUE || code == APngConstant.tEXt_VALUE || code == APngConstant.zTXt_VALUE) {
//                chunks.add(new APngChunk(length + APngConstant.CHUNK_TOP_LENGTH + APngConstant.LENGTH_CRC, rawData.position() - APngConstant.CHUNK_TOP_LENGTH));
//                skip(length);
//            } else if (code == APngConstant.IEND_VALUE) {
//                header.iendPosition = rawData.position() - APngConstant.CHUNK_TOP_LENGTH;
//                done = true;
//            } else if (code == APngConstant.gAMA_VALUE || code == APngConstant.bKGD_VALUE || code == APngConstant.tRNS_VALUE) {
//                skip(length);
//            } else {
//                skip(length);
//            }
//            Log.e("code", "==" + code + "   length==" + length);
            readCRC();
            if (!done) {
                done = rawData.position() >= rawData.limit();
            }
        }
        if (!chunks.isEmpty()) {
            header.otherChunk = chunks.toArray(new APngChunk[0]);
        }
        if (header.frameCount != header.frames.size()) {
            header.frameCount = header.frames.size();
        }

    }

    private boolean checkSignature() {
        for (int i = 0; i < APngConstant.LENGTH_SIGNATURE; i++) {
            byte b = readByte();
            if (b != APngConstant.BYTES_SIGNATURE[i]) {
                return false;
            }
        }
        return true;
    }

    private void readCRC() {
//        Log.e("readCRC", "=="+readInt());
        readInt();
//        skip(4);
    }

    /**
     * read The Animation Data Chunk
     */
    private void readIDAT(int length) {
        // 有多个idat存在的情况
        if (header.idatFirstPosition != 0) {
            // 已经有idat
            header.idatLastPosition = rawData.position() - APngConstant.CHUNK_TOP_LENGTH;
        } else {
            // 第一个idat
            header.idatFirstPosition = rawData.position() - APngConstant.CHUNK_TOP_LENGTH;
        }
        chunks.clear();
        if (apngSequenceExpect > 0) {
            // fcTL在IDAT之前，当做第一帧
            header.hasFcTL = true;
            header.currentFrame.bufferFrameStart = rawData.position();
            header.currentFrame.length = Math.min(length, rawData.remaining());
        }
        skip(length);
    }

    public void clear() {
        rawData = null;
        header = null;
    }

    /**
     * Animation Control Chunk<br/>
     * num_frames：0~3字节表示该Apng总的播放帧数。<br/>
     * num_plays：4~7字节表示该Apng循环播放的次数。<br/>
     */
    private void readAcTL() {
        header.frameCount = readInt();
        header.loopCount = readInt();
        Log.e("num_frames", "==" + header.frameCount + "    num_plays==" + header.loopCount);
    }

    /**
     * read palette<br/>
     * <a href = "https://www.w3.org/TR/PNG/#11PLTE">palette</a>
     */
    private void readPalette(int length) {
        if (length % 3 != 0) {
            return;
        }
        skip(length);
    }

    /**
     * 宽度	4字节
     * 高度	4字节
     * 位深	1个字节
     * 颜色种类	1个字节
     * 压缩方式	1个字节
     * 过滤方式	1个字节
     * 隔行扫描法	1个字节
     * Image Head Chunk
     * Width	4 bytes<br/>
     * Height	4 bytes<br/>
     * Bit depth	1 byte<br/>
     * Colour type	1 byte<br/>
     * Compression method	1 byte<br/>
     * Filter method	1 byte<br/>
     * Interlace method	1 byte<br/>
     */
    private void readIHDR() {
        header.width = readInt();
        header.height = readInt();
        header.bitDepth = readByte();
        try {
            header.colourType = PngColourType.fromByte(readByte());
        } catch (Exception e) {
            e.printStackTrace();
        }
        header.compressionMethod = readByte();
        header.filterMethod = readByte();
        header.interlaceMethod = readByte();
    }

    /**
     * read fdAT<br/>
     * 0    sequence_number       (unsigned int)   Sequence number of the animation chunk, starting from 0<br/>
     * 4    frame_data            X bytes          Frame data for this frame
     */
    private void readFdAT(int length) {
        int sequence = readInt();
        if (sequence != apngSequenceExpect) {
//            header.status = AnimDecoder.STATUS_FORMAT_ERROR;
            return;
        }
        if (length > rawData.remaining()) {
            // 数据错误(数量不足)删除
            header.frames.remove(header.frames.size() - 1);
        } else {
            apngSequenceExpect++;
            header.currentFrame.bufferFrameStart = rawData.position();
            header.currentFrame.length = length - 4;
            header.currentFrame.isFdAT = true;
        }
        // 减去sequence占用的位置
        skip(length - 4);
    }

    /**
     * read fcTL<br/>
     * 头部数据包含 数据格式
     * byte  description<br/>
     * 0     sequence_number：控制帧的序号，从0开始。<br/>
     * 4     width：帧的宽度。<br/>
     * 8     /height：帧的高度。<br/>
     * 12    x_offset：在x方向的偏移。<br/>
     * 16    y_offset：在y方向的偏移。<br/>
     * 20    delay_num：帧动画时间间隙的分子<br/>
     * 22    delay_den：帧动画时间间隙的分母<br/>
     * 24    dispose_op：在显示该帧之前，需要对前面缓冲输出区域做何种处理。<br/>
     * 25    blend_op：具体显示该帧的方式。<br/>
     */
    private void readFcTL() {
        APngFrame frame = new APngFrame();
        header.currentFrame = frame;
        int sequence = readInt();
        if (sequence != apngSequenceExpect) {
//            header.status = AnimDecoder.STATUS_FORMAT_ERROR;
            return;
        }
        apngSequenceExpect++;
        frame.sequenceNumber = sequence;
        frame.width = readInt();
        frame.height = readInt();
        frame.xOffset = readInt();
        frame.yOffset = readInt();
        //frame.delayNumerator = readUnsignedShort();
        //short delayDenominator = readUnsignedShort();
        //frame.delayDenominator = delayDenominator == 0 ? 100 : delayDenominator; // APNG spec says zero === 100.
        frame.setDelay(readUnsignedShort(), readUnsignedShort());
        frame.setDisposeOp(readByte());
        frame.setBlendOp(readByte());
        if (rawData.position() + APngConstant.LENGTH_CRC < rawData.limit()) {
            header.frames.add(frame);
        }
//        Log.e("frame", "===" + frame.toString());
        /* else {
            // 后面再无数据则不添加
            Log.w("apng data error", "after fcTL have not fdAT!");
        }*/
    }


    private void skip(int length) {
        int newPosition = Math.min(rawData.position() + length, rawData.limit());
        rawData.position(newPosition);
    }

    private int readInt() {
        return rawData.getInt();
    }

    /**
     * Reads next 16-bit value, LSB first.
     */
    private int readShort() {
        // Read 16-bit value.
        return rawData.getShort();
    }

    /**
     * read byte
     *
     * @return byte
     */
    private byte readByte() {
        return rawData.get();
    }


    private void reset() {
        rawData = null;
        header = new APngHeader();
    }

    /**
     * 是否apng 动图
     *
     * @param header header
     * @return is apng
     */
    public static boolean isAPNG(byte[] header) {
        return header[37] == 'a' && header[38] == 'c' && header[39] == 'T' && header[40] == 'L';
    }

    public short readUnsignedShort() {
        return (short) (rawData.getShort() & 0xffff);
    }

    /**
     * PNG images support 5 colour types as defined at http://www.w3.org/TR/PNG/#11IHDR
     */
    public enum PngColourType {
        PNG_GREYSCALE(0, 1, "1, 2, 4, 8, 16", "Greyscale", "Each pixel is a greyscale sample"),
        PNG_TRUECOLOUR(2, 3, "8, 16", "Truecolour", "Each pixel is an R,G,B triple"),
        PNG_INDEXED_COLOUR(3, 1, "1, 2, 4, 8", "Indexed-colour", "Each pixel is a palette index; a PLTE chunk shall appear."),
        PNG_GREYSCALE_WITH_ALPHA(4, 2, "4, 8, 16", "Greyscale with alpha", "Each pixel is a greyscale sample followed by an alpha sample."),
        PNG_TRUECOLOUR_WITH_ALPHA(6, 4, "8, 16", "Truecolour with alpha", "Each pixel is an R,G,B triple followed by an alpha sample.");

        public final int code;
        public final int componentsPerPixel;
        public final String allowedBitDepths;
        public final String name;
        public final String description;

        PngColourType(int code, int componentsPerPixel, String allowedBitDepths, String name, String description) {
            this.code = code;
            this.componentsPerPixel = componentsPerPixel;
            this.allowedBitDepths = allowedBitDepths;
            this.name = name;
            this.description = description;
        }

        public boolean isIndexed() {
            return (code & 0x01) > 0;
        }

        public boolean hasAlpha() {
            return (code & 0x04) > 0;
        }

        public boolean supportsSubByteDepth() {
            return code == 0 || code == 3;
        }

        public static PngColourType fromByte(byte b) throws IllegalArgumentException {
            Log.e("b", "===" + b);
            switch (b) {
                case 0:
                    return PNG_GREYSCALE;
                case 2:
                    return PNG_TRUECOLOUR;
                case 3:
                    return PNG_INDEXED_COLOUR;
                case 4:
                    return PNG_GREYSCALE_WITH_ALPHA;
                case 6:
                    return PNG_TRUECOLOUR_WITH_ALPHA;
                default:
                    throw new IllegalArgumentException(String.format("Valid PNG colour types are 0, 2, 3, 4, 6. Type '%d' is invalid", b));
            }
        }
    }

    public class APngFrame {
        public int sequenceNumber;
        public int width;
        public int height;
        public int xOffset;
        public int yOffset;
        //public short delayNumerator;
        //public short delayDenominator;
        @APngBlendMethod
        public int blendOp;

        public int length;
        public boolean isFdAT;

        public static final int APNG_BLEND_OP_SOURCE = 0;
        public static final int APNG_BLEND_OP_OVER = 1;

        public static final int DISPOSAL_UNSPECIFIED = 0;
        public static final int DISPOSAL_NONE = 1;

        public static final int DISPOSAL_BACKGROUND = 2;
        public static final int DISPOSAL_PREVIOUS = 3;

        @AnimDisposalMethod
        public int dispose;

        public int delay;

        public int bufferFrameStart;

        public void setDisposeOp(byte disposeOp) {
            // todo
            switch (disposeOp) {
                case 0:
                    this.dispose = DISPOSAL_NONE;
                    break;
                case 1:
                    this.dispose = DISPOSAL_BACKGROUND;
                    break;
                case 2:
                    this.dispose = DISPOSAL_PREVIOUS;
                    break;
                default:
                    this.dispose = DISPOSAL_UNSPECIFIED;
                    break;
            }
        }

        public void setBlendOp(byte blendOp) {
            // todo
            switch (blendOp) {
                case 0:
                    this.blendOp = APNG_BLEND_OP_SOURCE;
                    break;
                case 1:
                    this.blendOp = APNG_BLEND_OP_OVER;
                    break;
            }
        }

        public void setDelay(short delayNumerator, short delayDenominator) {
            if (delayDenominator == 1000) {
                delay = delayNumerator;
            } else {
                if (delayDenominator == 0) {
                    delayDenominator = 100;
                }
                // if denom is 100 then need to multiple by 10
                delay = (int) ((delayNumerator * 1000f) / delayDenominator);
            }
        }

        public String toString() {
            return "sequenceNumber:" + sequenceNumber +
                    ",width:" + width +
                    ",height:" + height +
                    ", xOffset:" + xOffset +
                    ",yOffset:" + yOffset +
                    //public short delayNumerator;
                    //public short delayDenominator;
                    ",blendOp:" + blendOp +
                    ",dispose:" + dispose +
                    ",length:" + length +
                    ",isFdAT:" + isFdAT;
        }
    }
    int INITIAL_FRAME_POINTER = -1;
    int BYTES_PER_INTEGER = Integer.SIZE / 8;
    static int STATUS_OK = 0;
    static int STATUS_FORMAT_ERROR = 1;
    static int STATUS_OPEN_ERROR = 2;
    static int STATUS_PARTIAL_DECODE = 3;
    int TOTAL_ITERATION_COUNT_FOREVER = 0;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {APngFrame.DISPOSAL_UNSPECIFIED, APngFrame.DISPOSAL_NONE, APngFrame.DISPOSAL_BACKGROUND, APngFrame.DISPOSAL_PREVIOUS})
    public @interface AnimDisposalMethod {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {APngFrame.APNG_BLEND_OP_SOURCE, APngFrame.APNG_BLEND_OP_OVER})
    public @interface APngBlendMethod {
    }

//    @Retention(RetentionPolicy.SOURCE)
//    @IntDef(value = {STATUS_OK, STATUS_FORMAT_ERROR, STATUS_OPEN_ERROR, STATUS_PARTIAL_DECODE})
//    @interface AnimDecodeStatus {
//    }

    public class APngHeader {
        /**
         itDepth是给定像素的每个<em>通道</ em>的位数。
         *更好的名称可能是“ bitsPerPixelChannel”，但使用的是“ bitDepth”
         *在整个PNG规范中。
         * <p>
         * bitDepth为8的真彩色图像表示像素的红色通道
         *具有8位（因此256级红色），绿色具有8位（256级绿色），并且
         *蓝色具有8位（因此256级绿色）。这意味着PerPixel的总位数
         *该位图将为8 + 8 + 8 = 24。
         * <p>
         * bitDepth为8的带<em> alpha </ em>图像的真彩色图像将是相同的
         *除了每个像素的每个alpha元素将具有8位（因此256级
         * alpha透明度），表示该位图的总bitsPerPixel
         *是8 + 8 + 8 + 8 = 32。
         * <p>
         *具有<em> bitDepth为16 </ em>的alpha图像的真彩色表示每个
         *红色，绿色蓝色和alpha分别具有16位，这意味着总数
         * bitsPerPixel将为16 + 16 + 16 + 16 = 64。
         * <p>
         * bitDepth为16的灰度图像（无Alpha）仅具有用于
         *每个像素，所以bitsPerPixel也将为16。
         * <p>
         *但是bitDepth为16的带有alpha </ em>的灰度图像具有灰色
         *通道和alpha通道，每个通道都有16位，因此bitsPerPixel将为
         * 16 + 16 = 32。
         * <p>
         *对于基于调色板的图像...
         * <ul>
         * <li>单色图像或具有2个调色板的图像的bitDepth = 1。</ li>
         * <li>具有4个调色板的图像的bitDepth = 2。</ li>
         * <li>具有8个调色板的图像的bitDepth = 3。</ li>
         * <li>具有16种调色板的图像的bitDepth = 4。</ li>
         * <li>具有16种灰度<em>和一个alpha通道</ em>的灰度图像
         *具有bitDepth = 4和bitsPerPixel = 8，因为灰色和Alpha通道
         * each have 4 bits.</li>
         * </ul>
         * @see #bitsPerPixel
         */


        public static final int NETSCAPE_LOOP_COUNT_FOREVER = 0;
        public static final int NETSCAPE_LOOP_COUNT_DOES_NOT_EXIST = -1;
        public PngColourType colourType;
        public byte bitDepth;
        /**
         * Every PNG image must be exactly one of the standard types as defined by the
         * PNG specification. Better names might have been "imageType" or "imageFormat"
         * but the name "colourType" is used throughout the PNG spec.
         */

        /**
         * Compression type of the file.
         * In practice this is redundant: it may be zip and nothing else.
         */
        public byte compressionMethod;

        /**
         * Filter method used by the file.
         * In practice this is redundant because the filter types are set in the
         * specification and have never been (and never will be) extended.
         */
        public byte filterMethod;

        /**
         * An image is either interlaced or not interlaced.
         * At the time of writing only non-interlaced is supported by this library.
         */
        public byte interlaceMethod;
        /**
         * The number of bits that comprise a single pixel in this bitmap (or every
         * frame if animated). This is distinct from bitDepth.
         *
         * @see #bitDepth
         */
        public int bitsPerPixel;
        public int bytesPerRow;
        public int filterOffset;
        public APngHeaderParser.APngFrame currentFrame;
        public final List<APngHeaderParser.APngFrame> frames = new ArrayList<>();

        public int idatFirstPosition;
        public int idatLastPosition;
        public boolean hasFcTL;
        public int iendPosition;
        public int loopCount = NETSCAPE_LOOP_COUNT_DOES_NOT_EXIST;
        public int frameCount = 0;
        /**
         * idat之后出现的其他类型的chunk的position数组
         */
        public APngChunk[] otherChunk;
        /**
         * 宽度
         */
        public int width;
        /**
         * 高度
         */
        public int height;

        @NonNull
        @Override
        public String toString() {
            return "width:" + width +
                    ",height:" + height +
                    ",bitDepth:" + bitDepth +
                    ",compressionMethod:" + compressionMethod +
                    ",filterMethod:" + filterMethod +
                    ",interlaceMethod:" + interlaceMethod;
        }
    }
    public static class APngChunk {
        int totalLength; // 总长度，包含top，length，crc
        int positionStart; // 起始点

        public APngChunk() {
        }

        APngChunk(int totalLength, int positionStart) {
            this.totalLength = totalLength;
            this.positionStart = positionStart;
        }
    }

}
