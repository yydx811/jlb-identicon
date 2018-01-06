package com.bestjlb.identicon.image;

/**
 * Created by yydx811 on 2018/1/6.
 */
public class Rectangle {

    private final int x;

    private final int y;

    private final int w;

    private final int h;

    private final String color;

    public Rectangle(int x, int y, int w, int h, String color) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public String getColor() {
        return color;
    }
}
