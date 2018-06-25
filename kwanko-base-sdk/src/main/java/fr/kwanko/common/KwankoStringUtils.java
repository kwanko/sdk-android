package fr.kwanko.common;

/**
 * SourceCode
 * Created by erusu on 2/20/2017.
 */

public class KwankoStringUtils {

    public static final String EMPTY = "";

    private KwankoStringUtils(){
        throw new AssertionError("instance is not allowed");
    }

    public static boolean isEmpty(String str){
        return str == null || str.isEmpty() ;
    }
}
