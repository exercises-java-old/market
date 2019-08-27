package se.lexicon.market.component.test.common.domain;

import com.so4it.common.util.object.Required;
import com.so4it.test.domain.AbstractTestBuilder;
import se.lexicon.market.component.domain.Money;
import se.lexicon.market.component.domain.MarketDeal;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public class MarketDealTestBuilder extends AbstractTestBuilder<MarketDeal> {

    private MarketDeal.Builder builder;

    public MarketDealTestBuilder(MarketDeal.Builder builder) {
        this.builder = Required.notNull(builder, "builder");
        this.builder
                //.withId("1111111111")
                .withInstrument("ABB")
                .withNoOfItems(100)
                .withPrice(Money.builder()
                    .withAmount((BigDecimal.valueOf(50d)))
                    .withCurrency(Currency.getInstance("SEK"))
                    .build())
                .withOrderId1("222222222")
                .withOrderId2("333333333")
                .build();
    }

    public static MarketDealTestBuilder builder() {
        return new MarketDealTestBuilder(MarketDeal.builder());
    }


    public MarketDealTestBuilder withId(String id){
        builder.withId(id);
        return this;
    }

    public MarketDealTestBuilder withInstrument(String instrument){
        builder.withInstrument(instrument);
        return this;
    }

    public MarketDealTestBuilder withNoOfItems(Integer noOfItems){
        builder.withNoOfItems(noOfItems);
        return this;
    }

    public MarketDealTestBuilder withPrice(Money money){
        builder.withPrice(money);
        return this;
    }


    public MarketDealTestBuilder withOrderId1(String orderId1){
        builder.withOrderId1(orderId1);
        return this;
    }

    public MarketDealTestBuilder withOrderId2(String orderId2){
        builder.withOrderId2(orderId2);
        return this;
    }


    @Override
    public MarketDeal build() {
        return builder.build();
    }
}
