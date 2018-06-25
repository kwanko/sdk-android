package fr.kwanko.common.permissions.request.conf;

import fr.kwanko.common.permissions.request.strategy.selector.DefaultStrategySelector;
import fr.kwanko.common.permissions.request.strategy.selector.StrategySelector;

/**
 * Created by erusu on 10/19/2016.
 */

public class Configuration {

    static{
        INSTANCE = new Configuration();
    }
    public static final Configuration INSTANCE;

    private StrategySelector strategySelector;

    private Configuration(){
        loadDefaultConfiguration();
    }

    private void loadDefaultConfiguration(){
        strategySelector = new DefaultStrategySelector();
    }

    public StrategySelector getStrategySelector(){
        return strategySelector;
    }

    public void setStrategySelector(StrategySelector strategySelector){
        this.strategySelector = strategySelector;
    }
}
