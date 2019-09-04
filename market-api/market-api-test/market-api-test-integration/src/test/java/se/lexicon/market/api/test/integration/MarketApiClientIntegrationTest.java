package se.lexicon.market.api.test.integration;

import org.junit.*;
import org.mockito.Mockito;
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
import org.junit.experimental.categories.Category;
import org.junit.rules.RuleChain;
import org.openspaces.core.GigaSpace;
import se.lexicon.market.component.test.common.domain.MarketOrderTestBuilder;
import se.lexicon.order.api.client.OrderApiClient;
import se.lexicon.order.api.client.OrderApiProvider;


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

    //private static final OrderApiClient ORDER_API_CLIENT = Mockito.mock(OrderApiClient.class);

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
    public void testPlaceMarketOrder() throws Exception {
        MarketApiClient marketApiClient = BEAN_CONTEXT.getBean(MarketApiClient.class);
        Poller.pollAndCheck(SatisfiedWhenClientConnected.create(marketApiClient));

        Boolean ok = marketApiClient.placeMarketOrder(MarketOrderTestBuilder.builder().build());

        Assert.assertTrue(ok);

        Thread.sleep(3000);

    }

}