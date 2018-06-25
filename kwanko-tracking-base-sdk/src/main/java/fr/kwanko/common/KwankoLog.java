package fr.kwanko.common;

import android.util.Log;

import java.util.Map;

/**
 * SourceCode
 * Created by erusu on 2/24/2017.
 */

public class KwankoLog {

    private static final String TAG = "Kwanko";
    private static final String EXCEPTION_MESSAGE = "Exception with stacktrace";
    private static Logger innerLogger = new DefaultLogger();

    private KwankoLog(){
        throw new AssertionError("instance is not allowed");
    }

    public static void setLogger(Logger logger){
        Preconditions.checkNotNull(logger);
        innerLogger = logger;
    }

    public static void e(Throwable t){
        innerLogger.e(TAG,t);
    }

    public static void e(String message,Throwable t){
        innerLogger.e(TAG,message,t);
    }

    public static void d(String message){
        innerLogger.d(TAG,message);
    }

    public static void w(String message){
        innerLogger.w(TAG,message);
    }

    public static void logRequest(Map<String,String > headers, String request){
        innerLogger.logRequest(headers,request);
    }

    public static void logResponse(String response){
        if(response != null) {
            innerLogger.logResponse(response);
        }else{
            innerLogger.logResponse("null response");
        }
    }

    public static void logQuery(String query){
        innerLogger.logQuery(query);
    }


    public  interface Logger{
        void e(String tag,Throwable t);
        void e(String tag,String message, Throwable t);
        void d(String tag, String msg);
        void w(String tag, String msg);

        void logRequest(Map<String,String> headers,String request);
        void logResponse(String response);
        void logQuery(String query);
    }

    private static class DefaultLogger implements Logger{

        @Override
        public void e(String tag, Throwable t) {
            Log.e(TAG,EXCEPTION_MESSAGE,t);
        }

        @Override
        public void e(String tag, String message, Throwable t) {
            Log.e(TAG,message,t);
        }

        @Override
        public void w(String tag, String msg) {
            Log.w(tag,msg);
        }

        @Override
        public void d(String tag, String msg) {
            Log.d(tag,msg);
        }

        @Override
        public void logRequest(Map<String,String> headers,String request) {
            Log.d(TAG,"Request: "+request);
        }

        @Override
        public void logResponse(String response) {
            Log.d(TAG,"Response:"+response);
        }

        @Override
        public void logQuery(String query) {
            Log.d(TAG,"Query: "+query);
        }
    }
}
