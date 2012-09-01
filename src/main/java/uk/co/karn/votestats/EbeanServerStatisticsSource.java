package uk.co.karn.votestats;

import com.avaje.ebean.EbeanServer;
import java.util.List;
import uk.co.karn.votestats.rest.StatisticsSource;
import uk.co.karn.votestats.rest.api.VoteCount;

public class EbeanServerStatisticsSource implements StatisticsSource {
    private final EbeanServer db;
    
    public EbeanServerStatisticsSource(EbeanServer db) {
        this.db = db;
    }

    @Override
    public List<VoteCount> totals() {
        return VoteQuery.TOTALS.execute(db);
    }

    @Override
    public List<VoteCount> weekly() {
        return VoteQuery.WEEKLY.execute(db);
    }
}
