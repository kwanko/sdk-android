package fr.kwanko.model;

import android.webkit.JavascriptInterface;

/**
 * Created by vfatu on 12.01.2017.
 */

public class SizeProperties {
    int width, height;

    public SizeProperties(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @JavascriptInterface
    public int getWidth() {
        return width;
    }

    @JavascriptInterface
    public int getHeight() {
        return height;
    }
}