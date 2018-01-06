package com.bestjlb.identicon.image;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by yydx811 on 2018/1/5.
 */
public class Png implements Image {

    public static final String IHDR = "IHDR";

    public static final String PLTE = "PLTE";

    public static final String TRNS = "tRNS";

    public static final String IDAT = "IDAT";

    public static final String IEND = "IEND";

    private final String fillStr = String.valueOf(new char[]{(char) 8, (char) 3});

    private final String spaceFill = String.valueOf(new char[]{(char) 0});

    private int width;

    private int height;

    private int depth;

    private int pixSize;

    private int dataSize;

    private int ihdrOffset = 0;

    private int ihdrSize = 25;

    private int plteOffset;

    private int plteSize;

    private int trnsOffset;

    private int trnsSize;

    private int idatOffset;

    private int idatSize;

    private int iendOffset;

    private int iendSize = 12;

    private String[] buffer;

    private int pIndex = 0;

    private int[] crc32 = new int[256];

    private Map<Integer, String> palette = new HashMap<>();

    public Png(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;

        this.pixSize = height * (width + 1);
        this.dataSize = (int) (2 + this.pixSize + 5 * Math.floor((0xfffe + this.pixSize) / 0xffff) + 4);

//            this.ihdrOffset = 0;
//            this.ihdrSize = 4 + 4 + 13 + 4;
        this.plteOffset = this.ihdrOffset + this.ihdrSize;
        this.plteSize = 4 + 4 + 3 * depth + 4;
        this.trnsOffset = this.plteOffset + this.plteSize;
        this.trnsSize = 4 + 4 + depth + 4;
        this.idatOffset = this.trnsOffset + this.trnsSize;
        this.idatSize = 4 + 4 + this.dataSize + 4;
        this.iendOffset = this.idatOffset + this.idatSize;
//            this.iendSize = 4 + 4 + 4;
        int bufferSize = this.iendOffset + this.iendSize;

        buffer = new String[bufferSize];
        Arrays.fill(buffer, spaceFill);

        writeBuffer(this.ihdrOffset, byte4(this.ihdrSize - 12), IHDR, byte4(width), byte4(height), fillStr);
        writeBuffer(this.plteOffset, byte4(this.plteSize - 12), PLTE);
        writeBuffer(this.trnsOffset, byte4(this.trnsSize - 12), TRNS);
        writeBuffer(this.idatOffset, byte4(this.idatSize - 12), IDAT);
        writeBuffer(this.iendOffset, byte4(this.iendSize - 12), IEND);

//            int header = ((8 + (7 << 4)) << 8) | (3 << 6);
//            header+= 31 - (header % 31);
//            int header = 30938;
        writeBuffer(this.idatOffset + 8, byte2(30938));

        for (int i = 0; (i << 16) - 1 < this.pixSize; ++i) {
            int size;
            String bits;
            if (i + 0xffff < this.pixSize) {
                size = 0xffff;
                bits = String.valueOf((char) 0);
            } else {
                size = this.pixSize - (i << 16) - i;
                bits = String.valueOf((char) 1);
            }
            writeBuffer(this.idatOffset + 8 + 2 + (i << 16) + (i << 2), bits, byte2lsb(size), byte2lsb(~size));
        }

        for (int i = 0; i < crc32.length; ++i) {
            int c = i;
            for (int j = 0; j < 8; j++) {
                if ((c & 1) == 1) {
                    c = -306674912 ^ ((c >> 1) & 0x7fffffff);
                } else {
                    c = (c >> 1) & 0x7fffffff;
                }
            }
            crc32[i] = c;
        }
    }

    private void writeBuffer(int offset, String... args) {
        for (int i = 0; i < args.length; ++i) {
            for (int j = 0; j < args[i].length(); ++j) {
                this.buffer[offset++] = String.valueOf(args[i].charAt(j));
            }
        }
    }

    private String byte4(int i) {
        return String.valueOf(new char[]{(char) ((i >> 24) & 255), (char) ((i >> 16) & 255), (char) ((i >> 8) & 255), (char) (i & 255)});
    }

    private String byte2(int i) {
        return String.valueOf(new char[]{(char) ((i >> 8) & 255), (char) (i & 255)});
    }

    private String byte2lsb(int i) {
        return String.valueOf(new char[]{(char) (i & 255), (char) ((i >> 8) & 255)});
    }

    private int index(int i, int j) {
        int x = j * (this.width + 1) + i + 1;
        int y = (int) (this.idatOffset + 8 + 2 + 5 * (Math.floor(x / 0xffff) + 1) + x);
        return y;
    }

    private void crc32(int offset, int size) {
        int crc = -1;
        for (int i = 4; i < size - 4; i += 1) {
            crc = this.crc32[(crc ^ this.buffer[offset + i].charAt(0)) & 0xff] ^ ((crc >> 8) & 0x00ffffff);
        }
        writeBuffer(offset + size - 4, byte4(crc ^ -1));
    }

    @Override
    public String getImage() {
        int base = 65521;
        int max = 5552;
        int s1 = 1;
        int s2 = 0;
        int n = max;

        for (int i = 0; i < this.height; ++i) {
            for (int j = -1; j < this.width; ++j) {
                s1 += this.buffer[this.index(j, i)].charAt(0);
                s2 += s1;
                if ((n -= 1) == 0) {
                    s1 %= base;
                    s2 %= base;
                    n = max;
                }
            }
        }
        s1 %= base;
        s2 %= base;
        writeBuffer(this.idatOffset + this.idatSize - 8, byte4((s2 << 16) | s1));

        crc32(this.ihdrOffset, this.ihdrSize);
        crc32(this.plteOffset, this.plteSize);
        crc32(this.trnsOffset, this.trnsSize);
        crc32(this.idatOffset, this.idatSize);
        crc32(this.iendOffset, this.iendSize);

        StringBuilder builder = new StringBuilder(buffer.length);
        Stream.of(this.buffer).forEach(builder::append);
        return "\211PNG\r\n\032\n" + builder.toString();
    }

    @Override
    public String color(int red, int green, int blue, int alpha) {
        String result;
        if (alpha < 0 || alpha > 255) {
            alpha = 255;
        }
        int color = ((alpha << 8 | red) << 8 | green) << 8 | blue;
        if (this.palette.containsKey(color)) {
            result = this.palette.get(color);
        } else {
            if (this.pIndex == this.pixSize) {
                result = " ";
            } else {
                int ndx = this.plteOffset + 8 + 3 * this.pIndex;

                this.buffer[ndx + 0] = String.valueOf((char) red);
                this.buffer[ndx + 1] = String.valueOf((char) green);
                this.buffer[ndx + 2] = String.valueOf((char) blue);
                this.buffer[this.trnsOffset + 8 + this.pIndex] = String.valueOf((char) alpha);

                result = String.valueOf((char)this.pIndex++);
                this.palette.put(color, result);
            }
        }
        return result;
    }

    @Override
    public void rectangle(int x, int y, int width, int height, String color) {
        for (int i = x; i < x + width; ++i) {
            for (int j = y; j < y + height; ++j) {
                this.buffer[index(i, j)] = color;
            }
        }
    }

    @Override
    public String getBase64() {
        String s = getImage();
        String ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        int c1, c2, c3, e1, e2, e3, e4;
        int l = s.length();
        int i = 0;
        StringBuilder builder = new StringBuilder();

        do {
            c1 = s.charAt(i);
            e1 = c1 >> 2;
            c2 = s.charAt(i + 1);
            e2 = ((c1 & 3) << 4) | (c2 >> 4);
            if (i + 2 == l) {
                c3 = 0;
            } else {
                c3 = s.charAt(i + 2);
            }
            if (l < i + 2) {
                e3 = 64;
            } else {
                e3 = ((c2 & 0xf) << 2) | (c3 >> 6);
            }
            if (l < i + 3) {
                e4 = 64;
            } else {
                e4 = c3 & 0x3f;
            }
            builder.append(ch.charAt(e1)).append(ch.charAt(e2)).append(ch.charAt(e3)).append(ch.charAt(e4));
        } while ((i += 3) < l);

        return builder.toString();
    }
}
