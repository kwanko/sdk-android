package fr.kwanko.rest.network;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import fr.kwanko.internal.model.AdModel;
import fr.kwanko.internal.model.EmptyAdModelException;
import fr.kwanko.rest.network.http.HttpSerialiser;

/**
 * SourceCode
 * Created by erusu on 3/16/2017.
 */

public class AdModelSerialiser extends HttpSerialiser<AdModel> {

    @Override
    public AdModel parse(HttpURLConnection connection) throws IOException, JSONException {
        try {
            return new AdModel(readData(connection));
        } catch (EmptyAdModelException e) {
            return null;
        }
    }
}
