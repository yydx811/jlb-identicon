package com.bestjlb.identicon;

import com.bestjlb.identicon.config.DigestConfig;
import com.bestjlb.identicon.config.ImageConfig;
import com.bestjlb.identicon.enums.ImageFormat;
import com.bestjlb.identicon.image.Image;
import com.bestjlb.identicon.image.Png;
import com.bestjlb.identicon.image.Svg;
import com.bestjlb.identicon.utils.ColorUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.stream.Stream;

/**
 * Created by yydx811 on 2018/1/5.
 */
public class Identicon {

    private final ImageConfig imageConfig;

    private final DigestConfig digestConfig;

    public Identicon(ImageConfig imageConfig, DigestConfig digestConfig) {
        this.imageConfig = imageConfig;
        this.digestConfig = digestConfig;
    }

    public String getBase64Image(ImageFormat format, String... args) {
        if (ArrayUtils.isEmpty(args)) {
            throw new IllegalArgumentException("arguments can't be empty!");
        }
        StringBuilder builder = new StringBuilder();
        Stream.of(args).forEach(builder::append);
        if (builder.length() == 0) {
            throw new IllegalArgumentException("arguments can't be empty!");
        }
        String hash = digestConfig.digestHex(builder.toString());

        int cell = imageConfig.getCell();
        int margin = imageConfig.getMargin();
        int fgRed, fgGreen, fgBlue, fgAlpha;

        if (imageConfig.emptyFgRGB()) {
            int[] rgb = ColorUtils.hsl2rgb((double) Integer.parseInt(hash.substring(hash.length() - 7), 16) / 0xfffffff, 0.5d, 0.7d);
            fgRed = rgb[0];
            fgGreen = rgb[1];
            fgBlue = rgb[2];
            fgAlpha = 255;
        } else {
            fgRed = imageConfig.getFgRed();
            fgGreen = imageConfig.getFgGreen();
            fgBlue = imageConfig.getFgBlue();
            fgAlpha = imageConfig.getFgAlpha();
        }

        Image image;
        if (format == null) {
            format = imageConfig.getFormat();
        }
        switch (format) {
            case PNG:
                image = new Png(imageConfig.getSize(), imageConfig.getSize(), 256);
                break;
            case SVG:
                image = new Svg(imageConfig.getSize(), imageConfig.getBgRed(), imageConfig.getBgGreen(), imageConfig.getBgBlue(),
                    imageConfig.getBgAlpha(), fgRed, fgGreen, fgBlue, fgAlpha);
                break;
            default:
                throw new RuntimeException("image format not found!");
        }

        String bg = image.color(imageConfig.getBgRed(), imageConfig.getBgGreen(), imageConfig.getBgBlue(), imageConfig.getBgAlpha());
        String fg = image.color(fgRed, fgGreen, fgBlue, fgAlpha);

        for (int i = 0; i < 15; ++i) {
            String color = Integer.parseInt(String.valueOf(hash.charAt(i)), 16) % 2 == 0 ? fg : bg;
            if (i < 5) {
                image.rectangle(2 * cell + margin, i * cell + margin, cell, cell, color);
            } else if (i < 10) {
                image.rectangle(1 * cell + margin, (i - 5) * cell + margin, cell, cell, color);
                image.rectangle(3 * cell + margin, (i - 5) * cell + margin, cell, cell, color);
            } else if (i < 15) {
                image.rectangle(0 * cell + margin, (i - 10) * cell + margin, cell, cell, color);
                image.rectangle(4 * cell + margin, (i - 10) * cell + margin, cell, cell, color);
            }
        }
        return image.getBase64();
    }

    public String getBase64Png(String... args) {
        return getBase64Image(ImageFormat.PNG, args);
    }

    public String getBase64Svg(String... args) {
        return getBase64Image(ImageFormat.SVG, args);
    }
}
