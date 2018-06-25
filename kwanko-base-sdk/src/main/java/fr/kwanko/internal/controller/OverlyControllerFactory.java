package fr.kwanko.internal.controller;

import fr.kwanko.KwankoAdType;
import fr.kwanko.internal.close.OnCloseListener;
import fr.kwanko.internal.factory.ContainerFactory;
import fr.kwanko.internal.factory.ContentControllerFactory;
import fr.kwanko.internal.model.AdModel;

/**
 * SourceCode
 * Created by erusu on 5/26/2017.
 */

public class OverlyControllerFactory  implements ControllerFactory{

    private final AdModel adModel;
    private OnCloseListener closeListener;

    public OverlyControllerFactory(AdModel adModel, OnCloseListener closeListener) {
        this.adModel = adModel;
        this.closeListener = closeListener;
    }

    @Override
    public AdController createAdController(KwankoAdType adType, ContainerFactory containerFactory,
                                           ContentControllerFactory contentControllerFactory) {
        return new OverlySkipLoadController(adType,
                containerFactory,contentControllerFactory,
                adModel,closeListener);
    }
}
