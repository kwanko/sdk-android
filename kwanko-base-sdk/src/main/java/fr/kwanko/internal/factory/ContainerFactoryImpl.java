package fr.kwanko.internal.factory;

import android.content.Context;

import fr.kwanko.KwankoAd;
import fr.kwanko.internal.containers.BannerContainer;
import fr.kwanko.internal.containers.BaseContainer;
import fr.kwanko.internal.containers.BaseParallaxContainer;
import fr.kwanko.internal.containers.BaseResizeContainer;
import fr.kwanko.internal.containers.OverlyContainer;
import fr.kwanko.internal.containers.RecyclerViewParallaxContainer;
import fr.kwanko.internal.containers.ScrollViewParallaxContainer;
import fr.kwanko.internal.model.AdModel;

import static fr.kwanko.SupportedFormats.FORMAT_BANNER;
import static fr.kwanko.SupportedFormats.FORMAT_OVERLY;
import static fr.kwanko.SupportedFormats.FORMAT_PARALLAX;
import static fr.kwanko.SupportedFormats.FORMAT_PARALLAX_LIST_VIEW;
import static fr.kwanko.SupportedFormats.FORMAT_PARALLAX_RECYCLER_VIEW;

/**
 * SourceCode
 * Created by erusu on 3/28/2017.
 */

public class ContainerFactoryImpl implements ContainerFactory {


    @Override
    public BaseContainer createContainer( AdModel model, Context context) {

        switch (model.getFormat()){
            case FORMAT_BANNER:
                return new BannerContainer(context, new BaseResizeContainer.BaseResizeParams(model));
            case FORMAT_OVERLY:
                return new OverlyContainer(context, new BaseResizeContainer.BaseResizeParams(model));
            case FORMAT_PARALLAX:
                return new ScrollViewParallaxContainer(context, new BaseParallaxContainer.ParallaxParams(model) );
            case FORMAT_PARALLAX_RECYCLER_VIEW:
                return new RecyclerViewParallaxContainer(context, new BaseParallaxContainer.ParallaxParams(model) );
            case FORMAT_PARALLAX_LIST_VIEW:
                return new RecyclerViewParallaxContainer(context, new BaseParallaxContainer.ParallaxParams(model));
            default:
                throw new IllegalArgumentException("format is not supported");
        }
    }
}
