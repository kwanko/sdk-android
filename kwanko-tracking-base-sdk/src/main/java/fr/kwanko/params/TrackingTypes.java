package fr.kwanko.params;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * SourceCode
 * Created by erusu on 3/14/2017.
 */

public class TrackingTypes {

    private TrackingTypes(){
        throw new AssertionError("instance is not allowed");
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({fr.kwanko.params.DeviceType.MOBILE,
            fr.kwanko.params.DeviceType.OTHER,
            fr.kwanko.params.DeviceType.TABLET,
            fr.kwanko.params.DeviceType.TELEVISION})
    public @interface DeviceType {}

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({fr.kwanko.params.Os.ANDROID})
    @interface Os{}

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({fr.kwanko.params.Position.TOP,
            fr.kwanko.params.Position.BOTTOM,
            fr.kwanko.params.Position.DEFAULT})
    public @interface Position{}

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({fr.kwanko.params.AdSizeStrategy.PIXEL,
            fr.kwanko.params.AdSizeStrategy.RATIO})
    @interface AdSizeStrategy{}

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({fr.kwanko.params.Connectivity._2G,
            fr.kwanko.params.Connectivity._3G,
            fr.kwanko.params.Connectivity._4G,
            fr.kwanko.params.Connectivity.EDGE,
            fr.kwanko.params.Connectivity.H_PLUS,
            fr.kwanko.params.Connectivity.WIFI,
            fr.kwanko.params.Connectivity.UNKNOWN})
    public @interface Connectivity{}
}
