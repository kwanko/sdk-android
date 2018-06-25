package fr.kwanko.params;

/**
 * SourceCode
 * Created by erusu on 6/12/2017.
 */

public interface ParamsCacheFactory {

    ParamsCache getParamsCache();

    ParamsCacheFactory DEFAULT = new ParamsCacheFactory() {
        @Override
        public ParamsCache getParamsCache() {
            return new HeapParamsCache();
        }
    };
}
