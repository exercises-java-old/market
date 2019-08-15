package se.lexicon.market.component.service;


import com.so4it.gs.rpc.Broadcast;
import com.so4it.gs.rpc.Routing;
import se.lexicon.market.component.domain.Market;
import se.lexicon.market.component.domain.MarketBooks;
import se.lexicon.market.component.domain.MarketDeals;
import se.lexicon.market.component.domain.Markets;
import se.lexicon.market.component.entity.MarketDealEntity;
import se.lexicon.market.component.entity.MarketEntity;
import java.math.BigDecimal;
import java.util.Set;

public interface MarketComponentService {

    String DEFAULT_BEAN_NAME = "marketComponentService";

    @Broadcast(reducer = InstrumentRemoteResultReducer.class)
    Set<String> getInstruments(String ssn);

    Markets getMarkets(@Routing String Instrument, String ssn);

    MarketDeals getMarketDeals (@Routing("getInstrument") MarketEntity marketEntity);

    void placeMarket(@Routing("getInstrument") Market market);

    void MatchMarket (@Routing("getInstrument") MarketEntity marketEntity);

    @Broadcast(reducer = BigDecimalRemoteResultReducer.class)
    BigDecimal getTotalMarketValueOfAllMarkets();

}
