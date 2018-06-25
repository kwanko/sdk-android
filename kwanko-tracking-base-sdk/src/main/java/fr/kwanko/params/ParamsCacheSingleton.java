package fr.kwanko.params;

/**
 * SourceCode
 * Created by erusu on 6/12/2017.
 */

public enum ParamsCacheSingleton {

    Instance(ParamsCacheFactory.DEFAULT);

    private ParamsCache cache;

    ParamsCacheSingleton(ParamsCacheFactory factory){
           cache = factory.getParamsCache();
    }

    public ParamsCache getParamCache(){
        return cache;
    }
}
