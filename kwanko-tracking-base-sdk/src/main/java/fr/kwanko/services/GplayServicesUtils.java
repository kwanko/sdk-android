package fr.kwanko.services;

import android.content.Context;

import fr.kwanko.common.KwankoLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * SourceCode
 * Created by erusu on 2/23/2017.
 */

public class GplayServicesUtils {

    private static final boolean RAISE_EXCEPTION = true;

    private static final String ADVERTISING_ID_CLASS_NAME
            = "com.google.android.gms.ads.identifier.AdvertisingIdClient";
    private static final String GET_ADVERTISING_ID_METHOD = "getAdvertisingIdInfo";
    private static final String GET_ID_METHOD = "getId";

    private GplayServicesUtils(){
        throw new AssertionError("instance is not allowed");
    }

    public static String getAdvertisingId(Context context){
        SharesPreferencesHelper sh =
                SharesPreferencesHelper.with(context);
        String id = sh.getAdvertisingId();
        if(id != null){
            return id;
        }else{
            id = getAdvertisingIdFromGplayServices(context);
        }
        if(id == null){
            raiseGplayServiceNotFoundException();
        }
        sh.saveAdvertisingId(id);
        return id;
    }

    /**
     * Call this method AdvertisingIdClient.getAdvertisingIdInfo(context)
     * with reflection
     * @return advertisingId
     */
    private static String getAdvertisingIdFromGplayServices(Context context){
        try {
            Class AdvertisingId = Class.forName(ADVERTISING_ID_CLASS_NAME);
            Method method = AdvertisingId.getMethod(GET_ADVERTISING_ID_METHOD,Context.class);
            Object infoObject = method.invoke(null,context);
            if(infoObject != null ){
                return getAdvertisingIdFromInfoObject(infoObject);
            }
        } catch (ClassNotFoundException e) {
            KwankoLog.e(e);
        } catch (NoSuchMethodException e) {
            KwankoLog.e(e);
        } catch (InvocationTargetException e) {
            KwankoLog.e(e);
        } catch (IllegalAccessException e) {
            KwankoLog.e(e);
        }catch(Exception e){
            KwankoLog.e(e);
        }
        return null;
    }

    private static String getAdvertisingIdFromInfoObject(Object infoObject)
            throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        Object idObject =  infoObject.getClass()
                .getMethod(GET_ID_METHOD).invoke(infoObject);
        return idObject != null ? idObject.toString():null;
    }

    private static void raiseGplayServiceNotFoundException(){
        if(!RAISE_EXCEPTION) {
            throw new IllegalStateException("Google play services are not present!" +
                    " Please add them as a dependency");
        }
    }




}
