package fr.kwanko.rest.network;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.kwanko.common.KwankoLog;
import fr.kwanko.params.ParamMap;
import fr.kwanko.params.ParamValue;
import fr.kwanko.rest.Pair;

/**
 * SourceCode
 * Created by erusu on 2/20/2017.
 */

public class ParamsAdapter {

    private static final String USER_INFO = "user_infos=";
    private static final String SDK_INFOS = "sdk_infos=";
    private static final String EMP = "emp";
    private static final String FORMAT = "format";
    private final ParamMap params;

    public ParamsAdapter(ParamMap params){
        this.params = params;
    }

    String getParamsAsStringWithRequestDetails(String slotId,String format){
        JSONObject userParams = new JSONObject();
        JSONObject sdkParams = new JSONObject();
        for(Map.Entry<String,ParamValue> entry:params.entrySet()){
            if(entry.getValue() == null){
                continue;
            }
            try {
                if(!entry.getValue().isFromSdk()) {
                    userParams.put(entry.getKey(), entry.getValue().toString());
                }else{
                    sdkParams.put(entry.getKey(), entry.getValue().toString());
                }
            }catch (JSONException e){
                KwankoLog.e(e);
            }
        }
        try {
            sdkParams.put(FORMAT, format);
        }catch (JSONException e){
            KwankoLog.e(e);
        }
        /*JSONObject res = new JSONObject();
        try {
            res.put(USER_INFO,userParams);
            res.put(SDK_INFOS,sdkParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        //return res.toString().replaceAll("\\\\","");
        return EMP + "="+ cleanSlotId(slotId) +"&"+(USER_INFO +userParams.toString() + "&" +
                SDK_INFOS  +sdkParams.toString()).replaceAll("\\\\","");
    }

    /**
     * In the past, slotId started by 'E', but now the id must be sent without.
     */
    private String cleanSlotId(String slotId) {
        return slotId.substring(1);
    }

    public List<Pair> getPairListFromTrackingParams(){
        List<Pair> pairs = new ArrayList<>();
        for(String key:params.keySet()){
            ParamValue paramValue = params.get(key);
            if(paramValue == null || paramValue.toString() == null){
                continue;
            }
            pairs.add(new Pair(key,params.get(key).toString()));
        }
        return pairs;
    }

}
