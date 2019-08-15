package se.lexicon.market.component.client;

import se.lexicon.market.component.domain.Market;
import se.lexicon.market.component.domain.Markets;

import java.util.Set;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public interface MarketComponentClient {

    Set<String> getInstruments(String ssn);
    Markets getMarkets(String instrument, String ssn);

    void placeMarket(Market market);

}
