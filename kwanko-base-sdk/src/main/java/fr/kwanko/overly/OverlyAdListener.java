package fr.kwanko.overly;

/**
 * SourceCode
 * Created by erusu on 5/23/2017.
 */

public abstract class OverlyAdListener {

    public void onOverlyAdLoaded(){}
    public void onError(Exception e){}
    public void onOverlyAdClosed(){}
    public void onOverlyAdOpen(){}
}
