package com.bestjlb.identicon.image;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Created by yydx811 on 2018/1/6.
 */
public class Svg implements Image {

    private double strokePercent = 0.005d;

    private int size;

    private String bg;

    private String fg;

    private double stroke;

    private List<Rectangle> rectangleList;

    public Svg(int size, int bgRed, int bgGreen, int bgBlue, int bgAlpha, int fgRed, int fgGreen, int fgBlue, int fgAlpha) {
        this.size = size;
        this.bg = this.color(bgRed, bgGreen, bgBlue, bgAlpha);
        this.fg = this.color(fgRed, fgGreen, fgBlue, fgAlpha);
        this.stroke = this.size * this.strokePercent;
        this.rectangleList = new ArrayList<>();
    }

    @Override
    public String color(int red, int green, int blue, int alpha) {
        StringBuilder builder = new StringBuilder(48);
        builder.append("rgba(").append(red).append(",").append(green).append(",").append(blue);
        builder.append(",").append(alpha < 0 || alpha > 255 ? 1 : Math.floor(alpha / 255)).append(")");
        return builder.toString();
    }

    @Override
    public void rectangle(int x, int y, int width, int height, String color) {
        this.rectangleList.add(new Rectangle(x, y, width, height, color));
    }

    @Override
    public String getImage() {
        StringBuilder xml = new StringBuilder();
        xml.append("<svg xmlns='http://www.w3.org/2000/svg'").append(" width='").append(this.size).append("' height='").append(this.size)
                .append("' style='background-color:").append(this.bg).append(";'>").append("<g style='fill:").append(this.fg)
                .append("; stroke:").append(this.fg).append("; stroke-width:").append(this.stroke).append(";'>");
        rectangleList.stream().forEach(rectangle -> {
            if (!StringUtils.equals(this.bg, rectangle.getColor())) {
                xml.append("<rect ").append(" x='").append(rectangle.getX()).append("'")
                        .append(" y='").append(rectangle.getY()).append("'")
                        .append(" width='").append(rectangle.getW()).append("'")
                        .append(" height='").append(rectangle.getH()).append("'").append("/>");
            }
        });
        xml.append("</g></svg>");
        return xml.toString();
    }

    @Override
    public String getBase64() {
        return Base64.getEncoder().encodeToString(getImage().getBytes());
    }

    public String getBg() {
        return bg;
    }

    public String getFg() {
        return fg;
    }
}
