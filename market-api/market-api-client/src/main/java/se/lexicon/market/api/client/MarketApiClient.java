package se.lexicon.market.api.client;

import se.lexicon.market.component.domain.MarketOrder;

public interface MarketApiClient {

    String DEFAULT_API_BEAN_NAME = "marketApiClient";

    boolean placeMarketOrder(MarketOrder marketOrder);
}
