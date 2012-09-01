package uk.co.karn.votestats;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import uk.co.karn.votestats.model.VoterStats;
import uk.co.karn.votestats.rest.api.ObjectFactory;
import uk.co.karn.votestats.rest.api.VoteCount;

/**
 *
 * @author tKe
 */
 enum VoteQuery {
    TOTALS, WEEKLY {
        @Override
        protected Query<VoterStats> processQuery(Query<VoterStats> query) {
            return super.processQuery(query).where().gt("vote_time", aWeekAgo()).query();
        }
    };
    private static final Function<VoterStats, VoteCount> TO_VOTECOUNT = new Function<VoterStats, VoteCount>() {
        private final ObjectFactory api = new ObjectFactory();

        @Override
        public VoteCount apply(VoterStats f) {
            return api.createVoteCount().withUsername(f.getUsername()).withCount(f.getCount());
        }
    };

    private static Long aWeekAgo() {
        return new Date().getTime() - 1000 * 60 * 60 * 24 * 7; // TODO: whole days?
    }

    protected final Query<VoterStats> query(EbeanServer db) {
        return processQuery(VoterStats.findIn(db));
    }

    protected Query<VoterStats> processQuery(Query<VoterStats> query) {
        return query;
    }

    public final List<VoteCount> execute(final EbeanServer db) {
        return Lists.newArrayList(Iterables.transform(query(db).findList(), TO_VOTECOUNT));
    }
    
}
