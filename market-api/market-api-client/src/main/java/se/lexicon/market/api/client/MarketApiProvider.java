package se.lexicon.market.api.client;

import com.so4it.api.*;

/**
 *
 * @author Stephan Köhler {@literal <mailto:stephan.kohler@so4it.com/>}
 */
@ApiClientProvider(value = Market.NAME,
        specification = Market.PATH,
        version = Market.VERSION,
        beanPublisher = MarketApiProviderBeanPublisher.class)


public class MarketApiProvider {
}
