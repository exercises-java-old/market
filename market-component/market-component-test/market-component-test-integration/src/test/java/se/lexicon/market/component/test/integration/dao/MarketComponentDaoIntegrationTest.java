package se.lexicon.market.component.test.integration.dao;

import se.lexicon.market.component.entity.MarketEntity;
import se.lexicon.market.component.test.common.entity.MarketEntityTestBuilder;
import com.so4it.test.category.IntegrationTest;
import com.so4it.test.gs.rule.ClearGigaSpaceTestRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.RuleChain;
import org.openspaces.core.GigaSpace;
import se.lexicon.market.component.test.integration.service.MarketComponentServiceIntegrationTestSuite;
import se.lexicon.market.componment.dao.MarketOrderDao;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
@Category(IntegrationTest.class)
public class MarketComponentDaoIntegrationTest {

    @ClassRule
    public static final RuleChain SUITE_RULE_CHAIN = MarketComponentDaoIntegrationTestSuite.SUITE_RULE_CHAIN;

    @Rule
    public ClearGigaSpaceTestRule clearGigaSpaceTestRule = new ClearGigaSpaceTestRule(MarketComponentServiceIntegrationTestSuite.getExportContext().getBean(GigaSpace.class));

    @Test
    public void testInsertingMarket(){
        MarketOrderDao marketOrderDao = MarketComponentDaoIntegrationTestSuite.getExportContext().getBean(MarketOrderDao.class);
        MarketEntity marketEntity = marketOrderDao.insert(MarketEntityTestBuilder.builder().build());
    }

}
