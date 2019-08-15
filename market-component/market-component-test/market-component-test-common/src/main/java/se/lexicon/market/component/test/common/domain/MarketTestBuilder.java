package se.lexicon.market.component.test.common.domain;

import se.lexicon.market.component.domain.*;
import com.so4it.common.util.object.Required;
import com.so4it.test.domain.AbstractTestBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public class MarketTestBuilder extends AbstractTestBuilder<Market> {

    private Market.Builder builder;

    public MarketTestBuilder(Market.Builder builder) {
        this.builder = Required.notNull(builder, "builder");
        this.builder
                //.withId("1111111111")
                .withSsn("1111111111")
                .withAmount(BigDecimal.TEN)
                .withInstrument("ABB")
                .withNoOfItems(100)
                .withSide(Side.BUY)
                .withMinMaxValue(Money.builder()
                    .withAmount((BigDecimal.valueOf(50d)))
                    .withCurrency(Currency.getInstance("SEK"))
                    .build())
                .withInsertionTimestamp(Instant.now())
                .withMarketBookId("ABB/SEK")
                .withMarketPriceType(MarketPriceType.MARKET)

//                .withMarketBookId (MarketBooks.valueOf(
//                        new MarketBook.Builder()
//                            //.withId("1111111111")
//                            .withInstrument("ABB")
//                            .withNoOfItems(100)
//                            .withSide(Side.BUY)
//                            .withPhase(Phase.UNKNOWN)
//                            .withMinMaxValue(Money.builder()
//                                .withAmount((BigDecimal.valueOf(500d)))
//                                .withCurrency(Currency.getInstance("SEK"))
//                                .build())
                            .build();
    }

    public static MarketTestBuilder builder() {
        return new MarketTestBuilder(Market.builder());
    }


    public MarketTestBuilder withId(String id){
        builder.withId(id);
        return this;
    }

    public MarketTestBuilder withSsn(String ssn){
        builder.withSsn(ssn);
        return this;
    }

    public MarketTestBuilder withInstrument(String instrument){
        builder.withInstrument(instrument);
        return this;
    }

    public MarketTestBuilder withNoOfItems(Integer noOfItems){
        builder.withNoOfItems(noOfItems);
        return this;
    }

    public MarketTestBuilder withMinMaxValue(Money money){
        builder.withMinMaxValue(money);
        return this;
    }

    public MarketTestBuilder withSide(Side side){
        builder.withSide(side);
        return this;
    }

    public MarketTestBuilder withMarketPriceType(MarketPriceType marketPriceType){
        builder.withMarketPriceType(marketPriceType);
        return this;
    }

    public MarketTestBuilder withAmount(BigDecimal amount){
        builder.withAmount(amount);
        return this;
    }

    public MarketTestBuilder withInsertionTimestamp(Instant insertionTimestamp){
        builder.withInsertionTimestamp(insertionTimestamp);
        return this;
    }

    public MarketTestBuilder withMarketBookId(String marketBookId){
        builder.withMarketBookId(marketBookId);
        return this;
    }


    @Override
    public Market build() {
        return builder.build();
    }
}
