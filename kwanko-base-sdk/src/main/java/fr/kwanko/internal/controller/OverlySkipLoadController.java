package fr.kwanko.internal.controller;

import fr.kwanko.KwankoAdType;
import fr.kwanko.internal.close.CloseEvents;
import fr.kwanko.internal.close.OnCloseListener;
import fr.kwanko.internal.containers.BaseContainer;
import fr.kwanko.internal.content.ContentController;
import fr.kwanko.internal.factory.ContainerFactory;
import fr.kwanko.internal.factory.ContentControllerFactory;
import fr.kwanko.internal.model.AdModel;

/**
 * SourceCode
 * Created by erusu on 5/26/2017.
 */

class OverlySkipLoadController extends KwankoAdController {

    private AdModel model;
    private ContentController contentController;
    private OnCloseListener closeListener;

    OverlySkipLoadController(KwankoAdType adView, ContainerFactory containerFactory,
                             ContentControllerFactory contentControllerFactory,
                             AdModel adModel,
                             OnCloseListener listener) {
        super(adView, containerFactory, contentControllerFactory);
        this.model = adModel;
        this.closeListener = listener;
    }

    @Override
    public void onAttach() {
        super.onAttach();
        onResult(model);
    }

    @Override
    void setContentControllerToContainer(BaseContainer container, ContentController contentController) {
        super.setContentControllerToContainer(container, contentController);
        this.contentController = contentController;
        contentController.subscribeForCloseEvent(CloseEvents.GENERAL,closeListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(this.contentController != null && closeListener != null){
            contentController.unSubscribeForCloseEvent(CloseEvents.GENERAL,closeListener);
        }
        contentController = null;
        closeListener = null;
    }
}
