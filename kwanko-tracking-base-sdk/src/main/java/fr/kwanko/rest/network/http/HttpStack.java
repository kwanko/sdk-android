package fr.kwanko.rest.network.http;

import android.net.Uri;

import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.kwanko.common.KwankoLog;
import fr.kwanko.rest.Pair;

/**
 * SourceCode
 * Created by erusu on 3/16/2017.
 */

public  class HttpStack<T> {

    private static final String ENCODING = "UTF-8";
    private final HttpSerialiser<T> serialiser;

    public HttpStack(HttpSerialiser<T> serialiser){
        this.serialiser = serialiser;
    }

    public T send(String url, Map<String,String> headers, String params, String method) {
        HttpURLConnection connection = null;
        try {
            connection = initialiseConnection(url,method,headers,true,true);
            writeParameters(connection,params);
            return serialiser.parse(connection);
        }catch (IOException e){
            KwankoLog.e(e);
        } catch (JSONException e) {
            KwankoLog.e(e);
        }finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public T sendNoResponse(String url, Map<String,String> headers, List<Pair> params, String method){
        HttpURLConnection connection = null;
        try {
            connection = initialiseConnection(url,method,headers,true,true);
            writeParameters(connection,params);
            return serialiser.parse(connection);
        }catch ( IOException e){
            KwankoLog.e(e);
        } catch (JSONException e) {
            KwankoLog.e(e);
        }finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public void call(String url){
        HttpURLConnection connection = null;
        try {
            connection = initialiseConnection(url,"GET",new HashMap<String, String>(),false,false);
        }catch ( IOException e){
            KwankoLog.e(e);
        }finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    private HttpURLConnection initialiseConnection(String urlString,
                                                   String method,Map<String,String> headers,
                                                   boolean doInput,
                                                   boolean doOutput) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        for(Map.Entry<String,String> header:headers.entrySet()){
            conn.setRequestProperty(header.getKey(),header.getValue());
        }
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod(method);
        conn.setDoInput(doInput);
        conn.setDoOutput(doOutput);
        return conn;
    }

    /**
     * "maff", "S312D314F2F71" = mraid
     * "maff", "S312D314F2F81" = mraid2
     * @param conn a HttpConnection
     * @throws IOException
     */
    private void writeParameters(HttpURLConnection conn, List<Pair> params) throws IOException {
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, ENCODING));
        String query = getQuery(params);
        KwankoLog.logQuery(query);
        writer.write(query);
        writer.flush();
        writer.close();
        os.close();
    }

    private void writeParameters(HttpURLConnection conn, String params) throws IOException {
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(params);
        writer.flush();
        writer.close();
        os.close();
    }

    private String getQuery(List<Pair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Pair pair : params) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(pair.getKey(), ENCODING));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), ENCODING));
        }
        return result.toString();
    }


}
