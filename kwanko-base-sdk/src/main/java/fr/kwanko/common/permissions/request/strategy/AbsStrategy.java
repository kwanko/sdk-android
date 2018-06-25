package fr.kwanko.common.permissions.request.strategy;

import fr.kwanko.common.permissions.request.featurers.Feature;

/**
 * Created by erusu on 10/19/2016.
 */

public abstract class AbsStrategy implements Strategy {

    protected final Feature feature;

    public AbsStrategy(Feature feature){
        this.feature = feature;
    }
}
