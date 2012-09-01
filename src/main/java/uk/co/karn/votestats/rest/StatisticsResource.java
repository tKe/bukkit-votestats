package uk.co.karn.votestats.rest;

import com.sun.jersey.api.core.InjectParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import uk.co.karn.votestats.rest.api.ObjectFactory;
import uk.co.karn.votestats.rest.api.Statistics;

/**
 *
 * @author tKe
 */
@Path("stats")
public class StatisticsResource {

    private static final ObjectFactory api = new ObjectFactory();
    
    @InjectParam private StatisticsSource stats;

    @GET
    @Produces({MediaType.APPLICATION_XML})
    public Statistics getStats() {
        return api.createStatistics()
                .withTotal(stats.totals())
                .withWeekly(stats.weekly());
    }
}
