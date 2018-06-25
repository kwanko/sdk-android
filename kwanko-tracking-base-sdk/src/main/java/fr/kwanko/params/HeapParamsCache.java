package fr.kwanko.params;

import android.support.annotation.NonNull;

import java.util.concurrent.ConcurrentHashMap;

/**
 * SourceCode
 * Created by erusu on 6/12/2017.
 */

public class HeapParamsCache implements ParamsCache {

    private ConcurrentHashMap<String,ParamValue> heapCache;

    public HeapParamsCache(){
        heapCache = new ConcurrentHashMap<>();
    }

    @Override
    public ParamValue getParam(String key) {
        return heapCache.get(key);
    }

    @Override
    public ParamValue setParam(@NonNull String key,@NonNull ParamValue value) {
        return heapCache.put(key,value);
    }

    @Override
    public boolean hasParam(@NonNull String key) {
        return heapCache.containsKey(key);
    }

    @Override
    public void evictAll() {
        heapCache.clear();
    }
}
