package se.lexicon.market.component.client;

import se.lexicon.market.component.domain.MarketOrder;
import se.lexicon.market.component.domain.MarketOrders;
import se.lexicon.market.component.service.MarketComponentService;
import com.so4it.common.util.object.Required;

import java.util.Set;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public class MarketComponentClientImpl implements MarketComponentClient {

    private MarketComponentService marketComponentService;

    public MarketComponentClientImpl(MarketComponentService marketComponentService) {
        this.marketComponentService = Required.notNull(marketComponentService,"marketComponentService");
    }

    @Override
    public Set<String> getInstruments(String ssn){ return marketComponentService.getInstruments(ssn); }

    @Override
    public MarketOrders getMarketOrders(String instrument, String ssn){
        return marketComponentService.getMarketOrders(instrument,ssn); }

    @Override
    public Boolean placeMarketOrder(MarketOrder marketOrder) {
        return marketComponentService.placeMarketOrder(marketOrder); }

}
