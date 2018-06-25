package fr.kwanko.internal.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import fr.kwanko.SupportedFormats;
import fr.kwanko.common.KwankoLog;
import fr.kwanko.internal.containers.BaseContainer;
import fr.kwanko.internal.containers.OverlyContainer;
import fr.kwanko.internal.content.ContentController;
import fr.kwanko.internal.content.HtmlContentController;
import fr.kwanko.internal.content.MraidContentController;
import fr.kwanko.internal.content.OverlyMraidContentController;
import fr.kwanko.internal.model.AdModel;
import fr.kwanko.model.KwankoPlacementType;
import fr.kwanko.params.TrackingParamsUtils;

/**
 * SourceCode
 * Created by erusu on 3/30/2017.
 */

public class ContentControllerFactoryImpl implements ContentControllerFactory {

    @Override
    public ContentController create(AdModel adModel, BaseContainer container) {
        if(adModel.getMediation() != null){
            return reflectAdMobContentController(container);
        }
        if(adModel.isMraid()){
            if(container instanceof OverlyContainer){
                return new OverlyMraidContentController(container,
                        adModel.getCloseButtonMetadata(),
                        getPlacementType(adModel));
            }else {
                return new MraidContentController(container, adModel.getCloseButtonMetadata(),
                        getPlacementType(adModel));
            }
        }
        return new HtmlContentController(container,adModel.getCloseButtonMetadata());
    }

    @SuppressWarnings("all")
    private ContentController reflectAdMobContentController(BaseContainer container){
        try {
            Class<? extends ContentController> clazz =
                    (Class<? extends ContentController>)
                            Class.forName("com.ads.kwankoandroidsdk.mediation.admob.internal.AdMobBannerContentController");
            Constructor<? extends ContentController> constructor =
                    clazz.getConstructor(BaseContainer.class);
            return constructor.newInstance(container);
        } catch (ClassNotFoundException e) {
            KwankoLog.e(e);
        }catch (ClassCastException e){
            KwankoLog.e(e);
        } catch (NoSuchMethodException e) {
            KwankoLog.e(e);
        } catch (IllegalAccessException e) {
            KwankoLog.e(e);
        } catch (InstantiationException e) {
            KwankoLog.e(e);
        } catch (InvocationTargetException e) {
            KwankoLog.e(e);
        }
        return null;
    }

    private KwankoPlacementType getPlacementType(AdModel model){
        switch (model.getFormat()){
            case SupportedFormats.FORMAT_BANNER:
                return KwankoPlacementType.INLINE;
            case SupportedFormats.FORMAT_OVERLY:
                if(isFullScreen(model)) {
                    return KwankoPlacementType.INTERSTITIAL;
                }else{
                    return KwankoPlacementType.OVERLY;
                }
        }
        return SupportedFormats.FORMAT_BANNER.equals(model.getFormat()) ?
                KwankoPlacementType.INLINE:
                KwankoPlacementType.INTERSTITIAL;
    }

    private boolean isFullScreen(AdModel model){
        int width = TrackingParamsUtils.getScreenWidth();
        int height = TrackingParamsUtils.getScreenHeight();
        return model.getAdWidth() >= width &&
                model.getAdHeight() >= height;
    }
}
