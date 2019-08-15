package se.lexicon.market.component.test.integration.service;

import com.so4it.test.category.IntegrationTest;
import com.so4it.test.common.Assert;
import com.so4it.test.gs.rule.ClearGigaSpaceTestRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.RuleChain;
import org.openspaces.core.GigaSpace;
import se.lexicon.market.component.domain.*;
import se.lexicon.market.component.service.MarketComponentService;
import se.lexicon.market.component.test.common.domain.MarketTestBuilder;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
@Category(IntegrationTest.class)
public class MarketComponentServiceIntegrationTest {

    @ClassRule
    public static final RuleChain SUITE_RULE_CHAIN = MarketComponentServiceIntegrationTestSuite.SUITE_RULE_CHAIN;

    @Rule
    public ClearGigaSpaceTestRule clearGigaSpaceTestRule = new ClearGigaSpaceTestRule(MarketComponentServiceIntegrationTestSuite.getExportContext().getBean(GigaSpace.class));

    @Test
    public void testMarketComponentServiceExists() {
        MarketComponentService marketComponentService = MarketComponentServiceIntegrationTestSuite.getImportContext().getBean(MarketComponentService.class);
        Assert.assertNotNull(marketComponentService);
    }

    @Test
    public void testPlaceMarket() {
        //Set<Currency> currencies = Currency.getAvailableCurrencies();
        //System.out.println(currencies);

        MarketComponentService marketComponentService = MarketComponentServiceIntegrationTestSuite.getImportContext().getBean(MarketComponentService.class);

        Market market = MarketTestBuilder.builder().withSsn("111111").withInstrument("ABB").withAmount(BigDecimal.ONE).build();
        marketComponentService.placeMarket(market);
        Markets markets = marketComponentService.getMarkets("ABB","111111");

        Assert.assertEquals(1, markets.size());
        Assert.assertEquals(market, markets.getFirst());

    }

    @Test
    public void testPlaceTwoMarkets() {
        MarketComponentService marketComponentService = MarketComponentServiceIntegrationTestSuite.getImportContext().getBean(MarketComponentService.class);

        Market market1 = MarketTestBuilder.builder().withSsn("111222").withInstrument("ABB").withAmount(BigDecimal.ONE).build();
        Market market2 = MarketTestBuilder.builder().withSsn("111222").withInstrument("ABB").withAmount(BigDecimal.TEN).build();

        marketComponentService.placeMarket(market1);
        marketComponentService.placeMarket(market2);

        Markets markets = marketComponentService.getMarkets("ABB","111222");

        Assert.assertEquals(2, markets.size());
    }

    @Test
    public void testMatchMarket() {
        MarketComponentService marketComponentService = MarketComponentServiceIntegrationTestSuite.getImportContext().getBean(MarketComponentService.class);

        Market market1 = MarketTestBuilder.builder()
                //.withId("111111")
                .withSsn("111111")
                .withInstrument("ABB")
                .withNoOfItems(100)
                .withSide(Side.BUY)
                .withMinMaxValue(Money.builder()
                    .withAmount((BigDecimal.valueOf(550d)))
                    .withCurrency(Currency.getInstance("SEK"))
                    .build())
                .build();

        Market market2 = MarketTestBuilder.builder()
                //.withId("222222")
                .withSsn("222222")
                .withInstrument("ABB")
                .withNoOfItems(100)
                .withSide(Side.SELL)
                .withMinMaxValue(Money.builder()
                    .withAmount((BigDecimal.valueOf(500d)))
                    .withCurrency(Currency.getInstance("SEK"))
                    .build())
                .build();

        Market market3 = MarketTestBuilder.builder()
                //.withId("333333")
                .withSsn("333333")
                .withInstrument("ABB")
                .withNoOfItems(50)
                .withSide(Side.SELL)
                .withMinMaxValue(Money.builder()
                    .withAmount((BigDecimal.valueOf(480d)))
                    .withCurrency(Currency.getInstance("SEK"))
                    .build())
                .build();

        Market market4 = MarketTestBuilder.builder()
                //.withId("444444")
                .withSsn("444444")
                .withInstrument("ABB")
                .withNoOfItems(50)
                .withSide(Side.SELL)
                .withMinMaxValue(Money.builder()
                    .withAmount((BigDecimal.valueOf(490d)))
                    .withCurrency(Currency.getInstance("SEK"))
                    .build())
                .build();

        Market market5 = MarketTestBuilder.builder()
                //.withId("555555")
                .withSsn("555555")
                .withInstrument("ABB")
                .withNoOfItems(100)
                .withSide(Side.BUY)
                .withMinMaxValue(Money.builder()
                    .withAmount((BigDecimal.valueOf(500d)))
                    .withCurrency(Currency.getInstance("SEK"))
                    .build())
                .build();

        marketComponentService.placeMarket(market1);
        marketComponentService.placeMarket(market2);
        marketComponentService.placeMarket(market3);
        marketComponentService.placeMarket(market4);
        marketComponentService.placeMarket(market5);

        Markets markets1 = marketComponentService.getMarkets("ABB","111111");
        Markets markets2 = marketComponentService.getMarkets("ABB","222222");
        Markets markets3 = marketComponentService.getMarkets("ABB","333333");
        Markets markets4 = marketComponentService.getMarkets("ABB","444444");
        Markets markets5 = marketComponentService.getMarkets("ABB","555555");

        Assert.assertEquals(1, markets1.getFirst().getMarketDeals().size());
        Assert.assertEquals(1, markets2.getFirst().getMarketDeals().size());
        Assert.assertEquals(1, markets3.getFirst().getMarketDeals().size());
        Assert.assertEquals(1, markets4.getFirst().getMarketDeals().size());
        Assert.assertEquals(2, markets5.getFirst().getMarketDeals().size());

    }

    @Test
    public void testGetAllMarketComponent() {
        MarketComponentService marketComponentService = MarketComponentServiceIntegrationTestSuite.getImportContext().getBean(MarketComponentService.class);

        marketComponentService.placeMarket(MarketTestBuilder.builder().withSsn("111111").withInstrument("ABB").withAmount(BigDecimal.ONE).build());
        marketComponentService.placeMarket(MarketTestBuilder.builder().withSsn("111111").withInstrument("ABB").withAmount(BigDecimal.TEN).build());
        marketComponentService.placeMarket(MarketTestBuilder.builder().withSsn("111111").withInstrument("SAAB").withAmount(BigDecimal.TEN).build());

        Markets markets1 = marketComponentService.getMarkets("ABB","111111");
        Assert.assertEquals(2, markets1.size());

        Markets markets2 = marketComponentService.getMarkets("SAAB","111111");
        Assert.assertEquals(1, markets2.size());

    }

    @Test
    public void testGetInstruments() {
        MarketComponentService marketComponentService = MarketComponentServiceIntegrationTestSuite.getImportContext().getBean(MarketComponentService.class);

        marketComponentService.placeMarket(MarketTestBuilder.builder().withSsn("111111").withInstrument("ABB").withAmount(BigDecimal.ONE).build());
        marketComponentService.placeMarket(MarketTestBuilder.builder().withSsn("111111").withInstrument("ABB").withAmount(BigDecimal.TEN).build());
        marketComponentService.placeMarket(MarketTestBuilder.builder().withSsn("111111").withInstrument("SAAB").withAmount(BigDecimal.TEN).build());

        marketComponentService.placeMarket(MarketTestBuilder.builder().withSsn("222222").withInstrument("ABB").withAmount(BigDecimal.ONE).build());
        marketComponentService.placeMarket(MarketTestBuilder.builder().withSsn("222222").withInstrument("SAAB").withAmount(BigDecimal.TEN).build());
        marketComponentService.placeMarket(MarketTestBuilder.builder().withSsn("222222").withInstrument("ERICSSON").withAmount(BigDecimal.TEN).build());
        marketComponentService.placeMarket(MarketTestBuilder.builder().withSsn("222222").withInstrument("ABB").withAmount(BigDecimal.TEN).build());

        Assert.assertEquals(3, marketComponentService.getInstruments("222222").size());
        Assert.assertEquals(2, marketComponentService.getInstruments("111111").size());

        Assert.assertEquals("[ABB, SAAB, ERICSSON]", marketComponentService.getInstruments("222222").toString());
        Assert.assertEquals("[ABB, SAAB]", marketComponentService.getInstruments("111111").toString());

    }

    @Test
    public void testGetTotalMarketValue() {
        MarketComponentService marketComponentService = MarketComponentServiceIntegrationTestSuite.getImportContext().getBean(MarketComponentService.class);

        marketComponentService.placeMarket(MarketTestBuilder.builder().withSsn("111111").withInstrument("ABB").withAmount(BigDecimal.ONE).build());
        marketComponentService.placeMarket(MarketTestBuilder.builder().withSsn("111111").withInstrument("ABB").withAmount(BigDecimal.TEN).build());
        marketComponentService.placeMarket(MarketTestBuilder.builder().withSsn("111111").withInstrument("SAAB").withAmount(BigDecimal.TEN).build());

        marketComponentService.placeMarket(MarketTestBuilder.builder().withSsn("222222").withInstrument("ABB").withAmount(BigDecimal.ONE).build());
        marketComponentService.placeMarket(MarketTestBuilder.builder().withSsn("222222").withInstrument("SAAB").withAmount(BigDecimal.TEN).build());
        marketComponentService.placeMarket(MarketTestBuilder.builder().withSsn("222222").withInstrument("ERICSSON").withAmount(BigDecimal.TEN).build());
        marketComponentService.placeMarket(MarketTestBuilder.builder().withSsn("222222").withInstrument("ABB").withAmount(BigDecimal.TEN).build());

        Assert.assertEquals(BigDecimal.valueOf(52.0), marketComponentService.getTotalMarketValueOfAllMarkets());

    }

}
