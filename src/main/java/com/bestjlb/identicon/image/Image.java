package com.bestjlb.identicon.image;

/**
 * Created by yydx811 on 2018/1/5.
 */
public interface Image {

    String color(int red, int green, int blue, int alpha);

    void rectangle(int x, int y, int width, int height, String color);

    String getImage();

    String getBase64();
}
