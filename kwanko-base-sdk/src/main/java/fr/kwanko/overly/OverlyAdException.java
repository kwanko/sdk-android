package fr.kwanko.overly;

/**
 * SourceCode
 * Created by erusu on 5/24/2017.
 */

public class OverlyAdException extends Exception {

    private int cause;

    public OverlyAdException(int cause){
        this.cause = cause;
    }

    public OverlyAdException(){
        //empty constructor
    }
}
