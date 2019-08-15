package se.lexicon.market.component.test.common.entity;

import se.lexicon.market.component.domain.Money;
import se.lexicon.market.component.domain.MarketPriceType;
import se.lexicon.market.component.domain.Side;
import se.lexicon.market.component.entity.MarketEntity;
import com.so4it.common.util.object.Required;
import com.so4it.test.domain.AbstractTestBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public class MarketEntityTestBuilder extends AbstractTestBuilder<MarketEntity> {

    private MarketEntity.Builder builder;


    public MarketEntityTestBuilder(MarketEntity.Builder builder) {
        this.builder = Required.notNull(builder, "builder");
        this.builder
                //.withId("1111111111")
                .withSsn("1111111111")
                .withInsertionTimestamp(Instant.now())
                .withAmount(BigDecimal.TEN)
                .withInstrument("ABB")
                .withNoOfItems(100)
                .withMinMaxValue(Money.builder()
                        .withAmount((BigDecimal.valueOf(50d)))
                        .withCurrency(Currency.getInstance("SEK"))
                        .build())
                .withSide(Side.BUY)
                .withMarketPriceType(MarketPriceType.MARKET)
                .withMarketBookId("ABB/SEK")
                .withNoOfItemsToMatch(100)
                .withAllItemsMatched(false);
    }

    public MarketEntityTestBuilder withSsn(String ssn){
        builder.withSsn(ssn);
        return this;
    }

    public MarketEntityTestBuilder withAmount(BigDecimal amount){
        builder.withAmount(amount);
        return this;
    }

    public MarketEntityTestBuilder withInstrument(String instrument){
        builder.withInstrument(instrument);
        return this;
    }

    public MarketEntityTestBuilder withNoOfItems(Integer noOfItems){
        builder.withNoOfItems(noOfItems);
        return this;
    }

     public MarketEntityTestBuilder withMinMaxValue(Money money){
        builder.withMinMaxValue(money);
        return this;
    }

    public MarketEntityTestBuilder withSide(Side side){
        builder.withSide(side);
        return this;
    }
    public MarketEntityTestBuilder withMarketPriceType(MarketPriceType marketPriceType){
        builder.withMarketPriceType(marketPriceType);
        return this;
    }

    public MarketEntityTestBuilder withMarketBookId(String marketBookId){
        builder.withMarketBookId(marketBookId);
        return this;
    }

    public MarketEntityTestBuilder withNoOfMatchedItems(Integer noOfMatchedItems){
        builder.withNoOfItemsToMatch(noOfMatchedItems);
        return this;
    }

    public MarketEntityTestBuilder withAllItemsMatched(Boolean allItemsMatched){
        builder.withAllItemsMatched(allItemsMatched);
        return this;
    }


    public static MarketEntityTestBuilder builder() {
        return new MarketEntityTestBuilder(MarketEntity.builder());
    }

    @Override
    public MarketEntity build() {
        return builder.build();
    }
}
