package fr.kwanko.common.permissions.request.strategy.selector;

import fr.kwanko.common.permissions.request.featurers.Feature;
import fr.kwanko.common.permissions.request.strategy.Strategy;

/**
 * Created by erusu on 10/18/2016.
 */

public interface StrategySelector {

    Strategy getStrategy(Feature feature);


}
