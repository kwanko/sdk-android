package fr.kwanko.internal.factory;

import fr.kwanko.internal.model.AdModel;
import fr.kwanko.internal.containers.BaseContainer;
import fr.kwanko.internal.content.ContentController;

/**
 * SourceCode
 * Created by erusu on 3/30/2017.
 */

public interface ContentControllerFactory {

    ContentController create(AdModel adModel, BaseContainer container);
}
