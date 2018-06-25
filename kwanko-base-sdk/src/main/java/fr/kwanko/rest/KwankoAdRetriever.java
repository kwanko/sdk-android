package fr.kwanko.rest;

import android.content.Context;

import java.io.File;

import fr.kwanko.AdRequest;
import fr.kwanko.internal.model.AdModel;

/**
 * SourceCode
 * Created by erusu on 3/10/2017.
 */

public interface KwankoAdRetriever {

    void retrieveAd(Context context,
                    String slotId,
                    KwankoAdRetrieverListener responseListener);

    void retrieveAd(AdRequest request, KwankoAdRetrieverListener<? extends AdModel> listener);

    void cancelAllRequest();

    void setExtraResourceFile(File file);

    class Factory{
        public static KwankoAdRetriever createAdRetriever(){
            return new KwankoAdRetrieverImpl();
        }
    }

    interface KwankoAdRetrieverListener<T extends AdModel> {
        void onResult(T adModel);
    }

}
