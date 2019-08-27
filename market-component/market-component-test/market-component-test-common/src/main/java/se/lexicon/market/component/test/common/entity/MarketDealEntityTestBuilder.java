package se.lexicon.market.component.test.common.entity;

import com.so4it.common.util.object.Required;
import com.so4it.test.domain.AbstractTestBuilder;
import se.lexicon.market.component.domain.Money;
import se.lexicon.market.component.entity.MarketDealEntity;

import java.math.BigDecimal;
import java.util.Currency;

public class MarketDealEntityTestBuilder extends AbstractTestBuilder<MarketDealEntity>{

    private MarketDealEntity.Builder builder;

    public MarketDealEntityTestBuilder(MarketDealEntity.Builder builder){

        this.builder= Required.notNull(builder,"builder");
        this.builder
                .withId("111111111")
                .withInstrument("ABB")
                .withNoOfItems(100)
                .withOrderId1("22222222")
                .withOrderId2("33333333")
                .withPrice(Money.builder().withAmount(BigDecimal.ONE).withCurrency(Currency.getInstance("SEK")).build())
                .build();
    }

    public MarketDealEntityTestBuilder withId(String id){
        builder.withId(id);
        return this;
    }

    public MarketDealEntityTestBuilder withInstrument(String instrument){
        builder.withInstrument(instrument);
        return this;
    }

    public MarketDealEntityTestBuilder withNoOfItems(Integer noOfItems){
        builder.withNoOfItems(noOfItems);
        return this;
    }

    public MarketDealEntityTestBuilder withOrderId1(String orderId1){
        builder.withOrderId1(orderId1);
        return this;
    }

    public MarketDealEntityTestBuilder withOrderId2(String orderId2){
        builder.withOrderId2(orderId2);
        return this;
    }

    public static MarketDealEntityTestBuilder builder(){
        return new MarketDealEntityTestBuilder(MarketDealEntity.builder());
    }


    @Override
    public MarketDealEntity build() {
        return builder.build();
    }
}
