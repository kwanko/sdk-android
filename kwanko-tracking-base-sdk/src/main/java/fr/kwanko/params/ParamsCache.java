package fr.kwanko.params;

/**
 * SourceCode
 * Created by erusu on 6/12/2017.
 */

public interface ParamsCache {

    ParamValue getParam(String key);
    ParamValue setParam(String key, ParamValue value);
    boolean hasParam(String key);
    void evictAll();

}
