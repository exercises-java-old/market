package se.lexicon.market.api.client;

import se.lexicon.market.MarketApiServiceGrpc;

public class MarketApiClientImpl implements MarketApiClient{

    private MarketApiServiceGrpc.MarketApiServiceBlockingStub marketService;

    public MarketApiClientImpl(MarketApiServiceGrpc.MarketApiServiceBlockingStub marketService) {
        this.marketService = marketService;
    }

    @Override
    public void createMarket() {


        //marketService.createApplication()

    }
}
