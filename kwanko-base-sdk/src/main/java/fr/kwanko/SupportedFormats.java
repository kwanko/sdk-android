package fr.kwanko;

/**
 * SourceCode
 * Created by erusu on 3/21/2017.
 */

public class SupportedFormats {

    public static final String FORMAT_OVERLY = "overlay";
    public static final String FORMAT_BANNER = "inline";
    public static final String FORMAT_PARALLAX = "parallax";
    public static final String FORMAT_PARALLAX_RECYCLER_VIEW = "rv_parallax";
    public static final String FORMAT_PARALLAX_LIST_VIEW = "lv_parallax";
    public static final String FORMAT_NATIVE = "native";

    private SupportedFormats(){
        throw new AssertionError("instance is not allowed");
    }
}
