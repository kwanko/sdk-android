package fr.kwanko.internal.overly;

import android.content.Context;

import fr.kwanko.internal.model.AdModel;
import fr.kwanko.overly.OverlyAdListener;

/**
 * SourceCode
 * Created by erusu on 5/24/2017.
 */

public abstract class BaseOverlyAdDisplayController {

    private OverlyAdListener externalListener;
    private Context context;
    private AdModel adModel;

    public BaseOverlyAdDisplayController(Context context, AdModel adModel,
                                         OverlyAdListener externalListener) {
        this.externalListener = externalListener;
        this.context = context;
        this.adModel = adModel;
    }

    public abstract boolean isLoaded();

    public abstract void show() ;

    public abstract void cancel();

    protected OverlyAdListener getListener() {
        return externalListener;
    }

    protected Context getContext() {
        return context;
    }

    protected AdModel getAdModel() {
        return adModel;
    }

    public void onDestroy(){
        externalListener = null;
        context = null;
        adModel = null;
    }
}
