package se.lexicon.market.component.test.integration.client;

import com.so4it.test.common.probe.Poller;
import com.so4it.test.common.probe.SatisfiedWhenTrueReturned;
import se.lexicon.market.component.client.MarketComponentClient;
import se.lexicon.market.component.entity.MarketOrderEntity;
import se.lexicon.market.component.test.common.domain.MarketOrderTestBuilder;
import se.lexicon.market.component.test.integration.service.MarketComponentServiceIntegrationTestSuite;
import com.so4it.test.category.IntegrationTest;
import com.so4it.test.gs.rule.ClearGigaSpaceTestRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.RuleChain;
import org.openspaces.core.GigaSpace;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
@Category(IntegrationTest.class)
public class MarketComponentClientIntegrationTest {

    @ClassRule
    public static final RuleChain SUITE_RULE_CHAIN = MarketComponentServiceIntegrationTestSuite.SUITE_RULE_CHAIN;

    @Rule
    public ClearGigaSpaceTestRule clearGigaSpaceTestRule = new ClearGigaSpaceTestRule(MarketComponentServiceIntegrationTestSuite.getExportContext().getBean(GigaSpace.class));

    @Test
    public void testCreatingMarketOrder() throws InterruptedException {
        MarketComponentClient marketorderComponentClient = MarketComponentServiceIntegrationTestSuite.getImportContext().getBean(MarketComponentClient.class);
        marketorderComponentClient.placeMarketOrder(MarketOrderTestBuilder.builder().build());

        Poller.pollAndCheck(SatisfiedWhenTrueReturned.create(() -> MarketComponentServiceIntegrationTestSuite.getExportContext().getBean(GigaSpace.class).count(MarketOrderEntity.templateBuilder().build()) == 1));
    }

}
