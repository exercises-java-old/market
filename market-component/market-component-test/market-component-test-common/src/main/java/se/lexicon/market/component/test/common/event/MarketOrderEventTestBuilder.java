package se.lexicon.market.component.test.common.event;

import com.so4it.common.util.object.Required;
import com.so4it.test.domain.AbstractTestBuilder;
import se.lexicon.market.component.domain.Money;
import se.lexicon.market.component.domain.OrderPriceType;
import se.lexicon.market.component.domain.Side;
import se.lexicon.market.component.event.PlaceMarketOrderEvent;
import se.lexicon.market.component.test.common.domain.MarketOrderTestBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public class MarketOrderEventTestBuilder extends AbstractTestBuilder<PlaceMarketOrderEvent> {

    private PlaceMarketOrderEvent.Builder builder;


    public MarketOrderEventTestBuilder(PlaceMarketOrderEvent.Builder builder) {
        this.builder = Required.notNull(builder, "builder");
        this.builder
                //.withId("1111111111")
                .withInstrument("ABB")
                .withCounter(1)
                .withMarketOrder(MarketOrderTestBuilder.builder().build());
    }

    public MarketOrderEventTestBuilder withInstrument(String instrument){
        builder.withInstrument(instrument);
        return this;
    }

    public static MarketOrderEventTestBuilder builder() {
        return new MarketOrderEventTestBuilder(PlaceMarketOrderEvent.builder());
    }

    @Override
    public PlaceMarketOrderEvent build() {
        return builder.build();
    }
}
