package com.bestjlb.identicon;

import com.bestjlb.identicon.config.DigestConfig;
import com.bestjlb.identicon.config.ImageConfig;
import com.bestjlb.identicon.image.Png;
import com.bestjlb.identicon.utils.ColorUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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

    public String getBase64Png(String... args) {
        if (ArrayUtils.isEmpty(args)) {
            throw new IllegalArgumentException("arguments can't be empty!");
        }
        StringBuilder builder = new StringBuilder();
        Stream.of(args).forEach(builder::append);
        String hash = digestConfig.digestHex(builder.toString());

        int cell = imageConfig.getCell();
        int margin = imageConfig.getMargin();
        int fgRed, fgGreen, fgBlue, fgAlpha;

        if (imageConfig.emptyFgRGB()) {
            double[] rgb = ColorUtils.hsl2rgb((double) Integer.parseInt(hash.substring(hash.length() - 7), 16) / 0xfffffff, 0.5d, 0.7d);
            fgRed = (int) (rgb[0] * 255);
            fgGreen = (int) (rgb[1] * 255);
            fgBlue = (int) (rgb[2] * 255);
            fgAlpha = 255;
        } else {
            fgRed = imageConfig.getFgRed();
            fgGreen = imageConfig.getFgGreen();
            fgBlue = imageConfig.getFgBlue();
            fgAlpha = imageConfig.getFgAlpha();
        }

        Png png = new Png(imageConfig.getSize(), imageConfig.getSize(), 256);
        String bg = png.color(imageConfig.getBgRed(), imageConfig.getBgGreen(), imageConfig.getBgBlue(), imageConfig.getBgAlpha());
        String fg = png.color(fgRed, fgGreen, fgBlue, fgAlpha);

        for (int i = 0; i < 15; ++i) {
            String color = Integer.parseInt(String.valueOf(hash.charAt(i)), 16) % 2 == 0 ? fg : bg;
            if (i < 5) {
                png.rectangle(2 * cell + margin, i * cell + margin, cell, cell, color);
            } else if (i < 10) {
                png.rectangle(1 * cell + margin, (i - 5) * cell + margin, cell, cell, color);
                png.rectangle(3 * cell + margin, (i - 5) * cell + margin, cell, cell, color);
            } else if (i < 15) {
                png.rectangle(0 * cell + margin, (i - 10) * cell + margin, cell, cell, color);
                png.rectangle(4 * cell + margin, (i - 10) * cell + margin, cell, cell, color);
            }
        }
        return png.getBase64();
    }
}
