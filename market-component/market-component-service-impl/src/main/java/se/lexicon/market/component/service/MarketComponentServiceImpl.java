package se.lexicon.market.component.service;

import com.so4it.common.logger.Logger;
import com.so4it.common.logger.LoggerFactory;
import com.so4it.queue.ParallelQueue;
import se.lexicon.market.component.domain.*;
import com.so4it.common.util.object.Required;
import com.so4it.gs.rpc.ServiceExport;
import se.lexicon.market.component.entity.MarketOrderEntity;
import se.lexicon.market.component.event.PlaceMarketOrderEvent;
import se.lexicon.market.componment.dao.MarketOrderDao;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@ServiceExport({MarketComponentService.class})
public class MarketComponentServiceImpl implements MarketComponentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketComponentServiceImpl.class);

    private MarketOrderDao marketOrderDao;

    private ParallelQueue<PlaceMarketOrderEvent> marketOrderParallelQueue;


    public MarketComponentServiceImpl(MarketOrderDao marketOrderDao, ParallelQueue<PlaceMarketOrderEvent> marketOrderParallelQueue) {

        this.marketOrderDao = Required.notNull(marketOrderDao,"marketOrderDao");
        this.marketOrderParallelQueue = Required.notNull(marketOrderParallelQueue,"marketOrderParallelQueue");

     }

    public Set<String> getInstruments(String ssn) {

        Map<String, String> instuments = new HashMap<>();
        marketOrderDao.readAll(MarketOrderEntity.templateBuilder().withSsn(ssn).build()).stream()
                .forEach(item -> instuments.put(item.getInstrument(),item.getInstrument()));

        return instuments.values().stream().collect(Collectors.toSet());
    };

    @Override
    public MarketOrders getMarketOrders(String instrument, String ssn) {

        return MarketOrders.valueOf(marketOrderDao.readAll(MarketOrderEntity.templateBuilder().withSsn(ssn).withInstrument(instrument).build()).stream().
                map(entity -> MarketOrder.builder()
                        .withId(entity.getId())
                        .withSsn(ssn)
                        .withOrderId(entity.getOrderId())
                        .withAmount(entity.getAmount())
                        .withInstrument(entity.getInstrument())
                        .withNoOfItems(entity.getNoOfItems())
                        .withMinMaxValue(entity.getMinMaxValue())
                        .withSide(entity.getSide())
                        .withOrderPriceType(entity.getOrderPriceType())
                        .withInsertionTimestamp(entity.getInsertionTimestamp())
                        .withOrderBookId(entity.getOrderBookId())
                        .build()).collect(Collectors.toSet()));
    }

    @Override
    public Boolean placeMarketOrder(MarketOrder marketOrder) {

        LOGGER.info("placeMarketOrder<offer>: " + marketOrder);

        //System.out.println("MarketOrderComponentServiceImpl.placeMarketOrder: " + marketOrder);

        Boolean offerOk = marketOrderParallelQueue.offer(PlaceMarketOrderEvent.builder()
                         //.withId(marketOrder.getId())
                        .withInstrument(marketOrder.getInstrument())
                        .withCounter(1)
                        .withMarketOrder(marketOrder)
                        .build());

//        Boolean offerOk = orderParallelQueue.offer(PlaceMarketOrderEvent.builder()
//                .withInstrument(marketOrder.getInstrument())
//                .withOrderEntity(MarketOrderEntity.builder()
//                        .withId(marketOrder.getId())
//                        .withSsn(marketOrder.getSsn())
//                        .withOrderId(marketOrder.getOrderId())
//                        .withAmount(marketOrder.getAmount())
//                        .withInstrument(marketOrder.getInstrument())
//                        .withNoOfItems(marketOrder.getNoOfItems())
//                        .withMinMaxValue(marketOrder.getMinMaxValue())
//                        .withSide(marketOrder.getSide())
//                        .withMarketPriceType(marketOrder.getMarketPriceType())
//                        .withOrderBookId(marketOrder.getOrderBookId())
//                        .withInsertionTimestamp(marketOrder.getInsertionTimestamp())
//                        .withNoOfItemsToMatch(marketOrder.getNoOfItems())
//                        .withAllItemsMatched(false)
//                        .build())
//                .withCounter(1).build());

//        Boolean offerOk = orderParallelQueue.offer(PlaceMarketOrderEvent.builder()
//                .withInstrument(marketOrder.getInstrument())
//                .withOrder(marketOrder)
//                .withCounter(1).build());

        // PUT IN ANOTHER QUEUE and try to put it in again from there

        return offerOk;
    }

    @Override
    public BigDecimal getTotalMarketValueOfAllMarkets() { return marketOrderDao.sum(); }

}
