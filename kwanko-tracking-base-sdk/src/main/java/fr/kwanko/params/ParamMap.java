package fr.kwanko.params;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * SourceCode
 * Created by erusu on 2/20/2017.
 */

public class ParamMap implements Map<String,ParamValue> {

    private Map<String,ParamValue> paramsMap;

    protected ParamMap(Map<String,ParamValue> params){
        this.paramsMap = params;
    }

    boolean hasParam(String paramKey){
        return paramsMap.containsKey(paramKey);
    }

    @Override
    public int size() {
        return paramsMap.size();
    }

    @Override
    public boolean isEmpty() {
        return paramsMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return paramsMap.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return paramsMap.containsValue(o);
    }

    @Override
    public ParamValue get(Object o) {
        return paramsMap.get(o);
    }

    @Override
    public ParamValue put(String s, ParamValue paramValue) {
        return paramsMap.put(s,paramValue);
    }

    @Override
    public ParamValue remove(Object o) {
        return paramsMap.remove(o);
    }

    @Override
    public void putAll(@NonNull Map<? extends String, ? extends ParamValue> map) {
        paramsMap.putAll(map);
    }

    @Override
    public void clear() {
        paramsMap.clear();
    }

    @NonNull
    @Override
    public Set<String> keySet() {
        return paramsMap.keySet();
    }

    @NonNull
    @Override
    public Collection<ParamValue> values() {
        return paramsMap.values();
    }

    @NonNull
    @Override
    public Set<Entry<String, ParamValue>> entrySet() {
        return paramsMap.entrySet();
    }

    Map<String,ParamValue> getParamsMap(){
        return paramsMap;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for(Map.Entry<String,ParamValue> entry:this.entrySet()){
            builder.append("\"")
                    .append(entry.getKey())
                    .append("\":")
                    .append(entry.getValue())
                    .append(",");
        }
        return builder.toString();
    }

    public static abstract class Builder{

        protected Map<String,ParamValue> buildMap;

        public Builder(){
            this.buildMap = new HashMap<>();
        }

        public Builder(ParamMap params){
            buildMap = params.getParamsMap();
            if(buildMap == null){
                buildMap = new HashMap<>();
            }
        }

        @SuppressWarnings("unchecked")
        public <T extends Builder> T setParam(String paramKey, ParamValue value){
            this.buildMap.put(paramKey,value);
            return (T)this;
        }

        public abstract ParamMap build();
    }
}
