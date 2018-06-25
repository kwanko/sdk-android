package fr.kwanko.params;

import android.view.Gravity;

import fr.kwanko.common.Preconditions;

/**
 * SourceCode
 * Created by erusu on 2/22/2017.
 */

public class Position {

    private Position(){
        throw new AssertionError("instance is not allowed");
    }

    public static final String TOP = "top";
    public static final String BOTTOM = "bottom";
    public static final String DEFAULT = "default";

    public static int gravityFromPositionValue(String position){
        Preconditions.checkNotNull(position);
        switch (position){
            case TOP:
                return Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            case BOTTOM:
                return Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            case DEFAULT:
                return Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
            default:
                throw new IllegalArgumentException("position: "+position+" is not allowed");
        }
    }
}
