package fr.kwanko.model;

import android.webkit.JavascriptInterface;

/**
 * Created by vfatu on 05.01.2017.
 */

public class PositionProperties {

    float x, y, width, height;

    public PositionProperties(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @JavascriptInterface
    public float getX() {
        return x;
    }

    @JavascriptInterface
    public float getY() {
        return y;
    }

    @JavascriptInterface
    public float getWidth() {
        return width;
    }

    @JavascriptInterface
    public float getHeight() {
        return height;
    }
}
