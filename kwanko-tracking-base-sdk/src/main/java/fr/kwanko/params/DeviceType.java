package fr.kwanko.params;
/**
 * SourceCode
 * Created by erusu on 2/20/2017.
 */

public class DeviceType {

    public static final int TABLET = 3;
    public static final int MOBILE = 2;
    public static final int TELEVISION = 6;
    public static final int OTHER = 0;

    private DeviceType(){
        throw new AssertionError("instance is not allowed");
    }
}
