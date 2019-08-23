package se.lexicon.market.api.test.integration;

import com.google.common.collect.Sets;
import org.junit.Assert;
import se.lexicon.market.api.client.MarketApiClient;
import se.lexicon.market.api.client.MarketApiProvider;
import com.so4it.api.interceptor.request.RequestContextClientInterceptor;
import com.so4it.api.interceptor.request.RequestContextServerInterceptor;
import com.so4it.api.test.common.ApiFrameworkBootstrapTestRule;
import com.so4it.api.test.common.ApiFrameworkCommonTest;
import com.so4it.api.test.common.SatisfiedWhenClientConnected;
import com.so4it.common.bean.BeanContext;
import com.so4it.test.category.IntegrationTest;
import com.so4it.test.common.probe.Poller;
import com.so4it.test.gs.rule.ClearGigaSpaceTestRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.RuleChain;
import org.openspaces.core.GigaSpace;
import se.lexicon.market.component.test.common.domain.MarketOrderTestBuilder;

import java.time.Instant;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
@Category(IntegrationTest.class)
public class MarketApiClientIntegrationTest {

    @ClassRule
    public static final RuleChain SUITE_RULE_CHAIN = MarketApiIntegrationTestSuite.SUITE_RULE_CHAIN;


    @Rule
    public ClearGigaSpaceTestRule clearGigaSpaceTestRule = new ClearGigaSpaceTestRule(MarketApiIntegrationTestSuite.getComponentRule().getBean(GigaSpace.class));




    private static final BeanContext BEAN_CONTEXT = ApiFrameworkCommonTest.createClientBeanContext(MarketApiIntegrationTestSuite.DYNAMIC_CONFIGURATION);



    @Rule
    public ApiFrameworkBootstrapTestRule apiFrameworkBootstrapTestRule = ApiFrameworkBootstrapTestRule.builder()
            .withBeanContext(BEAN_CONTEXT)
            .withDynamicConfiguration(MarketApiIntegrationTestSuite.DYNAMIC_CONFIGURATION)
            .withApiRegistryClient(MarketApiIntegrationTestSuite.API_REGISTRY)
            .withImports(MarketApiProvider.class)
            .withExports()
            .withClientInterceptors(new RequestContextClientInterceptor())
            .withServerInterceptors(new RequestContextServerInterceptor())
            .build();



    @Test
    public void testCreatingMarketBalance() throws Exception {
        MarketApiClient marketApiClient = BEAN_CONTEXT.getBean(MarketApiClient.class);
        Poller.pollAndCheck(SatisfiedWhenClientConnected.create(marketApiClient));

        Boolean ok = marketApiClient.placeMarketOrder(MarketOrderTestBuilder.builder().build());

        Assert.assertTrue(ok);

//        MarketBalance marketBalanceOne = marketApiClient.createMarketBalance(CreateMarketBalanceRequest.builder()
//                .withArrangementId("1")
//                .withBatchId("aaa")
//                .withInsertionTimestamp(Instant.parse("2019-06-27T09:00:00.000Z"))
//                .withBalances(Sets.newHashSet(BalanceTestBuilder.builder().build())).build());
//        MarketBalance marketBalanceTwo = marketApiClient.createMarketBalance(CreateMarketBalanceRequest.builder()
//                .withArrangementId("1")
//                .withBatchId("bbb")
//                .withInsertionTimestamp(Instant.parse("2019-06-27T10:00:00.000Z"))
//                .withBalances(Sets.newHashSet(BalanceTestBuilder.builder().build())).build());
        //Optional<MarketBalance> marketBalanceOptional = marketComponentService.getMarketBalance(marketBalanceOne.getArrangementId());
        //Assert.assertTrue(marketBalanceOptional.isPresent());
        //Assert.assertEquals(Integer.valueOf(2),marketBalanceOptional.getMarketBalance().getSequenceNumber());
    }

    @Test
    public void testCreatingMarketTransaction() throws Exception {
        MarketApiClient marketApiClient = BEAN_CONTEXT.getBean(MarketApiClient.class);
        Poller.pollAndCheck(SatisfiedWhenClientConnected.create(marketApiClient));

        //MarketTransaction marketTransaction = marketApiClient.createMarketTransaction(CreateMarketTransactionRequestTestBuilder.builder().build());
    }

}