package fr.kwanko.internal.factory;

import android.content.Context;

import fr.kwanko.KwankoAd;
import fr.kwanko.internal.containers.BaseContainer;
import fr.kwanko.internal.model.AdModel;

/**
 * SourceCode
 * Created by erusu on 3/28/2017.
 */

public interface ContainerFactory {

    BaseContainer createContainer(AdModel model, Context context);
}
