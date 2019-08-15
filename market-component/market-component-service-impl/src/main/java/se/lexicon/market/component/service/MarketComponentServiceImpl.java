package se.lexicon.market.component.service;

import se.lexicon.market.component.domain.*;
import se.lexicon.market.component.entity.MarketDealEntity;
import se.lexicon.market.component.entity.MarketEntity;
import com.so4it.common.util.object.Required;
import com.so4it.gs.rpc.ServiceExport;
import se.lexicon.market.componment.dao.MarketOrderDao;
import se.lexicon.market.componment.dao.MarketDealDao;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@ServiceExport({MarketComponentService.class})
public class MarketComponentServiceImpl implements MarketComponentService {

    private MarketOrderDao marketOrderDao;
    private MarketDealDao marketDealDao;

    public MarketComponentServiceImpl(MarketOrderDao marketOrderDao, MarketDealDao marketDealDao) {

        this.marketOrderDao = Required.notNull(marketOrderDao,"marketOrderDao");
        this.marketDealDao = Required.notNull(marketDealDao,"marketDealDao");
    }

    public Set<String> getInstruments(String ssn) {

        Map<String, String> instuments = new HashMap<>();
        marketOrderDao.readAll(MarketEntity.templateBuilder().withSsn(ssn).build()).stream()
                .forEach(item -> instuments.put(item.getInstrument(),item.getInstrument()));

        return instuments.values().stream().collect(Collectors.toSet());
    };

    @Override
    public Markets getMarkets(String instrument, String ssn) {

        return Markets.valueOf(marketOrderDao.readAll(MarketEntity.templateBuilder().withSsn(ssn).withInstrument(instrument).build()).stream().
                map(entity -> Market.builder()
                        .withId(entity.getId())
                        .withSsn(ssn)
                        .withAmount(entity.getAmount())
                        .withInstrument(entity.getInstrument())
                        .withNoOfItems(entity.getNoOfItems())
                        .withMinMaxValue(entity.getMinMaxValue())
                        .withSide(entity.getSide())
                        .withMarketPriceType(entity.getMarketPriceType())
                        .withInsertionTimestamp(entity.getInsertionTimestamp())
                        .withMarketBookId(entity.getMarketBookId())
                        .withMarketDealId(getMarketDeals(entity))
                        .build()).collect(Collectors.toSet()));
    }

    @Override
    public MarketDeals getMarketDeals (MarketEntity marketEntity) {

        MarketDeals marketDeals1 = MarketDeals.valueOf(marketDealDao.readAll(
                MarketDealEntity.templateBuilder().withMarketId1(marketEntity.getId()).build()).stream()
                .map(odentity -> MarketDeal.builder()
                        .withId(odentity.getId())
                        .withInstrument(odentity.getInstrument())
                        .withNoOfItems(odentity.getNoOfItems())
                        .withPrice(odentity.getPrice())
                        .withMatchingMarketId(odentity.getMarketId2())
                        .build())
                .collect(Collectors.toSet()));

        MarketDeals marketDeals2 = MarketDeals.valueOf(marketDealDao.readAll(
                MarketDealEntity.templateBuilder().withMarketId2(marketEntity.getId()).build()).stream()
                .map(odentity -> MarketDeal.builder()
                        .withId(odentity.getId())
                        .withInstrument(odentity.getInstrument())
                        .withNoOfItems(odentity.getNoOfItems())
                        .withPrice(odentity.getPrice())
                        .withMatchingMarketId(odentity.getMarketId1())
                        .build())
                .collect(Collectors.toSet()));

        return (marketDeals1.size() != 0) ? marketDeals1 : marketDeals2;

    }

//    @Override
//    public  MarketBooks getMarketBooks (MarketEntity marketEntity) {
//        return MarketBooks.valueOf(marketBookDao.readAll(
//            MarketBookEntity.templateBuilder().withSsn(marketEntity.getSsn()).withMarketId(marketEntity.getId()).build()).stream()
//            .map(obentity -> MarketBook.builder()
//                    .withId(obentity.getId())
//                    .withInstrument(obentity.getInstrument())
//                    .withNoOfItems(obentity.getNoOfItems())
//                    .withMinMaxValue(obentity.getMinMaxValue())
//                    .withSide(obentity.getSide())
//                    .withPhase(obentity.getPhase())
//                     .build())
//            .collect(Collectors.toSet()));
//    };

    @Override
    public void placeMarket(Market market) {

        MatchMarket (marketOrderDao.insert(MarketEntity.builder()
                .withId(market.getId())
                .withSsn(market.getSsn())
                .withAmount(market.getAmount())
                .withInstrument(market.getInstrument())
                .withNoOfItems(market.getNoOfItems())
                .withMinMaxValue(market.getMinMaxValue())
                .withSide(market.getSide())
                .withMarketPriceType(market.getMarketPriceType())
                .withMarketBookId(market.getMarketBookId())
                .withInsertionTimestamp(market.getInsertionTimestamp())
                .withNoOfItemsToMatch(market.getNoOfItems())
                .withAllItemsMatched(false)
                .build()));

    }

    @Override
    public synchronized void MatchMarket (MarketEntity marketEntity) {


        // GET ALL ORDERS, FILTER AGAINST ALL OTHERS BUY/SELL with same Instrument and not fully matched
        Set<MarketEntity> marketEntities = marketOrderDao.readAll
                (MarketEntity.templateBuilder()
                        .withSide(OtherSide(marketEntity.getSide()))
                        .withInstrument(marketEntity.getInstrument())
                        .withAllItemsMatched(false)
                        .build());

        double minMaxValue = 0d;

        boolean allPossibleMatchingFound = false;
        int noOfItemsToMatch = marketEntity.getNoOfItemsToMatch();
        int noOfItemsMatched = 0;

        while (!allPossibleMatchingFound) {

            MarketEntity bestMatchingMarket = null;

            for (MarketEntity matchingMarketEntity : marketEntities) {

                minMaxValue = AmountOf(matchingMarketEntity.getMinMaxValue().getAmount().doubleValue(),
                        matchingMarketEntity.getMinMaxValue().getCurrency(),
                        marketEntity.getMinMaxValue().getCurrency());

                if (marketEntity.getSide().equals(Side.SELL) ?
                        marketEntity.getMinMaxValue().getAmount().doubleValue() <= minMaxValue :
                        marketEntity.getMinMaxValue().getAmount().doubleValue() >= minMaxValue) {

                    bestMatchingMarket = chooseEntity
                            (noOfItemsToMatch, marketEntity.getMinMaxValue().getCurrency(),
                                    bestMatchingMarket,matchingMarketEntity);

                    if (bestMatchingMarket.getNoOfItemsToMatch().equals(noOfItemsToMatch) &&
                            bestMatchingMarket.getMinMaxValue().getCurrency().equals(marketEntity.getMinMaxValue().getCurrency()))
                        break; //Full matching found, exit loop
                }

            } // loop end;

           if (bestMatchingMarket == null){
                //System.out.println("No Match found for: " + marketEntity);
                return; //No matching found, exit this procedure
            }

            // Handle the result from the seach

            int itemsRemaining = noOfItemsToMatch - bestMatchingMarket.getNoOfItemsToMatch();
            allPossibleMatchingFound = itemsRemaining <= 0;
            noOfItemsMatched = itemsRemaining > 0 ? bestMatchingMarket.getNoOfItemsToMatch() : noOfItemsToMatch;

            marketEntity = marketOrderDao.update(MarketEntity.builder()
                    .withId(marketEntity.getId())
                    .withSsn(marketEntity.getSsn())
                    .withAmount(marketEntity.getAmount())
                    .withInsertionTimestamp(marketEntity.getInsertionTimestamp())
                    .withNoOfItems(marketEntity.getNoOfItems())
                    .withSide(marketEntity.getSide())
                    .withMarketPriceType(marketEntity.getMarketPriceType())
                    .withMarketBookId(marketEntity.getMarketBookId())
                    .withMinMaxValue(marketEntity.getMinMaxValue())
                    .withInstrument(marketEntity.getInstrument())
                    .withNoOfItemsToMatch(marketEntity.getNoOfItemsToMatch() - noOfItemsMatched)
                    .withAllItemsMatched(itemsRemaining == 0)
                    .build());

            //System.out.println("Entity: " + marketEntity);

            MarketEntity marketedEntity = marketOrderDao.update(MarketEntity.builder()
                    .withId(bestMatchingMarket.getId())
                    .withSsn(bestMatchingMarket.getSsn())
                    .withAmount(bestMatchingMarket.getAmount())
                    .withInsertionTimestamp(bestMatchingMarket.getInsertionTimestamp())
                    .withNoOfItems(bestMatchingMarket.getNoOfItems())
                    .withSide(bestMatchingMarket.getSide())
                    .withMarketPriceType(bestMatchingMarket.getMarketPriceType())
                    .withMarketBookId(bestMatchingMarket.getMarketBookId())
                    .withMinMaxValue(bestMatchingMarket.getMinMaxValue())
                    .withInstrument(bestMatchingMarket.getInstrument())
                    .withNoOfItemsToMatch(bestMatchingMarket.getNoOfItemsToMatch() - noOfItemsMatched)
                    .withAllItemsMatched((bestMatchingMarket.getNoOfItemsToMatch() - noOfItemsMatched) == 0)
                    .build());

            //System.out.println("Matched with: " + marketedEntity);

            // Create initial DEAL
            MarketDealEntity marketDealEntity = marketDealDao.insert(MarketDealEntity.builder()
                .withInstrument(marketEntity.getInstrument())
                .withNoOfItems(noOfItemsToMatch)
                .withMarketId1(marketEntity.getId())
                .withMarketId2(bestMatchingMarket.getId())
                .withPrice(CalculatePrice(marketEntity, bestMatchingMarket))
                .withClosed(false)
                .build());

            //System.out.println("Deal: " + marketDealEntity);

            marketEntities.remove(bestMatchingMarket); // Do not use this entity the next round

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

    private MarketEntity chooseEntity
            (int noOfItemsToMatch, Currency inCurrency, MarketEntity current, MarketEntity compareWith) {

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

    private Money CalculatePrice (MarketEntity marketEntity1, MarketEntity marketEntity2) {
        // WE NEED to change the lastPrice later on
        if (marketEntity1.getSide() == Side.SELL)
            return CalculatePrice (marketEntity1.getMinMaxValue(), marketEntity1, marketEntity2);
        return CalculatePrice (marketEntity2.getMinMaxValue(), marketEntity2, marketEntity1);
    }

    private Money CalculatePrice (Money lastPrice, MarketEntity seller, MarketEntity buyer) {

        if (buyer.getMarketPriceType() == MarketPriceType.MARKET && seller.getMarketPriceType() == MarketPriceType.MARKET) {
            return lastPrice;
        } else if (buyer.getMarketPriceType() == MarketPriceType.MARKET && seller.getMarketPriceType() != MarketPriceType.MARKET) {
            return seller.getMinMaxValue();
        } else if (seller.getMarketPriceType() == MarketPriceType.MARKET && buyer.getMarketPriceType() != MarketPriceType.MARKET) {
            return buyer.getMinMaxValue();
        }

        // Both NOT MARKET
        if (seller.getMinMaxValue().equals(buyer.getMinMaxValue()))
            return seller.getMinMaxValue(); //Agreed price

        return lastPrice;
    }

    @Override
    public BigDecimal getTotalMarketValueOfAllMarkets() { return marketOrderDao.sum(); }

}
