package fr.kwanko.internal.close;

/**
 * Created by vfatu on 14.02.2017.
 */

public interface OnCloseListener {

    void onCloseEvent(Object source,CloseEvent event);

    interface CloseEvent{}
}
