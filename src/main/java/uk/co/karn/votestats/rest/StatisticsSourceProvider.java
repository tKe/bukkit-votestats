package uk.co.karn.votestats.rest;

import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author tKe
 */
//@Provider
public class StatisticsSourceProvider extends SingletonTypeInjectableProvider<Context, StatisticsSource> {
    public StatisticsSourceProvider(StatisticsSource instance) {
        super(StatisticsSource.class, instance);
    }
}
