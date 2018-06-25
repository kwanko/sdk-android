package fr.kwanko.common;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * This class is a json serialiser and deserializer
 * SourceCode
 * Created by erusu on 4/6/2017.
 */

public class Josr {

    public <T> T fromJson(String jsonString, Class<T> clazz){
        try {
            T instance = clazz.newInstance();
            JSONObject json = new JSONObject(jsonString);
            Iterator<String> keyIterator = json.keys();
            while (keyIterator.hasNext()){
                String key = keyIterator.next();
                if(json.isNull(key)){
                    continue;
                }
                Object value = json.get(key);
                if(value != null) {
                    setValueTo(instance, key, value);
                }
            }
            return instance;
        } catch (JSONException e) {
            KwankoLog.e(e);
        } catch (InstantiationException e) {
            KwankoLog.e(e);
        } catch (IllegalAccessException e) {
            KwankoLog.e(e);
        }
        return null;
    }

    private <T> void setValueTo(T instance,String key,Object value){
        Class<?> clazz = instance.getClass();
        try {
            Field field = clazz.getDeclaredField(key);
            field.setAccessible(true);
            field.set(instance,value);
        } catch (NoSuchFieldException e) {
            KwankoLog.e(e);
        } catch (IllegalAccessException e) {
            KwankoLog.e(e);
        }
    }
}
