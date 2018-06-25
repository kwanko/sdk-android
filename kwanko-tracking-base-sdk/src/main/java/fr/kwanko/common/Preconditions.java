package fr.kwanko.common;

/**
 * SourceCode
 * Created by erusu on 2/24/2017.
 */

public class Preconditions {

    private Preconditions(){
        throw new AssertionError("instance is not allowed");
    }

    public static void checkNotNull(Object reference){
        if(reference == null){
            throw new IllegalArgumentException("Argument should not be null");
        }
    }
}
