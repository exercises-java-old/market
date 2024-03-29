package se.lexicon.market.api.server;


import com.so4it.api.ApiServiceProvider;
import com.so4it.api.Market;
import com.so4it.api.util.StreamObserverErrorHandler;
import com.so4it.common.util.object.Required;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.lexicon.market.MarketApiServiceGrpc;
import se.lexicon.market.PlaceMarketOrderRequest;
import se.lexicon.market.PlaceMarketOrderResponse;
import se.lexicon.market.component.client.MarketComponentClient;
import se.lexicon.market.component.domain.MarketOrder;
import se.lexicon.market.component.domain.Money;
import se.lexicon.market.component.domain.OrderPriceType;
import se.lexicon.market.component.domain.Side;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@ApiServiceProvider(
        value = Market.NAME,
        version = Market.VERSION,
        properties = Market.PROPERTIES,
        specification = Market.PATH,
        specificationPackageName = Market.PACKAGE_NAME,
        specificationServiceNames = Market.SERVICE_NAMES)
public class MarketApiServiceImpl extends MarketApiServiceGrpc.MarketApiServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketApiServiceImpl.class);

    private MarketComponentClient marketComponentClient;

    public MarketApiServiceImpl(MarketComponentClient marketComponentClient) {
        this.marketComponentClient = Required.notNull(marketComponentClient,"marketOrderComponentClient");
    }

    @Override
    public void placeMarketOrder(PlaceMarketOrderRequest request, StreamObserver<PlaceMarketOrderResponse> responseObserver) {
        StreamObserverErrorHandler.of(responseObserver).onError(() -> {

            LOGGER.info("placeMarketOrder: " + request);

            //if (request.getSerializedSize() = )
            Boolean Ok = marketComponentClient.placeMarketOrder
                    (MarketOrder.builder()
                            //.withId(request.getId())
                            .withSsn(request.getSsn())
                            .withOrderId(request.getOrderid())
                            .withAmount(BigDecimal.valueOf(request.getAmount()))
                            .withInsertionTimestamp(Instant.now())
                            .withInstrument(request.getInstrument())
                            .withNoOfItems(request.getNoOfItems())
                            .withOrderBookId(request.getOrderBookId())
                            .withSide(mapSide(request.getSide()))
                            .withOrderPriceType(mapOrderPrice(request.getOrderPriceType()))
                            .withMinMaxValue(mapMoney (request.getMinMaxValue()))
                            .build());

            responseObserver.onNext(PlaceMarketOrderResponse.newBuilder().setOk(Ok).build());
            responseObserver.onCompleted();
        }, "Failed creating market request order");
    }

    private Side mapSide (se.lexicon.market.Side value) {
        if (value == se.lexicon.market.Side.BUY) return Side.BUY;
        return Side.SELL;
    }

    private OrderPriceType mapOrderPrice (se.lexicon.market.OrderPriceType value) {

        if (value == se.lexicon.market.OrderPriceType.MARKET) return OrderPriceType.MARKET;
        if (value == se.lexicon.market.OrderPriceType.LIMIT) return OrderPriceType.LIMIT;
        return OrderPriceType.FULL_LIMIT;
    }

    private Money mapMoney (se.lexicon.market.Money money){
        return Money.builder()
                .withAmount(BigDecimal.valueOf(money.getAmount()))
                .withCurrency(Currency.getInstance(money.getCurrency()))
                .build();
    }

}
