package se.lexicon.market.component.service;

import com.so4it.queue.ParallelQueueConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.lexicon.market.component.domain.OrderPriceType;
import se.lexicon.market.component.domain.Money;
import se.lexicon.market.component.domain.Side;
import se.lexicon.market.component.entity.MarketDealEntity;
import se.lexicon.market.component.entity.MarketOrderEntity;
import se.lexicon.market.component.event.PlaceMarketOrderEvent;
import se.lexicon.market.componment.dao.MarketDealDao;
import se.lexicon.market.componment.dao.MarketOrderDao;
import se.lexicon.order.api.client.OrderApiClient;
import se.lexicon.order.component.domain.OrderDeal;

import java.util.Currency;
import java.util.Set;

public class MarketOrderParallelQueueConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketOrderParallelQueueConsumer.class);

    private MarketOrderDao marketOrderDao;
    private MarketDealDao  marketDealDao;

    //private OrderApiClient orderApiClient;


    public MarketOrderParallelQueueConsumer(MarketOrderDao marketOrderDao, MarketDealDao  marketDealDao) {
        this.marketOrderDao = marketOrderDao;
        this.marketDealDao  = marketDealDao;
        //this.orderApiClient = orderApiClient;
    }

    /**
     * This will be called by the parallel queue framework guaranteeing that only one order for the same
     * accoutn id will be handled at the time
     *
     * @param placeMarketOrderEvent
     */
    @ParallelQueueConsumer
    public void placeOrder (PlaceMarketOrderEvent placeMarketOrderEvent) {

        LOGGER.info("placeOrder: " + placeMarketOrderEvent);

        //MatchMarketOrder (placeMarketOrderEvent.getMarketOrder());

        MatchMarketOrder
            (MarketOrderEntity.builder()
            //.withId(placeMarketOrderEvent.getMarketOrder().getId())
            .withOrderId(placeMarketOrderEvent.getMarketOrder().getOrderId())
            .withSsn(placeMarketOrderEvent.getMarketOrder().getSsn())
            .withAmount(placeMarketOrderEvent.getMarketOrder().getAmount())
            .withInsertionTimestamp(placeMarketOrderEvent.getMarketOrder().getInsertionTimestamp())
            .withNoOfItems(placeMarketOrderEvent.getMarketOrder().getNoOfItems())
            .withSide(placeMarketOrderEvent.getMarketOrder().getSide())
            .withOrderPriceType(placeMarketOrderEvent.getMarketOrder().getOrderPriceType())
            .withOrderBookId(placeMarketOrderEvent.getMarketOrder().getOrderBookId())
            .withMinMaxValue(placeMarketOrderEvent.getMarketOrder().getMinMaxValue())
            .withInstrument(placeMarketOrderEvent.getMarketOrder().getInstrument())
            .withNoOfItemsToMatch(placeMarketOrderEvent.getMarketOrder().getNoOfItems())
            .withAllItemsMatched(false)
            .build());
    }

    private void MatchMarketOrder (MarketOrderEntity marketOrderEntity) {


        // GET ALL ORDERS, FILTER AGAINST ALL OTHERS BUY/SELL with same Instrument and not fully matched
        Set<MarketOrderEntity> marketOrderEntities = marketOrderDao.readAll
                (MarketOrderEntity.templateBuilder()
                        .withSide(OtherSide(marketOrderEntity.getSide()))
                        .withInstrument(marketOrderEntity.getInstrument())
                        .withAllItemsMatched(false)
                        .build());

        double minMaxValue = 0d;

        boolean allPossibleMatchingFound = false;
        int noOfItemsToMatch = marketOrderEntity.getNoOfItemsToMatch();
        int noOfItemsMatched = 0;

        while (!allPossibleMatchingFound) {

            MarketOrderEntity bestMatchingMarket = null;

            for (MarketOrderEntity matchingMarketOrderEntity : marketOrderEntities) {

                minMaxValue = AmountOf(matchingMarketOrderEntity.getMinMaxValue().getAmount().doubleValue(),
                        matchingMarketOrderEntity.getMinMaxValue().getCurrency(),
                        marketOrderEntity.getMinMaxValue().getCurrency());

                if (marketOrderEntity.getSide().equals(Side.SELL) ?
                        marketOrderEntity.getMinMaxValue().getAmount().doubleValue() <= minMaxValue :
                        marketOrderEntity.getMinMaxValue().getAmount().doubleValue() >= minMaxValue) {

                    bestMatchingMarket = chooseEntity
                            (noOfItemsToMatch, marketOrderEntity.getMinMaxValue().getCurrency(),
                                    bestMatchingMarket,matchingMarketOrderEntity);

                    if (bestMatchingMarket.getNoOfItemsToMatch().equals(noOfItemsToMatch) &&
                            bestMatchingMarket.getMinMaxValue().getCurrency().equals(marketOrderEntity.getMinMaxValue().getCurrency()))
                        break; //Full matching found, exit loop
                }

            } // loop end;

            if (bestMatchingMarket == null){

                //System.out.println("No Match found for: " + marketEntity);
                marketOrderDao.insertOrUpdate(MarketOrderEntity.builder()
                        .withId(marketOrderEntity.getId())
                        .withSsn(marketOrderEntity.getSsn())
                        .withOrderId(marketOrderEntity.getOrderId())
                        .withAmount(marketOrderEntity.getAmount())
                        .withInsertionTimestamp(marketOrderEntity.getInsertionTimestamp())
                        .withNoOfItems(marketOrderEntity.getNoOfItems())
                        .withSide(marketOrderEntity.getSide())
                        .withOrderPriceType(marketOrderEntity.getOrderPriceType())
                        .withOrderBookId(marketOrderEntity.getOrderBookId())
                        .withMinMaxValue(marketOrderEntity.getMinMaxValue())
                        .withInstrument(marketOrderEntity.getInstrument())
                        .withNoOfItemsToMatch(marketOrderEntity.getNoOfItemsToMatch())
                        .withAllItemsMatched(false) // NOW ALLOWED TO BE MATCHED AGAINST NEW INCOMMING ORDERS
                        .build());

                return; //No matching found, exit this procedure
            }

            // Handle the result from the seach

            int itemsRemaining = noOfItemsToMatch - bestMatchingMarket.getNoOfItemsToMatch();
            allPossibleMatchingFound = itemsRemaining <= 0;
            noOfItemsMatched = itemsRemaining > 0 ? bestMatchingMarket.getNoOfItemsToMatch() : noOfItemsToMatch;

            marketOrderEntity = marketOrderDao.insertOrUpdate(MarketOrderEntity.builder()
                    .withId(marketOrderEntity.getId())
                    .withSsn(marketOrderEntity.getSsn())
                    .withOrderId(marketOrderEntity.getOrderId())
                    .withAmount(marketOrderEntity.getAmount())
                    .withInsertionTimestamp(marketOrderEntity.getInsertionTimestamp())
                    .withNoOfItems(marketOrderEntity.getNoOfItems())
                    .withSide(marketOrderEntity.getSide())
                    .withOrderPriceType(marketOrderEntity.getOrderPriceType())
                    .withOrderBookId(marketOrderEntity.getOrderBookId())
                    .withMinMaxValue(marketOrderEntity.getMinMaxValue())
                    .withInstrument(marketOrderEntity.getInstrument())
                    .withNoOfItemsToMatch(marketOrderEntity.getNoOfItemsToMatch() - noOfItemsMatched)
                    .withAllItemsMatched(itemsRemaining == 0)
                    .build());

            //System.out.println("Entity: " + marketEntity);

            MarketOrderEntity bestMatchingEntity = marketOrderDao.update(MarketOrderEntity.builder()
                    .withId(bestMatchingMarket.getId())
                    .withSsn(bestMatchingMarket.getSsn())
                    .withOrderId(bestMatchingMarket.getOrderId())
                    .withAmount(bestMatchingMarket.getAmount())
                    .withInsertionTimestamp(bestMatchingMarket.getInsertionTimestamp())
                    .withNoOfItems(bestMatchingMarket.getNoOfItems())
                    .withSide(bestMatchingMarket.getSide())
                    .withOrderPriceType(bestMatchingMarket.getOrderPriceType())
                    .withOrderBookId(bestMatchingMarket.getOrderBookId())
                    .withMinMaxValue(bestMatchingMarket.getMinMaxValue())
                    .withInstrument(bestMatchingMarket.getInstrument())
                    .withNoOfItemsToMatch(bestMatchingMarket.getNoOfItemsToMatch() - noOfItemsMatched)
                    .withAllItemsMatched((bestMatchingMarket.getNoOfItemsToMatch() - noOfItemsMatched) == 0)
                    .build());

            //System.out.println("Matched with: " + marketedEntity);

            // Send the suggested DEAL back to order
            Money agreedPrice = CalculatePrice(marketOrderEntity, bestMatchingMarket);

            marketDealDao.insert(MarketDealEntity.builder()
                    .withInstrument(marketOrderEntity.getInstrument())
                    .withNoOfItems(noOfItemsToMatch)
                    .withPrice(agreedPrice)
                    .withOrderId1(marketOrderEntity.getOrderId())
                    .withOrderId2(bestMatchingMarket.getOrderId())
                    .build());

//            orderApiClient.makeDeal(OrderDeal.builder()
//                    .withSsn(marketOrderEntity.getSsn())
//                    .withOrderId(marketOrderEntity.getOrderId())
//                    .withInstrument(marketOrderEntity.getInstrument())
//                    .withNoOfItems(noOfItemsToMatch)
//                    .withPrice(mapMoney(agreedPrice))
//                    .build());
//
//            orderApiClient.makeDeal(OrderDeal.builder()
//                    .withSsn(bestMatchingMarket.getSsn())
//                    .withOrderId(bestMatchingMarket.getOrderId())
//                    .withInstrument(bestMatchingMarket.getInstrument())
//                    .withNoOfItems(noOfItemsToMatch)
//                    .withPrice(mapMoney(agreedPrice))
//                    .build());

            marketOrderEntities.remove(bestMatchingMarket); // Do not use this entity the next round

            noOfItemsToMatch = itemsRemaining;

        } // While loop
    }

    private double AmountOf(double amount, Currency fromCurrency, Currency toCurrency) {
        if (fromCurrency == toCurrency) return amount;
        // NOT SAME CURRACY, CONVERT IT TO toCurrency
        return amount; // * ConvertionFactor(fromCurrency,toCurrency)
    }

    private Side OtherSide(Side side) {
        if (side == Side.BUY) return Side.SELL;
        return Side.BUY;
    }

    private MarketOrderEntity chooseEntity
            (int noOfItemsToMatch, Currency inCurrency, MarketOrderEntity current, MarketOrderEntity compareWith) {

        if (current == null) return compareWith;

        if (current.getMinMaxValue().getCurrency().equals(compareWith.getMinMaxValue().getCurrency())) { // same currency

            if (current.getNoOfItemsToMatch().equals(noOfItemsToMatch)) return current;
            if (compareWith.getNoOfItemsToMatch().equals(noOfItemsToMatch)) return compareWith;
            if (current.getNoOfItemsToMatch() >= compareWith.getNoOfItemsToMatch()) return current;
            return compareWith;

        }

        // Otherwise, always choose the same currency as in the market
        if (inCurrency.equals(current.getMinMaxValue().getCurrency())) return current;
        return compareWith;

    }

    private Money CalculatePrice (MarketOrderEntity marketEntity1, MarketOrderEntity marketEntity2) {
        // WE NEED to change the lastPrice later on
        if (marketEntity1.getSide() == Side.SELL)
            return CalculatePrice (marketEntity1.getMinMaxValue(), marketEntity1, marketEntity2);
        return CalculatePrice (marketEntity2.getMinMaxValue(), marketEntity2, marketEntity1);
    }

    private Money CalculatePrice (Money lastPrice, MarketOrderEntity seller, MarketOrderEntity buyer) {

        if (buyer.getOrderPriceType() == OrderPriceType.MARKET && seller.getOrderPriceType() == OrderPriceType.MARKET) {
            return lastPrice;
        } else if (buyer.getOrderPriceType() == OrderPriceType.MARKET && seller.getOrderPriceType() != OrderPriceType.MARKET) {
            return seller.getMinMaxValue();
        } else if (seller.getOrderPriceType() == OrderPriceType.MARKET && buyer.getOrderPriceType() != OrderPriceType.MARKET) {
            return buyer.getMinMaxValue();
        }

        // Both NOT MARKET
        if (seller.getMinMaxValue().equals(buyer.getMinMaxValue()))
            return seller.getMinMaxValue(); //Agreed price

        return lastPrice;
    }

    private se.lexicon.order.component.domain.Money mapMoney (Money money){
        return se.lexicon.order.component.domain.Money.builder()
                .withAmount(money.getAmount())
                .withCurrency(money.getCurrency())
                .build();
    }

}
