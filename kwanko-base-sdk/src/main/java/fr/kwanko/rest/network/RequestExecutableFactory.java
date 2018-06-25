package fr.kwanko.rest.network;

import fr.kwanko.AdRequest;
import fr.kwanko.SupportedFormats;
import fr.kwanko.internal.model.AdModel;
import fr.kwanko.rest.network.http.HttpStack;

/**
 * SourceCode
 * Created by erusu on 3/10/2017.
 */

public class RequestExecutableFactory {

    private static RequestExecutableFactory instance;

    private RequestExecutable defaultRequestExecutable;

    private RequestExecutableFactory(){
        //private constructor
    }

    public static RequestExecutableFactory getInstance() {
        if(instance == null){
            instance = new RequestExecutableFactory();
        }
        return instance;
    }

    @SuppressWarnings("all")
    public RequestExecutable newRequestExecutable(AdRequest request){
        if(defaultRequestExecutable != null){
            RequestExecutable temp = defaultRequestExecutable;
            defaultRequestExecutable = null;
            return temp;
        }
        if(SupportedFormats.FORMAT_NATIVE.equals(request.getFormat())){
            return new NativeAdsRequestExecutable();
        }
        return new DefaultRequestExecutable(new HttpStack<AdModel>(new AdModelSerialiser()) {
        });
    }
}
