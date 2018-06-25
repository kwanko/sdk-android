package fr.kwanko.rest.network.http;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * SourceCode
 * Created by erusu on 3/16/2017.
 */

public abstract class HttpSerialiser<T> {

    public abstract T parse(HttpURLConnection connection) throws IOException, JSONException;

    protected String readData(HttpURLConnection conn) throws IOException {
        StringBuilder data = new StringBuilder();

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line ;

        while ((line = rd.readLine()) != null) {
            data.append(line);
        }
        return data.toString();
    }

}
