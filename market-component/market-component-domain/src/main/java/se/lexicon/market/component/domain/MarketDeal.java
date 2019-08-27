package se.lexicon.market.component.domain;

import com.so4it.annotation.Allowed;
import com.so4it.common.util.object.Required;
import com.so4it.common.util.object.ValueObject;

import java.io.Serializable;

public class MarketDeal extends ValueObject implements Serializable {

    private static final long serialVersionUID = 2L;

    @Allowed(types = {Allowed.Type.NULLABLE})
    private String id;

    private String orderId1;
    private String orderId2;

    private String instrument;

    private Integer noOfItems;

    private Money price;

    private MarketDeal() {
    }

    private MarketDeal(Builder builder) {
        this.id = builder.id;
        this.orderId1 = Required.notNull(builder.orderId1,"orderId1");
        this.orderId2 = Required.notNull(builder.orderId2,"orderId2");
        this.instrument = Required.notNull(builder.instrument,"instrument");
        this.noOfItems = Required.notNull(builder.noOfItems,"noOfItems");
        this.price = Required.notNull(builder.price,"price");
    }

    public String getId() {
        return id;
    }

    public String getInstrument() {
        return instrument;
    }

    public Integer getNoOfItems() {
        return noOfItems;
    }

    public Money getPrice() { return price; }

    public String getOrderId1() {
        return orderId1;
    }
    public String getOrderId2() {
        return orderId2;
    }

    @Override
    protected Object[] getIdFields() {
        return new Object[]{id, orderId1, orderId2, instrument, noOfItems, price};
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder implements com.so4it.common.builder.Builder<MarketDeal>{

        private String id;

        private String orderId1;

        private String orderId2;

        private String instrument;

        private Integer noOfItems;

        private Money price;

        public Builder withId(String id){
            this.id = id;
            return this;
        }

        public Builder withOrderId1(String orderId1){
            this.orderId1 = orderId1;
            return this;
        }

        public Builder withOrderId2(String orderId2){
            this.orderId2 = orderId2;
            return this;
        }

        public Builder withInstrument(String instrument){
            this.instrument = instrument;
            return this;
        }

        public Builder withNoOfItems(Integer noOfItems){
            this.noOfItems = noOfItems;
            return this;
        }

        public Builder withPrice(Money price){
            this.price = price;
            return this;
        }

        @Override
        public MarketDeal build() {
            return new MarketDeal(this);
        }
    }
}

