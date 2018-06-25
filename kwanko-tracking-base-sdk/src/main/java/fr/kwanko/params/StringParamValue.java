package fr.kwanko.params;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * SourceCode
 * Created by erusu on 2/20/2017.
 */

public class StringParamValue extends ParamValue{

    private final String value;

    public StringParamValue(String value){
        this.value = value;
    }

    StringParamValue(String value,boolean isFromSdk,boolean cacheable){
        this(value,isFromSdk);
        setCacheable(cacheable);
    }

    StringParamValue(String value, boolean isFromSdk){
        this(value);
        setFromSdk(isFromSdk);
    }

    StringParamValue(int value){
        this.value = String.valueOf(value);
    }

    StringParamValue(int value,boolean isFromSdk){
        this(value);
        setFromSdk(isFromSdk);
    }

    public StringParamValue(double value){
        this.value = String.valueOf(value);
    }

    StringParamValue(double value,boolean isFromSdk){
        this(value);
        setFromSdk(isFromSdk);
    }

    StringParamValue(double value,boolean isFromSdk,boolean cacheable){
        this(value);
        setFromSdk(isFromSdk);
        setCacheable(cacheable);
    }

    StringParamValue(List<String> values){
        if(values == null){
            value = null;
            return;
        }
        String [] valuesArray = new String[values.size()];
        value = init(values.toArray(valuesArray));
    }

    StringParamValue(String [] values){
        value = init(values);
    }

    StringParamValue(Map<String,String> mapValues){
        value = initWithMap(mapValues);
    }

    public StringParamValue(boolean value){
        this.value = String.valueOf(value);
    }

    StringParamValue(JSONArray array){
        this.value = array!= null ?array.toString():null;
    }

    StringParamValue(JSONArray array,boolean isFromSdk){
        this(array);
        setFromSdk(isFromSdk);
    }

    StringParamValue(JSONArray array,boolean isFromSdk,boolean cacheable){
        this(array,isFromSdk);
        setCacheable(cacheable);
    }

    private String init(String [] values){
        if(values == null){
            return null;
        }
        int size = values.length;
        int pos = 0;
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(String s:values){
            builder.append(s);
            if(pos<size - 1){
                builder.append(",");
            }
            pos++;
        }
        builder.append("]");
        return builder.toString();
    }

    private String initWithMap(Map<String,String> map){
        if(map == null){
            return null;
        }
        StringBuilder builder = new StringBuilder();
        int size = map.size();
        int pos = 0;
        builder.append("{");
        for(Map.Entry<String,String> entry:map.entrySet()){
            getLine(builder,entry.getKey(),entry.getValue());
            if(pos < size - 1){
                builder.append(",");
            }
            pos++;
        }
        builder.append("}");
        return builder.toString();
    }

    private void getLine(StringBuilder builder,String key,String  value){
        builder.append("\"").append(key).append("\":\"").append(value).append("\"");
    }

    @Override
    public String toString() {
        return value;
    }
}
