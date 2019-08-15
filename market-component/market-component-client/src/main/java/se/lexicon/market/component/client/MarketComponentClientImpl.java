package se.lexicon.market.component.client;

import se.lexicon.market.component.domain.Market;
import se.lexicon.market.component.domain.Markets;
import se.lexicon.market.component.service.MarketComponentService;
import com.so4it.common.util.object.Required;

import java.util.Set;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public class MarketComponentClientImpl implements MarketComponentClient{

    private MarketComponentService marketComponentService;

    public MarketComponentClientImpl(MarketComponentService marketComponentService) {
        this.marketComponentService = Required.notNull(marketComponentService,"marketComponentService");
    }

    @Override
    public Set<String> getInstruments(String ssn){ return marketComponentService.getInstruments(ssn); }

    @Override
    public Markets getMarkets(String instrument, String ssn){ return marketComponentService.getMarkets(instrument,ssn); }

    @Override
    public void placeMarket(Market market) { marketComponentService.placeMarket(market); }

}
