package fr.kwanko.common.permissions.request.strategy.selector;

import fr.kwanko.common.permissions.request.featurers.CommonFeatures;
import fr.kwanko.common.permissions.request.featurers.Feature;
import fr.kwanko.common.permissions.request.strategy.Strategy;
import fr.kwanko.common.permissions.request.strategy.impl.RequestGenericStrategy;
import fr.kwanko.common.permissions.request.strategy.impl.RequestLocationStrategy;

/**
 * Created by erusu on 10/18/2016.
 */

public class DefaultStrategySelector implements StrategySelector {

    @Override
    public Strategy getStrategy(Feature feature) {
        if(feature instanceof CommonFeatures.Location){
            return new RequestLocationStrategy(feature);
        }
        if(feature instanceof CommonFeatures.GenericFeature){
            return new RequestGenericStrategy(feature);
        }
        return null;
    }
}
