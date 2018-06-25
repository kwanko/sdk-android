package fr.kwanko.internal.close;

/**
 * SourceCode
 * Created by erusu on 6/14/2017.
 */

public class CloseEvents {

    public static final OnCloseListener.CloseEvent GENERAL = new GeneralCloseEvent();
    public static final OnCloseListener.CloseEvent RESIZE = new ResizeCloseEvent();
    public static final OnCloseListener.CloseEvent EXPAND = new ExpandCloseEvent();

    private CloseEvents(){
        throw new AssertionError();
    }
}
