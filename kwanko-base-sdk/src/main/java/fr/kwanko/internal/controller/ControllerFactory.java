package fr.kwanko.internal.controller;

import fr.kwanko.KwankoAdType;
import fr.kwanko.internal.factory.ContainerFactory;
import fr.kwanko.internal.factory.ContentControllerFactory;

/**
 * SourceCode
 * Created by erusu on 5/23/2017.
 */

public interface ControllerFactory {

    AdController createAdController(KwankoAdType adType, ContainerFactory containerFactory,
                                    ContentControllerFactory contentControllerFactory);

    ControllerFactory DEFAULT_FACTORY = new ControllerFactory() {

        @Override
        public AdController createAdController(KwankoAdType adType,
                                               ContainerFactory containerFactory,
                                               ContentControllerFactory contentControllerFactory) {
            return new KwankoAdController(adType,containerFactory,
                    contentControllerFactory);
        }
    };


}
