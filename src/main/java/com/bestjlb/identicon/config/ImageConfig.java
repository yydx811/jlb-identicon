package com.bestjlb.identicon.config;

import com.bestjlb.identicon.enums.ImageFormat;

/**
 * Created by yydx811 on 2018/1/5.
 */
public class ImageConfig {

    /**
     * 背景色rgb，红
     */
    private int bgRed;

    /**
     * 背景色rgb，绿
     */
    private int bgGreen;

    /**
     * 背景色rgb，蓝
     */
    private int bgBlue;

    /**
     * 背景色alpha通道，透明度
     */
    private int bgAlpha;

    /**
     * foreground字体色rgb，红
     */
    private int fgRed;

    /**
     * foreground字体色rgb，绿
     */
    private int fgGreen;

    /**
     * foreground字体色rgb，蓝
     */
    private int fgBlue;

    /**
     * foreground字体色alpha通道，透明度
     */
    private int fgAlpha;

    /**
     * 图片宽高，正方形
     */
    private int size;

    /**
     * 边距百分比，(0~1]
     */
    private double marginPercent;

    /**
     * 色调（相），圆盘0-360°每一度代表一个色调（相）。
     */
    private double hue;

    /**
     * 饱和度，与hsb/hsv模式的饱和度有区别
     */
    private double saturation;

    /**
     * 亮度，指的是白的量，与brightness明度有区别，明度是指光量，可以是任何颜色。
     */
    private double lightness;

    /**
     * 图片输出格式
     * @see ImageFormat
     */
    private ImageFormat format;

    private int baseMargin;

    private int cell;

    private int margin;

    /**
     * 默认配置
     */
    public ImageConfig() {
        this(255, 255, 255, 255, 0, 0, 0, 0, 64, 0.08d, 0d, 0.5d, 0.7d, ImageFormat.PNG);
    }

    public ImageConfig(int size) {
        this(255, 255, 255, 255, 0, 0, 0, 0, size, 0.08d, 0d, 0.5d, 0.7d, ImageFormat.PNG);
    }

    public ImageConfig(int size, double marginPercent) {
        this(255, 255, 255, 255, 0, 0, 0, 0, size, marginPercent, 0d, 0.5d, 0.7d, ImageFormat.PNG);
    }

    public ImageConfig(int bgRed, int bgGreen, int bgBlue, int bgAlpha, int fgRed, int fgGreen, int fgBlue, int fgAlpha,
                       int size, double marginPercent, double hue, double saturation, double lightness, ImageFormat format) {
        this.bgRed = bgRed;
        this.bgGreen = bgGreen;
        this.bgBlue = bgBlue;
        this.bgAlpha = bgAlpha;
        this.fgRed = fgRed;
        this.fgGreen = fgGreen;
        this.fgBlue = fgBlue;
        this.fgAlpha = fgAlpha;
        this.size = size;
        this.marginPercent = marginPercent;
        this.hue = hue;
        this.saturation = saturation;
        this.lightness = lightness;
        this.format = format;
        this.baseMargin = (int) Math.floor(size * marginPercent);
        this.cell = (int) Math.floor((size - (baseMargin * 2)) / 5);
        this.margin = (int) Math.floor((size - cell * 5) / 2);
    }

    public int getBgRed() {
        return bgRed;
    }

    public void setBgRed(int bgRed) {
        this.bgRed = bgRed;
    }

    public int getBgGreen() {
        return bgGreen;
    }

    public void setBgGreen(int bgGreen) {
        this.bgGreen = bgGreen;
    }

    public int getBgBlue() {
        return bgBlue;
    }

    public void setBgBlue(int bgBlue) {
        this.bgBlue = bgBlue;
    }

    public int getBgAlpha() {
        return bgAlpha;
    }

    public void setBgAlpha(int bgAlpha) {
        this.bgAlpha = bgAlpha;
    }

    public int getFgRed() {
        return fgRed;
    }

    public void setFgRed(int fgRed) {
        this.fgRed = fgRed;
    }

    public int getFgGreen() {
        return fgGreen;
    }

    public void setFgGreen(int fgGreen) {
        this.fgGreen = fgGreen;
    }

    public int getFgBlue() {
        return fgBlue;
    }

    public void setFgBlue(int fgBlue) {
        this.fgBlue = fgBlue;
    }

    public int getFgAlpha() {
        return fgAlpha;
    }

    public void setFgAlpha(int fgAlpha) {
        this.fgAlpha = fgAlpha;
    }

    public int getSize() {
        return size;
    }

    public double getMarginPercent() {
        return marginPercent;
    }

    public double getHue() {
        return hue;
    }

    public void setHue(double hue) {
        this.hue = hue;
    }

    public double getSaturation() {
        return saturation;
    }

    public void setSaturation(double saturation) {
        this.saturation = saturation;
    }

    public double getLightness() {
        return lightness;
    }

    public void setLightness(double lightness) {
        this.lightness = lightness;
    }

    public ImageFormat getFormat() {
        return format;
    }

    public void setFormat(ImageFormat format) {
        this.format = format;
    }

    public int getBaseMargin() {
        return baseMargin;
    }

    public int getCell() {
        return cell;
    }

    public int getMargin() {
        return margin;
    }

    public boolean isSvg() {
        return ImageFormat.SVG.equals(this.format);
    }

    public boolean emptyFgRGB() {
        return this.fgRed == 0 && this.fgGreen == 0 && this.fgBlue == 0 && this.fgAlpha == 0;
    }
}
