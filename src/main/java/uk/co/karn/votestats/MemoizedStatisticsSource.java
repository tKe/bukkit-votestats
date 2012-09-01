package uk.co.karn.votestats;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import java.util.List;
import java.util.concurrent.TimeUnit;
import uk.co.karn.votestats.rest.StatisticsSource;
import uk.co.karn.votestats.rest.api.VoteCount;

/**
 *
 * @author tKe
 */
public final class MemoizedStatisticsSource implements StatisticsSource {
    private final Supplier<List<VoteCount>> weekly;
    private final Supplier<List<VoteCount>> totals;

    public MemoizedStatisticsSource(final StatisticsSource statisticsSource, long i, TimeUnit timeUnit) {
        weekly = Suppliers.memoizeWithExpiration(new Supplier<List<VoteCount>>(){
            @Override
            public List<VoteCount> get() {
                return statisticsSource.weekly();
            }
        }, i, timeUnit);
        totals = Suppliers.memoizeWithExpiration(new Supplier<List<VoteCount>>(){
            @Override
            public List<VoteCount> get() {
                return statisticsSource.totals();
            }
        }, i, timeUnit);
    }

    @Override
    public List<VoteCount> totals() {
        return totals.get();
    }

    @Override
    public List<VoteCount> weekly() {
        return weekly.get();
    }
}
