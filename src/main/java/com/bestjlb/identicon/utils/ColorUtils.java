package com.bestjlb.identicon.utils;

/**
 * Created by yydx811 on 2018/1/5.
 */
public class ColorUtils {

    /**
     * hsl转换为rgb。
     * <p>https://gist.github.com/aemkei/1325937
     *
     * @param h
     *          hue，色调。
     * @param s
     *          saturation，对比度。
     * @param l
     *          lightness，亮度。
     * @return
     */
    public static double[] hsl2rgb(double h, double s, double l) {
        h *= 6;

        double[] d = {
                l += s *= l < 0.5d ? l : 1 - l,
                l - h % 1 * s * 2,
                l -= s *= 2,
                l,
                l + h % 1 * s,
                l + s
        };

        return new double[]{
                d[ ~~(int) h % 6 ],
                d[ ((int) h | 16) % 6 ],
                d[ ((int) h | 8)  % 6 ]
        };
    }
}
