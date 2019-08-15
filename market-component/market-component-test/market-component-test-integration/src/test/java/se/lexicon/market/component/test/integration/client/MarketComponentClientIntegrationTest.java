package se.lexicon.order.component.test.integration.client;

import se.lexicon.order.component.test.common.domain.OrderTestBuilder;
import com.so4it.test.category.IntegrationTest;
import com.so4it.test.gs.rule.ClearGigaSpaceTestRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.RuleChain;
import org.openspaces.core.GigaSpace;
import se.lexicon.order.component.client.OrderComponentClient;
import se.lexicon.order.component.entity.OrderEntity;
import se.lexicon.order.component.test.integration.service.OrderComponentServiceIntegrationTestSuite;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
@Category(IntegrationTest.class)
public class OrderComponentClientIntegrationTest {

    @ClassRule
    public static final RuleChain SUITE_RULE_CHAIN = OrderComponentServiceIntegrationTestSuite.SUITE_RULE_CHAIN;

    @Rule
    public ClearGigaSpaceTestRule clearGigaSpaceTestRule = new ClearGigaSpaceTestRule(OrderComponentServiceIntegrationTestSuite.getExportContext().getBean(GigaSpace.class));

    @Test
    public void testCreatingOrder(){
        OrderComponentClient orderComponentClient = OrderComponentServiceIntegrationTestSuite.getImportContext().getBean(OrderComponentClient.class);
        orderComponentClient.placeOrder(OrderTestBuilder.builder().build());


        Assert.assertEquals(1, OrderComponentServiceIntegrationTestSuite.getExportContext().getBean(GigaSpace.class).count(OrderEntity.templateBuilder().build()));

    }

}
