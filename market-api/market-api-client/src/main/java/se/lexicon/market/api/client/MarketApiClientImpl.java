package se.lexicon.market.api.client;

import com.google.common.util.concurrent.ListenableFuture;
import com.so4it.metric.springframework.MetricsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.lexicon.market.MarketApiServiceGrpc;
import se.lexicon.market.PlaceMarketOrderRequest;
import se.lexicon.market.PlaceMarketOrderResponse;
import se.lexicon.market.component.domain.MarketOrder;
import se.lexicon.market.component.domain.Money;
import se.lexicon.market.component.domain.OrderPriceType;
import se.lexicon.market.component.domain.Side;

@MetricsBean(name = "MarketApiClient")
public class MarketApiClientImpl implements MarketApiClient{

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketApiClientImpl.class);

    private MarketApiServiceGrpc.MarketApiServiceFutureStub marketApiService;

    public MarketApiClientImpl(MarketApiServiceGrpc.MarketApiServiceFutureStub marketApiService) {
        this.marketApiService = marketApiService;
    }

    @Override
    public boolean placeMarketOrder(MarketOrder marketOrder) {

        LOGGER.info("placeMarketOrder:" + marketOrder);

        ListenableFuture<PlaceMarketOrderResponse> response = marketApiService.placeMarketOrder(PlaceMarketOrderRequest.newBuilder()
              //.setId(marketOrder.getId())
              .setSsn(marketOrder.getSsn())
              .setOrderid(marketOrder.getOrderId())
              .setAmount(marketOrder.getAmount().floatValue())
              .setInstrument(marketOrder.getInstrument())
              .setNoOfItems(marketOrder.getNoOfItems())
              .setMinMaxValue(mapMoney(marketOrder.getMinMaxValue()))
              .setSide(mapSide(marketOrder.getSide()))
              .setOrderPriceType(mapOrderPrice(marketOrder.getOrderPriceType()))
              .build());

        return true;
        //return response.getOk();
    }

    // 0 = BUY, 1 = SELL
    private se.lexicon.market.Side mapSide (Side value) {
        if (value == Side.BUY) return se.lexicon.market.Side.BUY;
        return se.lexicon.market.Side.SELL;
    }

    private se.lexicon.market.OrderPriceType mapOrderPrice (OrderPriceType value) {

        if (value == OrderPriceType.MARKET) return se.lexicon.market.OrderPriceType.MARKET;
        if (value == OrderPriceType.LIMIT) return se.lexicon.market.OrderPriceType.LIMIT;
        return se.lexicon.market.OrderPriceType.FULL_LIMIT;
    }

    private se.lexicon.market.Money mapMoney (Money money){
        return se.lexicon.market.Money.newBuilder()
                .setAmount(money.getAmount().floatValue())
                .setCurrency(money.getCurrency().getCurrencyCode())
                .build();
    }

}
