package se.lexicon.market.component.dao;

import com.so4it.component.dao.gs.AbstractSpaceDao;
import org.openspaces.core.GigaSpace;
import se.lexicon.market.component.entity.MarketDealEntity;
import se.lexicon.market.componment.dao.MarketDealDao;

public class MarketDealDaoImpl extends AbstractSpaceDao<MarketDealEntity, String> implements MarketDealDao {

    public MarketDealDaoImpl(GigaSpace gigaSpace) {
        super(gigaSpace);
    }

}
