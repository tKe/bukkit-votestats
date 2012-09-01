package uk.co.karn.votestats.rest;

import java.util.List;
import uk.co.karn.votestats.rest.api.VoteCount;

/**
 *
 * @author tKe
 */
public interface StatisticsSource {
    List<VoteCount> totals();
    List<VoteCount> weekly();
}
